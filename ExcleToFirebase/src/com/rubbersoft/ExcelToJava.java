package com.rubbersoft;

import com.rubbersoft.model.SheetData;
import com.rubbersoft.model.SheetRow;
import jxl.*;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

public class ExcelToJava {

    /**
     * Returns the data of Excel file as an object.
     *
     * @param path Path of the file i.e sheet.xls
     * @return the data present in the file
     */

    public static SheetData readFile(String path) {
        try {
            Workbook workbook = Workbook.getWorkbook(new File(path));
            Sheet sheet = workbook.getSheet(0);
            SheetData sheetData= new SheetData();

            for(int i=0; i<=15; i++){
                SheetRow sheetRow = new SheetRow();
                for (int j=0; j<=7; j++){
                    /*
                    This will print each cell before adding it into the column..
                    if(j==0) System.out.println();
                    System.out.print(sheet.getCell(j,i).getContents() + "   ");
                    */
                    sheetRow.setColumn(sheet.getCell(j,i).getContents());
                }
                sheetData.addRow(sheetRow);
            }
            return sheetData;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        return null;
    }
}
