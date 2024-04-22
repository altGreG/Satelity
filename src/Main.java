import OtherGroupsWork.KingOfSats.InsertIntoClass;
import OtherGroupsWork.LyngSat.LyngSat;
import OtherGroupsWork.Merge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.SatBeam;
import scraper.SatBeamScraper;
import scraper.WebsiteData;
import test.TestKingOfSat;
import test.TestTranslation;
import utils.Serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    protected static final Logger main = LogManager.getLogger();

    private static SatBeamScraper scraper = new SatBeamScraper();

    private static LyngSat scraperLyngSat = new LyngSat();

    private static Merge merge = new Merge();

    private static TestTranslation testTranslation = new TestTranslation();
    private  static TestKingOfSat testKingOfSat = new TestKingOfSat();

    private static Menu menu = new Menu();

    public Main() {}

    public static void main(String[] args) throws Exception {
        main.info("Start of APP\n\n");

        try{
            menu.startMenu();
        }catch (Exception er){
            main.error("Error: " + er.getMessage());
        }finally {
            System.out.println("Piwo dobre :)");
        }

//        WebsiteData AllData = new WebsiteData();
//
//        System.out.println("Scraping SatBeams");
//        List<SatBeam> sats = scraper.ScrapeData(1,1, "any");
//        List<WebsiteData.Satellite> satsSatBeam = scraper.SatBeamListToSatellites(sats);

//        System.out.println("Scraping LyngSat");
//        WebsiteData LyngSat = new WebsiteData();
//        List<WebsiteData.Satellite> satsLyngSat = scraperLyngSat.ScrapeLyngSat();
//        LyngSat.setSatellites((ArrayList<WebsiteData.Satellite>) satsLyngSat);
//
////        satsLyngSat = merge.mergeSatBeamToLyngSat(satsSatBeam, satsLyngSat);
////        satsSatBeam = merge.mergeLyngSatToSatBeam(satsLyngSat,satsSatBeam);
//
//        List<WebsiteData.Satellite> LyngSatNotInSatBeam = merge.listOfLyngSatNotInSatBeam(satsLyngSat, satsSatBeam);
//
////        for(WebsiteData.Satellite sat : satsLyngSat){
////            System.out.println(sat.toString());
////        }
//
//            try{
//                Serialization service = new Serialization();
//
//                service.SerializeOutput(LyngSat, "lyngsat");
//
//                WebsiteData deserialized = service.SerializeInput("lyngsat");
//
//                int counter = 0;
//                for(WebsiteData.Satellite sat : deserialized.getSatellites()){
//                    System.out.println(counter + ". " + sat.getNames().getFirst());
//                    counter ++;
//                }
//                counter = 0;
//            }catch (Exception er){
//                main.warn("Problem with serialization" + er);
//            }
//        Serialization service = new Serialization();

//        // KingOfSat
//        WebsiteData KingOfSat = new WebsiteData();
//        try {
//            InsertIntoClass GETKingOfSat = new InsertIntoClass();
//            List<WebsiteData.Satellite> SatsKingOfSat = GETKingOfSat.getSatelitaList();
//            KingOfSat.setSatellites((ArrayList<WebsiteData.Satellite>) SatsKingOfSat);
//
//    		int counter = 0;
//    		for(WebsiteData.Satellite sats : KingOfSat.getSatellites()) {
//    			System.out.println(sats.toString());
//    			counter ++;
//    		}
////    		System.out.println("Wypisano " + counter + " satelit z KingOfSats");
//
//        }catch(Exception er) {System.out.println("Blad w KingOfSat");}
//
//        WebsiteData SatBeam = service.SerializeInput("satbeam");
//        merge.mergeKingOfSatToSatBeam(KingOfSat.getSatellites(), SatBeam.getSatellites() );
//
//        merge.listOfKingOfSatNotInSatBeam(KingOfSat.getSatellites(), SatBeam.getSatellites());

//        //test merging SatBeam with Other Sites
//        Serialization service = new Serialization();
//        List<WebsiteData.Satellite> satSatBeam = service.SerializeInput("satbeam").getSatellites();
//        List<WebsiteData.Satellite> satLyngSat = service.SerializeInput("lyngsat").getSatellites();
//        List<WebsiteData.Satellite> satKingOfSat = service.SerializeInput("kingofsat").getSatellites();
//
//
//        for(WebsiteData.Satellite updatedSat : merge.mergeSatBeamToOtherSat(satSatBeam, satLyngSat)){
//            System.out.println(updatedSat.toString());
//        }

        main.info("End of program");

    }
}