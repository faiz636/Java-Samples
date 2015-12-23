package com.rubbersoft;

import com.rubbersoft.model.SheetData;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SheetData sheetData = ExcelToJava.readFile("sheet.xls");
        System.out.println(sheetData.size());

//        printing whole sheet
        for(int i=0; i<sheetData.size(); i++){
            for(int j=0; j<sheetData.getRow(i).size(); j++){
                System.out.print(sheetData.getRow(i).getColumn(j) + "   ");
            }
            System.out.println();
        }
    }
}
