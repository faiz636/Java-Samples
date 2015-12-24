package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int INITIAL_DELAY = 5, PERIOD = 20;

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {
        fileReaderTask();
    }

    //    this will read the file periodically and print its size
    public static void fileReaderTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                SheetData sheetData = ExcelToJava.readFile("sheet.xls");
                if (sheetData == null) return;
                System.out.println("rows added in excle file : " + sheetData.size());
                if (sheetData.size() > 0) {
                    new FirebaseThread(sheetData);
                }
                //shutting down the scheduler is necessary else it program will not terminate.
//                scheduler.shutdown();
            }
        };

//        scheduler.schedule(task,100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(task, INITIAL_DELAY, PERIOD, TimeUnit.SECONDS);
    }

}
