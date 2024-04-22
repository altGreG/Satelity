import OtherGroupsWork.KingOfSats.InsertIntoClass;
import OtherGroupsWork.LyngSat.LyngSat;
import OtherGroupsWork.Merge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.SatBeam;
import scraper.SatBeamScraper;
import scraper.WebsiteData;
import utils.DataExport;
import utils.Serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu {
    protected static final Logger menu = LogManager.getLogger();
    private static LyngSat scraperLyngSat = new LyngSat();

    private static Merge merge = new Merge();
    public int startMenu() throws InterruptedException {

        boolean run = true;
        boolean runMainChoice = true;
        while(run){


                System.out.println("===================== Satellites Project Menu ==========");
                System.out.println("Choose action ");
            System.out.println("  - 1: Scrape Data");
            System.out.println("  - 2: Load Data from Database");
            System.out.println("  - 0: End Program");


            int choice = -1;
            while(runMainChoice){
                System.out.print("Choose: ");
                try{
                    Scanner scanner = new Scanner(System.in);
                    choice = scanner.nextInt();
                    if(choice != 1 && choice !=2 && choice !=0){
                        throw new Exception("Wrong value. Try again!\n");
                    }

                    runMainChoice = false;
                }catch (Exception er){
                    menu.warn(er.getMessage());
                }
            }


            switch (choice) {
                case 0:
                    return 0;
                case 1:
                    run = false;
                    scrapeMenu("scrape_data");
                    break;
                case 2:
                    run = false;
                    scrapeMenu("load_data");

            }

        }

        return 0;
    }

    public void scrapeMenu(String choice) throws InterruptedException {
        boolean runScraperMenu = true;
        int rangeStart = 0;
        int rangeEnd = 0;
        String status = "";
        Scanner scanner = new Scanner(System.in);

        if(Objects.equals(choice, "scrape_data")){
            System.out.println("\n===================== Set Options For Scraper ==========");
            while(runScraperMenu){
                System.out.print("Range Start (ID of first Sat to scrap; min 1): ");
                try{
                    rangeStart = scanner.nextInt();

                    if(rangeStart <= 0 || rangeStart >= 609) {
                        throw new Exception("Start of range must be between 1 and 608");
                    }
                    runScraperMenu = false;
                }catch (Exception er){
                    menu.warn(er.getMessage());
                }
            }

            runScraperMenu = true;
            while(runScraperMenu){
                System.out.print("Range End (ID of last Sat to scrap; max 608): ");
                try{
                    rangeEnd = scanner.nextInt();

                    if(rangeEnd > 609 || rangeEnd <= rangeStart-1){
                        throw new Exception("End of range must be between " + rangeStart + " and 608");
                    }
                    runScraperMenu = false;
                }catch (Exception er){
                    menu.warn(er.getMessage());
                }
            }

            runScraperMenu = true;
            while(runScraperMenu){
                System.out.print("Status(any, active, deorbited, failed, retired) ");
                System.out.print("Status (Info about Sats current condition): ");
                try{
                    status = scanner.next();

                    if(!status.contains("active") && !status.contains("deorbited") && !status.contains("retired") && !status.contains("failed") && !status.contains("any")){
                        throw new Exception("Provided wrong type of status");
                    }
                    runScraperMenu = false;
                }catch (Exception er){
                    menu.warn(er.getMessage());
                }
            }

            runScraperMenu = false;

//        System.out.println("Chooses");
//        System.out.println("Chooses 1: " + rangeStart);
//        System.out.println("Chooses 2: " + rangeEnd);
//        System.out.println("Chooses 3: " + status);

            useScraper(rangeStart, rangeEnd, status); 
        } else if (Objects.equals(choice,"load_data")) {
            Serialization service = new Serialization();
            try{
                System.out.println("\n");

                WebsiteData SatBeam = service.SerializeInput("satbeam");
                List<WebsiteData.Satellite> satsSatBeamA1Merge;
                List<WebsiteData.Satellite> satsSatBeamA2Merge;
                WebsiteData LyngSat = service.SerializeInput("lyngsat");
                WebsiteData KingOfSat = service.SerializeInput("kingofsat");

                List<WebsiteData.Satellite> satList = new ArrayList<>();

                System.out.println("\n");

                satsSatBeamA1Merge = merge.mergeOtherSatToSatBeam(LyngSat.getSatellites(), SatBeam.getSatellites(), "lyngsat");
                satsSatBeamA2Merge = merge.mergeOtherSatToSatBeam(KingOfSat.getSatellites(), satsSatBeamA1Merge, "kingofsat");

                System.out.println("\n");

                List<WebsiteData.Satellite> LyngSatNotInSatBeam = merge.listOfOtherSatNotInSatBeam(LyngSat.getSatellites(), SatBeam.getSatellites());
                List<WebsiteData.Satellite> KingOfSatNotInSatBeam = merge.listOfOtherSatNotInSatBeam(KingOfSat.getSatellites(), SatBeam.getSatellites());

                System.out.println("\n");


                // after merging
                System.out.println("Adding SatBeam To List: " + satsSatBeamA2Merge.size());
                satList.addAll(satsSatBeamA2Merge);
                System.out.println("Adding LyngSat To List: " + LyngSatNotInSatBeam.size());
                satList.addAll(LyngSatNotInSatBeam);
                System.out.println("Adding KingOfSat To List: " + KingOfSatNotInSatBeam.size() + "\n");
                satList.addAll(KingOfSatNotInSatBeam);

                ArrayList<ArrayList<WebsiteData.Satellite>> sats = new ArrayList<>();
                sats.add((ArrayList<WebsiteData.Satellite>) satsSatBeamA2Merge);
                sats.add((ArrayList<WebsiteData.Satellite>) LyngSatNotInSatBeam);
                sats.add((ArrayList<WebsiteData.Satellite>) KingOfSatNotInSatBeam);

                String[] siteNames = {"SatBeam","LyngSat","KingOfSat"};


                // export to Excel
                try{
                    DataExport.saveToExcel(sats, siteNames);
                }catch (Exception er){
                    menu.warn("Error while saving to excel: " + er);
                }


                // here we can skip function useScraper and go to useData
                useData(satList);

            }catch (Exception er){
                menu.warn("Can't deserialize data: " + er);
            }
        }


    }

    public void useScraper(int rangeStart, int rangeEnd, String status) throws InterruptedException {
        SatBeamScraper scraper = new SatBeamScraper();
        List<SatBeam> SatBeamList = new ArrayList<>();
        List<WebsiteData.Satellite> SatelliteList = new ArrayList<>();

        System.out.println("\n");
        try {
            // scrape SatBeam
            SatBeamList = scraper.ScrapeData(rangeStart, rangeEnd, status);
            List<WebsiteData.Satellite> satsSatBeam = scraper.SatBeamListToSatellites(SatBeamList);
            Serialization service = new Serialization();
            service.SerializeOutput(new WebsiteData().setSatellites((ArrayList<WebsiteData.Satellite>) satsSatBeam), "satbeam");

            // scrape LyngSat
            System.out.println("Scraping LyngSat");
            List<WebsiteData.Satellite> satsLyngSat = scraperLyngSat.ScrapeLyngSat();
            service.SerializeOutput(new WebsiteData().setSatellites((ArrayList<WebsiteData.Satellite>) satsLyngSat), "lyngsat");
            List<WebsiteData.Satellite> LyngSatNotInSatBeam = merge.listOfOtherSatNotInSatBeam(satsLyngSat, satsSatBeam);

            // scrape KingOfSat
            WebsiteData KingOfSat = new WebsiteData();
            InsertIntoClass GETKingOfSat = new InsertIntoClass();
            List<WebsiteData.Satellite> satsKingOfSat = GETKingOfSat.getSatelitaList();
            KingOfSat.setSatellites((ArrayList<WebsiteData.Satellite>) satsKingOfSat);
            service.SerializeOutput(KingOfSat, "kingofsat");
            List<WebsiteData.Satellite> KingOfSatNotInSatBeam = merge.listOfOtherSatNotInSatBeam(KingOfSat.getSatellites(),satsSatBeam);

            // merging
            satsSatBeam = merge.mergeOtherSatToSatBeam(satsLyngSat, satsSatBeam, "lyngsat");
            satsSatBeam = merge.mergeOtherSatToSatBeam(satsKingOfSat, satsSatBeam, "kingofsat");


            // after merging
            System.out.println("Adding SatBeam To List: " + satsSatBeam.size());
            SatelliteList.addAll(satsSatBeam);
            System.out.println("Adding LyngSat To List: " + LyngSatNotInSatBeam.size());
            SatelliteList.addAll(LyngSatNotInSatBeam);
            System.out.println("Adding KingOfSat To List: " + KingOfSatNotInSatBeam.size());
            SatelliteList.addAll(KingOfSatNotInSatBeam);
        }catch (Exception er){
            menu.warn("Error in useScraper: " + er);
        }

        useData(SatelliteList);
    }

    public void useData(List<WebsiteData.Satellite> SatelliteList) throws InterruptedException {
        boolean runUseDataMenu = true;
        boolean runUseDataChoice = true;

        System.out.println("Count of all Satellites from SatBeam, KingOfSat and LyngSat: " + SatelliteList.size());

        while(runUseDataMenu){
            runUseDataChoice = true;
            System.out.println("\n================== Choose what to do with data ==========");
            System.out.println("\nChoose action ");
            System.out.println("  - 1: Print Sat's In Reagard To Given Info");
            System.out.println("  - 2: Print All Sat's");
            System.out.println("  - 3: End Program");

            int choice = -1;
            while(runUseDataChoice) {
                System.out.print("Choose: ");
                try {
                    Scanner scanner = new Scanner(System.in);
                    choice = scanner.nextInt();
                    if (choice != 1 && choice != 2 && choice != 3) {
                        throw new Exception("Wrong value. Try again!\n");
                    }

                    runUseDataChoice = false;
                } catch (Exception er) {
                    menu.warn(er.getMessage());
                }
            }

            switch (choice) {
                case 1:
                    System.out.println("Satellite One");
                    printSatBySetInfo(SatelliteList);
                    break;
                case 2:
                    int whichSat = 0;
                    String printAll = "no";

                    for(WebsiteData.Satellite sat: SatelliteList){
                        System.out.println(sat.toString());
                        whichSat++;
                        Thread.sleep(100);
                        if(printAll != "all"){
                            if(whichSat % 5 == 0){

                                System.out.print("Provide 'n' and click Enter to continue, and print next 5 Satellites or provide 'all' and click Enter to print all remaining Satellites. ");

                                String cont = "random";
                                Scanner scanner = new Scanner(System.in);
                                while(!cont.equals("n") && !cont.equals("all")) {
                                    System.out.print("\nProvide choice: ");
                                    cont = scanner.nextLine();
                                    if(cont.equals("all")){
                                        printAll = "all";
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    runUseDataMenu = false;
                    break;
            }

        }
    }

    public void printSatBySetInfo(List<WebsiteData.Satellite> SatelliteList){
        boolean getInfo = true;
        List<WebsiteData.Satellite> SortedSatList = new ArrayList<>();

        String name = "";
        int norad = 0;

        List<String> similarNames = new ArrayList<>();




        while (getInfo){
            getInfo = true;
            System.out.println("\n================= Choose type of sorting ================");
            System.out.println("  - 1: By name");
            System.out.println("  - 2: By norad");
//            System.out.println("  - 3: By operator");
//            System.out.println("  - 4: By launch site");
//            System.out.println("  - 5: Manufacturer");
//            System.out.println("  - 6: Orbital Position");

            int choice = -1;
            Scanner scanner = new Scanner(System.in);
            while(getInfo){
                try {
                    System.out.print("Choose: ");
                    choice = scanner.nextInt();
                    if (choice != 1 && choice != 2 && choice != 3 && choice != 4) {
                        throw new Exception("Wrong value. Try again!\n");
                    }

                    getInfo = false;
                }catch (Exception er){
                    menu.warn("Error while choosing sort method: " + er);
                }
            }

            boolean runSort = true;
            switch (choice) {
                case 1:

                    while(runSort){
                        System.out.print("Type name: ");
                        name = scanner.next();

                        boolean nameExist = false;
                        for (WebsiteData.Satellite sat : SatelliteList){
                            if(sat.getNames().get(0).toLowerCase().contains(name.toLowerCase())){
                                SortedSatList.add(sat);
                                similarNames.add(sat.getNames().get(0));
                                nameExist = true;
                            }
                        }

                        boolean runNameChoose = true;
                        if(nameExist){
                            while(runNameChoose){
                                int whichName=0;
                                System.out.println("\nNames Similar to Given:");
                                System.out.println("   - -1"+": End Sort");

                                for(String tempName: similarNames){
                                    System.out.println("   - " + whichName + ": " + tempName);
                                    whichName++;
                                }


                                int choosedName = -1;
                                try{
                                    System.out.print("\nChoose sat: ");
                                    choosedName = scanner.nextInt();
                                }catch (Exception er){
                                    menu.warn("Error while choosing sat name");
                                }

                                if(Objects.equals(choosedName, -1)){
                                    runNameChoose = false;
                                }else {
                                    System.out.println(SortedSatList.get(choosedName).toString());
                                }

                                runSort = false;
                            }
                        }else {
                            System.out.println("There is no Satellite with name similar to provided.");
                        }
                    }



                    break;
                case 2:
                    System.out.print("Type Norad: ");
                    norad = scanner.nextInt();

                    boolean exist = false;
                    for (WebsiteData.Satellite sat : SatelliteList){
                        if(sat.getNorad() == norad){
                            System.out.print(sat.toString());
                            exist = true;
                        }
                    }
                    if(!exist){
                        System.out.print("There is no Sat with provided Norad");
                    }
                    break;
            }
        }

    }

}
