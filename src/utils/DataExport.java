package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import scraper.WebsiteData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DataExport {

    public static void saveToExcel(ArrayList<ArrayList<WebsiteData.Satellite>> sats, String[] whichSite) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream out;

        int counter = -1;
        for(ArrayList<WebsiteData.Satellite> satList : sats){
            counter++;
            XSSFSheet spreadsheet = workbook.createSheet(whichSite[counter]);
            int rownum = 0;
            for (WebsiteData.Satellite sat : satList)
            {
                Row row = spreadsheet.createRow(rownum++);
                createList(sat, row);
            }
            String filePath = "resources/Satellites.xlsx";
            out = new FileOutputStream(new File(filePath));
            workbook.write(out);
            out.close();
        }



    }

    private static void createList(WebsiteData.Satellite sat, Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue(sat.getNames().get(0));

        cell = row.createCell(1);
        cell.setCellValue(sat.getNorad());

        cell = row.createCell(2);
        cell.setCellValue(sat.getNorad());

        cell = row.createCell(3);
        cell.setCellValue(sat.getOperator());

        cell = row.createCell(4);
        cell.setCellValue(sat.getStatus());

        cell = row.createCell(5);
        cell.setCellValue(sat.getOrbitalPosition());

        cell = row.createCell(6);
        cell.setCellValue(sat.getActualPosition());

        cell = row.createCell(7);
        cell.setCellValue(sat.getLaunchDate());

        cell = row.createCell(8);
        cell.setCellValue(sat.getLaunchSite());

        cell = row.createCell(9);
        cell.setCellValue(sat.getLaunchVehicle());

        cell = row.createCell(10);
        cell.setCellValue(sat.getSatelliteManufacturer());

        cell = row.createCell(11);
        cell.setCellValue(sat.getSatelliteModel());

        cell = row.createCell(12);
        cell.setCellValue(sat.getSatelliteExpectedLifetime());

        // TODO: ADD TRANSMITTERS
    }

}
