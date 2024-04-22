package OtherGroupsWork.KingOfSats;
import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

public class rapersc {
	//w tym arrayu sa wszystkie informacje z glownej strony, zalezy nam na pozycji, nazwie, norad, longitude; wszystkie te rzeczy sa na staluch indeksach
	private static ArrayList<ArrayList<String>> scrapedDataMain = new ArrayList<ArrayList<String>>();
	//3d array bo czemu nie, sa tu rzeczy z podstrony, kazdy kolejny rekord tabeli jest jako kolejny array w liscie, UWAGA PIERWSZY ELEMENT TABELI JEST ZAWSZE OPISEM PÓL - TRZEBA POMINAC, te tabele nie sa tej samej dlugosci!!!!
	//powodzenia ;3 
	private static ArrayList<ArrayList<ArrayList<String>>> scrapedDataInSat = new ArrayList<ArrayList<ArrayList<String>>>();
	
	public rapersc() throws IOException {
		Document doc;
		Elements body = null;
		try {
		    doc = Jsoup.connect("https://pl.kingofsat.net/satellites.php").timeout(6000).get();
		    body = doc.select("tbody");
		} catch (IOException e) {
		    e.printStackTrace();
		}

		if (body != null) {
		    for (Element e : body.select("tr")) {
		        ArrayList<String> mainSatInfo = new ArrayList<String>();
		        for (Element x : e.select("td")) {
		            mainSatInfo.add(x.text());
		        }
		        scrapedDataMain.add(mainSatInfo);
		    }
		}

		int i = 0;
		int counter = 0;
		for (Element e : body.select("tr td a.A3")) {
		    if (i % 2 != 0) {
		        String address = e.attr("href");
		        Document inSat = null; 
		        try {
		            inSat = Jsoup.connect("https://pl.kingofsat.net/" + address).timeout(6000).get();
		        } catch (IOException e1) {
		            e1.printStackTrace();
		        }

		        if (inSat != null) {
					try {
						System.out.write(("\rKingOfSat: " + counter).getBytes());
					}catch (Exception er){}
					counter++;
		            Elements inSatBody = inSat.select("table.frq tbody");
		            ArrayList<ArrayList<String>> satInfoContainer = new ArrayList<ArrayList<String>>();
		            for (Element ine : inSatBody.select("tr")) {
		                ArrayList<String> smallSatInfo = new ArrayList<String>();
		                for (Element x : ine.select("td")) {
		                    smallSatInfo.add(x.text());
		                }
		                satInfoContainer.add(smallSatInfo);
		            }
		            scrapedDataInSat.add(satInfoContainer);
		        }
		    }
		    i++;	
		}

		// Remove every "moving" satelite.
		for (i = 0; i < scrapedDataMain.size(); ) {
			String name = scrapedDataMain.get(i).get(1);
			if (name.contains("Moving")) {
				scrapedDataMain.remove(i);
				continue;
			}
			i++;
		}
		System.out.println("\n");
	}
	
//	public static void main(String[] args) throws IOException {
//
//	}
	
	ArrayList<ArrayList<String>> getscrapedDataMain (){
		return scrapedDataMain;
	}
	
	ArrayList<ArrayList<ArrayList<String>>> getscrapedDataInSat (){
		return scrapedDataInSat;
	}

}
