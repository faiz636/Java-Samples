package com.rubbersoft;

import com.rubbersoft.model.SheetData;
import com.rubbersoft.model.SheetRow;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExcelToJava {

    private static Map<Integer, Integer> START_POINT = new HashMap<Integer, Integer>();//to save excel pointers
    private static final String EXCEL_POIINTER_FILE_PATH = "excel_pointers";//name of the excel pointers
    private static int i;

    /**
     * Returns the data of Excel file as an object.
     *
     * @param path Path of the file i.e sheet.xls
     * @return data present in the file
     * null if an exception occured
     */
    public static ArrayList<SheetData> readFile(String path) {
        readExcelPointers();//read the file excel pointers
        try {
            ArrayList<SheetData> sheetDataList = new ArrayList<SheetData>();//create new sheet data list

            //open excel file and get excel work book by using excel reader library
            Workbook workbook = Workbook.getWorkbook(new File(path));
            printMessage("reading excle file");

            for (int sheetNo = 0; sheetNo < 4; sheetNo++) {//loop to read excel sheet

                //open sheet form workbook
                Sheet sheet = workbook.getSheet(sheetNo);

                //initialize sheet data obj
                SheetData sheetData = new SheetData();

                //add sheet data to list of sheet data
                sheetDataList.add(sheetData);

                System.out.println("----------------------------------------------------------------------------");

                //get number of rows of sheet
                int size = sheet.getRows();
                boolean invalidData;
                printMessage("reading sheet " + sheetNo + "  and size : " + size);

                for (int i = START_POINT.get(sheetNo); i < size; i++) {//loop for iterate on rows starting
                    SheetRow sheetRow = new SheetRow();//create new sheetRow object
                    invalidData = false;//row data is not invalid right now
                    System.out.print(i);
                    for (int j = 0; j <= 7; j++) {//loop to read columns
                    /*
                    This will print each cell before adding it into the column..
                    if(j==0) printMessage();
                    System.out.print(sheet.getCell(j,i).getContents() + "   ");
                    */
                        //read content of cell
                        String s = sheet.getCell(j, i).getContents();
                        if (s.length() == 0) {//check if cell was empty
                            invalidData = true;
                            printMessage("row " + i + " is invalid");
                            break;
                        }
                        System.out.print(" -- " + s);

                        sheetRow.setColumn(s);//set cell data to row object
                    }
                    System.out.println();
                    if (invalidData) continue;//if data was invalid skip this row and continue with next row
                    sheetData.addRow(sheetRow);
                }
                START_POINT.put(sheetNo, size);//update data for excel pointers
            }
            workbook.close();//close excel sheet
            writeExcelPointers();//update excel pointers file
            i++;
            return sheetDataList;//return data obtain by excel file

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        i++;
        return null;//return nothing if any error occur during file reading
    }

    private static void readExcelPointers() {//read excel pointers file
        try {
            //open file through file reader and using wrapper buffer reader for easy file handling
            //if file not exist will give an exception
            BufferedReader reader = new BufferedReader(new FileReader(EXCEL_POIINTER_FILE_PATH));
            for (int i = 0; i < 4; i++) {//to read for each line(4 in our requirement)
                try {
                    //read line as string
                    //parse srting to integer
                    //and save it for use
                    ExcelToJava.START_POINT.put(i, Integer.parseInt(reader.readLine()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {//if file not exist
            e.printStackTrace();
            for (int i = 0; i < 4; i++) {//set initial file reading to 1(row 2 in sheet, 0 based indexing)
                ExcelToJava.START_POINT.put(i, 1);
            }
        }
        printMessage(START_POINT);
    }

    private static void writeExcelPointers() {//update excel pointer file
        printMessage("writeExcelPointers" + START_POINT);
        try {
            //open file in writable mode through file writer and using wrapper buffer writer for easy file handling
            BufferedWriter writer = new BufferedWriter(new FileWriter(ExcelToJava.EXCEL_POIINTER_FILE_PATH));
            for (int i = 0; i < 4; i++) {
                writer.write(ExcelToJava.START_POINT.get(i) + "");//write data to file
                writer.newLine();//change line
            }
            writer.close();//close file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //methods for usual formatted print operation
    private static void printMessage(Object o) {
        printMessage(o.toString());
    }

    //print message with formatting
    private static void printMessage(String message) {
        System.out.println("FileReadingTask-" + i + "--" + message);
    }

}
