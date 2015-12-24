package com.rubbersoft;

import com.rubbersoft.model.SheetData;
import com.rubbersoft.model.SheetRow;
import jxl.*;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelToJava {

    private static int START_POINT = 1;

    /**
     * Returns the data of Excel file as an object.
     *
     * @param path Path of the file i.e sheet.xls
     * @return data present in the file
     *         null if an exception occured
     */
    public static ArrayList<SheetData> readFile(String path) {
        try {
            ArrayList<SheetData> sheetDataList = new ArrayList<SheetData>();
            Workbook workbook = Workbook.getWorkbook(new File(path));
            System.out.println("reading excle file");

            for (int sheetNo = 0;sheetNo<4;sheetNo++) {

                Sheet sheet = workbook.getSheet(sheetNo);
                SheetData sheetData = new SheetData();
                sheetDataList.add(sheetData);

                int size = sheet.getRows();
                boolean invalidData;
                System.out.println("reading sheet " + sheetNo);

                for (int i = START_POINT; i < size; i++) {
                    SheetRow sheetRow = new SheetRow();
                    invalidData = false;
                    for (int j = 0; j <= 7; j++) {
                    /*
                    This will print each cell before adding it into the column..
                    if(j==0) System.out.println();
                    System.out.print(sheet.getCell(j,i).getContents() + "   ");
                    */
                        String s = sheet.getCell(j, i).getContents();
                        if (s.length() == 0) {
                            invalidData = true;
                            System.out.println("row " + i + " is invalid");
                            break;
                        }

                        sheetRow.setColumn(s);
                    }
                    if (invalidData) continue;
                    sheetData.addRow(sheetRow);
                }
//                START_POINT = size;
            }
            workbook.close();
            return sheetDataList;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        return null;
    }
}
