package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.ArrayList;

public class Main {

    private static final int INITIAL_DELAY = 5, PERIOD = 20;

    public static void main(String[] args) {
        fileReaderTask();
    }

    //    this will read the file periodically and print its size
    public static void fileReaderTask() {
        long t;
        t = System.currentTimeMillis();
        ArrayList<SheetData> sheetDataList = ExcelToJava.readFile("sheet.xls");
        System.out.println("file reading total time taken " + (System.currentTimeMillis() - t) + "ms, " + ((System.currentTimeMillis() - t) / (1000)) + "sec");
        t = System.currentTimeMillis();
        FirebaseDataSender ft = new FirebaseDataSender(sheetDataList);
        System.out.println("sending data total time taken " + (System.currentTimeMillis() - t) + "ms, " + ((System.currentTimeMillis() - t) / (1000)) + "sec");
        System.exit(0);
    }

}
