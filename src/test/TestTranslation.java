package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.SatBeam;
import scraper.SatBeamScraper;
import scraper.WebsiteData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TestTranslation {

    protected static final Logger logger = LogManager.getLogger();

    private static SatBeamScraper scraper = new SatBeamScraper();

    public int checkWhetherSatBeamListIsScrapedProperly(int rangeStart, int rangeEnd, String status){
        logger.info(">>> Test Whether Scraper Properly Fetch Data From Website <<<");
        List<SatBeam> satellites = new ArrayList<SatBeam>();

        try{
            satellites = scraper.ScrapeData(rangeStart,rangeEnd, status);
        }catch (Exception er){
            logger.error(er.getMessage());
            return 1;
        }


        for(SatBeam SingleSat : satellites){
            System.out.println(SingleSat.toString());
        }

        return 0;

    }

    public int checkWhetherSatBeamIsTranslatedToWebsiteSatellites(int satID) throws ParseException {
        System.out.println("\n\n");
        logger.info(">>> Test Whether SatBeam Object Is Properly Translated To Satellite Object <<<");

        List<SatBeam> satellites = new ArrayList<SatBeam>();

        try{
            satellites = scraper.ScrapeData(satID,satID, "any");
        }catch (Exception er){
            logger.error("While scraping data something goes wrong:" + er);
            return 1;
        }


        WebsiteData.Satellite testSat = new WebsiteData.Satellite();
        ;
        try {
            testSat = satellites.getFirst().toSatellite();
        }catch (Exception err){
            logger.error("Cannot translate SatBeam Object to WebsiteData.Satellite Object: " + err);
            return 1;
        }
        logger.info("Successfully translate SatBeam with id = " + satID + " to WebsiteData.Satellite Object");
        System.out.println(testSat.toString());
        return 0;
    }

    public int checkWhetherSatBeamDataIsTranslatedToExistingWebsiteSatellite(int satID){
        System.out.println("\n\n");
        logger.info(">>> Test Whether SatBeam Object Is Properly Translated and Update Data in Already Created Satellite <<<");

        List<SatBeam> satellites = new ArrayList<SatBeam>();

        try{
            satellites = scraper.ScrapeData(satID,satID, "any");
        }catch (Exception er){
            logger.error("While scraping data something goes wrong:" + er);
            return 1;
        }

        SatBeam satBeam = new SatBeam();
        try {
           satBeam = satellites.getFirst();
        }catch (Exception err){
            logger.error("Problem while extracting Sat with given ID from list: " + err);
            return 1;
        }
        ;
        logger.info("Created for tests WebsiteData.Satellite Object");
        WebsiteData.Satellite testSat = new WebsiteData.Satellite();
        testSat.setStatus("dunno");
        testSat.setOperator("Some Guy");
        testSat.setLaunchSite("North or West");
        testSat.setLaunchMass(69696969.0);
        testSat.setSatelliteModel("LEGO Super Extra Satellite");

        System.out.println(testSat.toString() + "\n\n");


        try{
            testSat = satBeam.updateSatelliteBySatBeam(testSat);
            logger.info("Created WebsiteData.Satellite Object after update");
            System.out.println(testSat.toString());
            return 0;
        }catch (Exception er){
            logger.error("Error while updating WebsiteData.Satellite Object occured" + er);
            return 1;
        }
    }

    public int checkWhetherSatBeamListIsTranslatedToWebsiteSatellitesList(int rangeStart, int rangeEnd) throws ParseException {
        System.out.println("\n\n");
        logger.info(">>> Test Whether SatBeam Object List Is Properly Translated To Satellite Object List <<<");

        List<SatBeam> satellites = new ArrayList<SatBeam>();

        try{
            satellites = scraper.ScrapeData(rangeStart,rangeEnd, "active");
        }catch (Exception er){
            logger.error("While scraping data something goes wrong:" + er);
            return 1;
        }

        try{
            ArrayList<WebsiteData.Satellite> testSattelites = scraper.SatBeamListToSatellites(satellites);
            for(WebsiteData.Satellite oneSattelite: testSattelites){
                System.out.println(oneSattelite.toString());
            }
        }catch (Exception er){
            logger.error("While translating List<SatBeam> to List<WebsiteData.Satellite> some error occured: " + er);
            return 1;
        }


        return 0;
    }

}
