package com.rubbersoft;

import com.rubbersoft.model.SheetData;
import com.rubbersoft.model.SheetRow;
import jxl.*;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExcelToJava {

    private static Map<Integer,Integer> START_POINT = new HashMap<Integer, Integer>();
    private static final String EXCEL_POIINTER_FILE_PATH = "excel_pointers";
    private static int i;

    /**
     * Returns the data of Excel file as an object.
     *
     * @param path Path of the file i.e sheet.xls
     * @return data present in the file
     *         null if an exception occured
     */
    public static ArrayList<SheetData> readFile(String path) {
        readExcelPointers();
        try {
            ArrayList<SheetData> sheetDataList = new ArrayList<SheetData>();
            Workbook workbook = Workbook.getWorkbook(new File(path));
            printMessage("reading excle file");

            for (int sheetNo = 0;sheetNo<4;sheetNo++) {

                Sheet sheet = workbook.getSheet(sheetNo);
                SheetData sheetData = new SheetData();
                sheetDataList.add(sheetData);

                System.out.println("----------------------------------------------------------------------------");

                int size = sheet.getRows();
                boolean invalidData;
                printMessage("reading sheet " + sheetNo + "  and size : " + size);

                for (int i = START_POINT.get(sheetNo); i < size; i++) {
                    SheetRow sheetRow = new SheetRow();
                    invalidData = false;
                    System.out.print(i);
                    for (int j = 0; j <= 7; j++) {
                    /*
                    This will print each cell before adding it into the column..
                    if(j==0) printMessage();
                    System.out.print(sheet.getCell(j,i).getContents() + "   ");
                    */
                        String s = sheet.getCell(j, i).getContents();
                        if (s.length() == 0) {
                            invalidData = true;
                            printMessage("row " + i + " is invalid");
                            break;
                        }
                        System.out.print(" -- "+s);

                        sheetRow.setColumn(s);
                    }
                    System.out.println();
                    if (invalidData) continue;
                    sheetData.addRow(sheetRow);
                }
                START_POINT.put(sheetNo,size);
            }
            workbook.close();
//            writeExcelPointers();
            i++;
            return sheetDataList;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        i++;
        return null;
    }

    private static void readExcelPointers(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(EXCEL_POIINTER_FILE_PATH));
            for(int i=0;i<4;i++){
                try {
                    ExcelToJava.START_POINT.put(i,Integer.parseInt(reader.readLine()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            for(int i=0;i<4;i++){
                    ExcelToJava.START_POINT.put(i,1);
            }
        }
        printMessage(START_POINT);
    }

    private static void writeExcelPointers(){
        printMessage("writeExcelPointers" + START_POINT);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ExcelToJava.EXCEL_POIINTER_FILE_PATH));
            for (int i=0;i<4;i++){
                writer.write(ExcelToJava.START_POINT.get(i)+"");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMessage(Object o){
        printMessage(o.toString());
    }

    private static void printMessage(String message){
        System.out.println("FileReadingTask-" + i + "--" + message);
    }

}
