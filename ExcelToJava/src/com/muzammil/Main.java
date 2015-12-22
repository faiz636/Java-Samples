package com.muzammil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.*;
import jxl.read.biff.BiffException;

public class Main {

    public static void main(String[] args) {
        readExcel();
    }

    private static void readExcel() {
        try {
            Workbook workbook = Workbook.getWorkbook(new File("sheet.xls"));
            Sheet sheet = workbook.getSheet(0);
            SheetData sheetData= new SheetData();

            for(int i=0; i<=15; i++){
                SheetRow sheetRow = new SheetRow();
                for (int j=0; j<=7; j++){
                    System.out.print(sheet.getCell(j,i).getContents() + "   ");
                    sheetRow.setColumn(sheet.getCell(j,i).getContents());
                }
                System.out.println();
                sheetData.setRow(sheetRow);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }

//    private static void function() {
//        try {
//            File file = new File("sheet.xls");
//            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
//            HSSFWorkbook wb = new HSSFWorkbook(fs);
//            HSSFSheet sheet = wb.getSheetAt(0);
//            HSSFRow row;
//            HSSFCell cell;
//
//            int rows; // No of rows
//            rows = sheet.getPhysicalNumberOfRows();
//
//            int cols = 0; // No of columns
//            int tmp = 0;
//
//            // This trick ensures that we get the data properly even if it doesn't start from first few rows
//            for(int i = 0; i < 10 || i < rows; i++) {
//                row = sheet.getRow(i);
//                if(row != null) {
//                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
//                    if(tmp > cols) cols = tmp;
//                }
//            }
//
//            for(int r = 0; r < rows; r++) {
//                row = sheet.getRow(r);
//                if(row != null) {
//                    for(int c = 0; c < cols; c++) {
//                        cell = row.getCell((short)c);
//                        if(cell != null) {
//                            // Your code here
//                        }
//                    }
//                }
//            }
//        } catch(Exception ioe) {
//            ioe.printStackTrace();
//        }
//    }


}
