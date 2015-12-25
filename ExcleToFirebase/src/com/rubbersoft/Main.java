package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int INITIAL_DELAY = 5, PERIOD = 20;

    public static void main(String[] args) {
        fileReaderTask();
    }

    //    this will read the file periodically and print its size
    public static void fileReaderTask() {
        ArrayList<SheetData> sheetDataList = ExcelToJava.readFile("sheet.xls");
        if (sheetDataList == null) return;
        FirebaseThread ft = new FirebaseThread(sheetDataList);
        try {
            ft.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
