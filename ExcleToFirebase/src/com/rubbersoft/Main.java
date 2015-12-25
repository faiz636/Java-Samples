package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int INITIAL_DELAY = 0, PERIOD = 1;
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(5);

    public static void main(String[] args) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("running file reader task");
                fileReaderTask();
            }
        };
        scheduler.scheduleAtFixedRate(task, Main.INITIAL_DELAY, Main.PERIOD, TimeUnit.MINUTES);
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
//        System.exit(0);
    }

}
