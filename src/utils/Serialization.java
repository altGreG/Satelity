package utils;

import scraper.WebsiteData;

import java.io.*;
import java.util.List;

public class Serialization {

    //types SatBeam, LyngSat and KingOfSat
    public static void SerializeOutput(WebsiteData site, String whichSite) throws IOException {

        FileOutputStream siteFile = new FileOutputStream("resources/error.data");
        if(whichSite.toLowerCase().contains("satbeam")){
            siteFile = new FileOutputStream("resources/SatBeam.data");
        } else if (whichSite.toLowerCase().contains("lyngsat")) {
            siteFile = new FileOutputStream("resources/LyngSat.data");
        } else if (whichSite.toLowerCase().contains("kingofsat")) {
            siteFile = new FileOutputStream("resources/KingOfSat.data");
        }

        ObjectOutputStream siteOut = new ObjectOutputStream(siteFile);

        siteOut.writeObject(site);

        siteOut.close();
        siteFile.close();
        System.out.println(whichSite.toUpperCase() + " has been serialized");

    }

    public static WebsiteData SerializeInput(String whichSite) throws IOException, ClassNotFoundException {

        FileInputStream siteFile = new FileInputStream("resources/error.data");

        if(whichSite.toLowerCase().contains("satbeam")){
            siteFile = new FileInputStream("resources/SatBeam.data");
        } else if (whichSite.toLowerCase().contains("lyngsat")) {
            siteFile = new FileInputStream("resources/LyngSat.data");
        } else if (whichSite.toLowerCase().contains("kingofsat")) {
            siteFile = new FileInputStream("resources/KingOfSat.data");
        }

        ObjectInputStream siteIn = new ObjectInputStream(siteFile);

        WebsiteData deserializedSite = (WebsiteData) siteIn.readObject();

        siteIn.close();
        siteFile.close();

        System.out.println(whichSite.toUpperCase() + " has been deserialized");

        return deserializedSite;

    }
}
