package com.muzammil;

/**
 * Created by Muhammad Muzammil on 22-Dec-15.
 */
public class DemoClass {
    public static void main(String[] args){
        SheetData sheetData = ExcelToJava.readFile("sheet.xls");
        for(int i=0; i<sheetData.getRow(0).size(); i++){
            System.out.print(sheetData.getRow(0).getColumn(i) + "   ");
            //This will print the titles of each column..
        }
    }
}
