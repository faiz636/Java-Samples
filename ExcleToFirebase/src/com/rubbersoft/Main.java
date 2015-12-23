package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final int INITIAL_DELAY = 5,PERIOD = 5;

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void main(String[] args) {

/*
        SheetData sheetData = ExcelToJava.readFile("sheet.xls");
        System.out.println(sheetData.size());

//        printing whole sheet
        for(int i=0; i<sheetData.size(); i++){
            for(int j=0; j<sheetData.getRow(i).size(); j++){
                System.out.print(sheetData.getRow(i).getColumn(j) + "   ");
            }
            System.out.println();
        }
*/

        fileReaderTask();
    }

//    this will read the file periodically and print its size
    public static void fileReaderTask(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                SheetData sheetData = ExcelToJava.readFile("sheet.xls");
                System.out.println("task running : "+sheetData.size());

                //shutting down the scheduler is necessary else it program will not terminate.
//                scheduler.shutdown();
            }
        };

//        scheduler.schedule(task,100, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(task,INITIAL_DELAY,PERIOD, TimeUnit.SECONDS);
    }

}
