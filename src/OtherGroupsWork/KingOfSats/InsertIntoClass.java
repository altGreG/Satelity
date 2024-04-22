package OtherGroupsWork.KingOfSats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import scraper.WebsiteData;
import scraper.WebsiteData.Satellite;
import scraper.WebsiteData.Satellite.Transmitter;

public class InsertIntoClass extends WebsiteData {
    private static rapersc webScraper;

    static {
        try {
            webScraper = new rapersc();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<ArrayList<String>> scrapedDataMain = webScraper.getscrapedDataMain();
    private static ArrayList<ArrayList<ArrayList<String>>> scrapedDataInSat = webScraper.getscrapedDataInSat();

    private ArrayList<Satellite> satelitaList = new ArrayList<>();
    public ArrayList<Satellite> getSatelitaList(){
    	return this.satelitaList;
    }
    private static ArrayList<ArrayList<Transmitter>> oneSatTransList = new ArrayList<>();

    private static WebsiteData sateliteClass = new WebsiteData();

    public InsertIntoClass() {
        extractTransmitterData();
        this.satelitaList = extractData();
    }
    
    static int counter = 0;
    
    public static ArrayList<Satellite> extractData() {
    	ArrayList<Satellite> satList = new ArrayList();
        for (ArrayList<String> data_set : scrapedDataMain) {

        	
///        	
        	if (counter == scrapedDataMain.size() - 1) {
        		continue;
        	}
            Satellite satelita = new Satellite();
            String s = data_set.get(1);
            if (s.indexOf("(") == -1 || s.indexOf(")") == -1) {
                satelita.setNames(new ArrayList<>(Arrays.asList(data_set.get(1))));
            }
            else {
	            ArrayList<String> names = new ArrayList<String>();
	            names.add(s.substring(0, s.indexOf("(")));
	            names.add(s.substring(s.indexOf("(") + 1, s.indexOf(")")));
	            satelita.setNames(names);
            }
            
            satelita.setNorad(Integer.parseInt(data_set.get(2)));
            float OrbitalPosition = parseAngle(data_set.get(0));
            float actualPosition = parseAngle(data_set.get(7));        
            satelita.setOrbitalPosition(OrbitalPosition);
            satelita.setActualPosition(actualPosition);
            
            satelita.setTransmitters(oneSatTransList.get(counter));
            satList.add(satelita);
            counter++;
        }
       //satelitaList.get(96).printSateliteData();
//       System.out.println(satelitaList.get(96).getNames());
        return satList;
    }

    public static void extractTransmitterData() {
        for (ArrayList<ArrayList<String>> data_set : scrapedDataInSat) {
            ArrayList<Transmitter> transmittersList = new ArrayList<>();
            for (int i = 1; i < data_set.size(); i++) {
                ArrayList<String> one_transmitter = data_set.get(i);
                Transmitter transmitter = new Satellite().new Transmitter();

                String frequencyStr = one_transmitter.get(2).replace(",", "");
                Float frequency = Float.parseFloat(frequencyStr);
                transmitter.setFrequency(frequency);
                transmitter.setModulation(one_transmitter.get(7));
                transmitter.setPolarisation(one_transmitter.get(3).toCharArray()[0]);
                transmitter.setBeam(one_transmitter.get(5));
                transmitter.setStandard(one_transmitter.get(6));
                transmittersList.add(transmitter);
            }
            oneSatTransList.add(transmittersList);
        }
    }

    public static float parseAngle(String angle) {
        Pattern pattern = Pattern.compile("([0-9.]+)°?([EW])");
        Matcher matcher = pattern.matcher(angle);

        if (matcher.find()) {
            float number = Float.parseFloat(matcher.group(1));
            String direction = matcher.group(2);
            if ("W".equals(direction)) {
                number = -number;
            }
            return number;
        }
        return Float.NaN;
    }
}
