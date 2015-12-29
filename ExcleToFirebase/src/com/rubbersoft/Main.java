package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static int INITIAL_DELAY = 0, PERIOD = 1;
    private static final int INITIAL_DELAY_DUMMY = 0, PERIOD_DUMMY = 10;
    private static TimeUnit TIME_UNIT = TimeUnit.MINUTES, TIME_UNIT_DUMMY = TimeUnit.SECONDS;
    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(10);

    public static void main(String[] args) {
        if (args.length>0 && args[0].compareTo("dummy")==0){
            System.out.println("will run dummy task");
            scheduleDummyTask();
        }else {
            if (args.length>0) {
                if (args.length > 0 && args[0].compareTo("min") == 0) {
                    TIME_UNIT = TimeUnit.MINUTES;
                } else if (args.length > 0 && args[0].compareTo("sec") == 0) {
                    TIME_UNIT = TimeUnit.SECONDS;
                }
                try {
                    INITIAL_DELAY = Integer.parseInt(args[1]);
                    PERIOD = Integer.parseInt(args[2]);
                }catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("invalid Arguments");
                    e.printStackTrace();
                }

            }
            scheduleTask();
        }
        exitProcedure();
    }

    //    schedule task for running periodically
    private static void scheduleTask() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("running file reader task");
                fileReaderTask();
            }
        };
        scheduler.scheduleAtFixedRate(task, Main.INITIAL_DELAY, Main.PERIOD, TIME_UNIT);
    }

    //    schedule task for running periodically
    private static void scheduleDummyTask() {
        scheduler.scheduleAtFixedRate(new FirebaseDummyDataSender(), Main.INITIAL_DELAY_DUMMY, Main.PERIOD_DUMMY, TIME_UNIT_DUMMY);
    }

    //    take exit command and start exit procedure
    private static void exitProcedure() {
        Scanner in = new Scanner(System.in);
        String s;

        do {
            System.out.print("enter string  :");
        } while (!((s = in.nextLine()).contentEquals("exit")));

        System.out.println("shut down sequence started.");
        scheduler.shutdown();

        while (!scheduler.isTerminated()) {
            try {
                System.out.println("please wait for pending execution to complete...");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("exiting..");
        System.exit(0);
    }


    //    read the file and send it to firebase
    public static void fileReaderTask() {
//        long t;
//        t = System.currentTimeMillis();
        ArrayList<SheetData> sheetDataList = ExcelToJava.readFile("sheet.xls");
//        System.out.println("file reading total time taken " + (System.currentTimeMillis() - t) + "ms, " + ((System.currentTimeMillis() - t) / (1000)) + "sec");
//        t = System.currentTimeMillis();
        new FirebaseDataSender(sheetDataList);
//        System.out.println("sending data total time taken " + (System.currentTimeMillis() - t) + "ms, " + ((System.currentTimeMillis() - t) / (1000)) + "sec");
    }

}
