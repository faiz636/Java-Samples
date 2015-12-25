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
//        long t;
//        t = System.currentTimeMillis();
        ArrayList<SheetData> sheetDataList = ExcelToJava.readFile("sheet.xls");
//        System.out.println("total time taken "+ (System.currentTimeMillis()-t) +"ms, " + ( (System.currentTimeMillis()-t)/(1000))+"sec");
//        t = System.currentTimeMillis();
        FirebaseThread ft = new FirebaseThread(sheetDataList);
        try {
            ft.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("total time taken "+ (System.currentTimeMillis()-t) +"ms, " + ( (System.currentTimeMillis()-t)/(1000))+"sec");
        System.exit(0);
    }

}
