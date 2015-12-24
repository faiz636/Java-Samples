package com.rubbersoft;

import com.rubbersoft.model.SheetData;
import com.rubbersoft.model.SheetRow;
import jxl.*;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

public class ExcelToJava {

    private static int START_POINT = 1;

    /**
     * Returns the data of Excel file as an object.
     *
     * @param path Path of the file i.e sheet.xls
     * @return data present in the file
     *         null if an exception occured
     */
    public static SheetData readFile(String path) {
        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            SheetData sheetData= new SheetData();

            int size = workbook.getSheet(0).getRows();
            boolean invalidData;
            System.out.println("reading excle file");

            for(int i=START_POINT; i<size; i++){
                SheetRow sheetRow = new SheetRow();
                invalidData = false;
                for (int j=0; j<=7; j++){
                    /*
                    This will print each cell before adding it into the column..
                    if(j==0) System.out.println();
                    System.out.print(sheet.getCell(j,i).getContents() + "   ");
                    */
                    String s = sheet.getCell(j,i).getContents();
                    if (s.length()==0) {
                        invalidData = true;
                        System.out.println("row "+ i + " is invalid");
                        break;
                    }

                    sheetRow.setColumn(s);
                }
                if (invalidData) continue;
                sheetData.addRow(sheetRow);
            }
            START_POINT = size;
            workbook.close();
            return sheetData;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        return null;
    }
}
