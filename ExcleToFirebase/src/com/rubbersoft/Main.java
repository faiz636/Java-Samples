package com.rubbersoft;

import com.rubbersoft.model.SheetData;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static int INITIAL_DELAY = 0;//delay before first run of scheduled task
    private static int PERIOD = 1; //time to rerun task after this time
    private static final int INITIAL_DELAY_DUMMY = 0;
    private static final int PERIOD_DUMMY = 10;
    private static TimeUnit TIME_UNIT = TimeUnit.MINUTES;//unit of time can be seconds also
    private static TimeUnit TIME_UNIT_DUMMY = TimeUnit.SECONDS;
    //get scheduler service with 10 threads in pool. 10 threads to keep on waiting even if not running
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    public static long DUMMY_TIME_ADVANCE;
    private static String FILE_NAME = "sheet.xls";//name of default excel file to read from


    public static void main(String[] args) {
        if (args.length>0 && args[0].compareTo("dummy")==0){//checking arguments for dummy
            System.out.println("will run dummy task");
            if (args.length>1 && args[1].compareTo("old")==0){
                int days = 1;
                if (args.length>2){
                    try {
                        days = Integer.parseInt(args[2]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                DUMMY_TIME_ADVANCE = 1000*60*60*24*days;
            }
            scheduleDummyTask();
        }else {
            if (args.length > 0) {//check in program is called with any arguments

                Main.FILE_NAME = args[0];//get file name from arguments

                if (args.length > 1) {//check if any further arguments are passed
                    if (args.length > 0 && args[0].compareTo("min") == 0) {//if time argument is passed for minute
                        TIME_UNIT = TimeUnit.MINUTES;
                    } else if (args.length > 0 && args[0].compareTo("sec") == 0) {//if time argument is passed for minute
                        TIME_UNIT = TimeUnit.SECONDS;
                    }
                    try {//necessary initial delay and period were passed with above argument
                        INITIAL_DELAY = Integer.parseInt(args[1]);
                        PERIOD = Integer.parseInt(args[2]);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("invalid Arguments");
                        e.printStackTrace();
                    }
                }
            }
            scheduleTask();//schedule task
        }
        exitProcedure();//wait for exit to be called
    }

    //    schedule task for running periodically
    private static void scheduleTask() {
        Runnable task = new Runnable() {//making anonymous object by implementing interface for thread
            @Override
            public void run() {//this method will be called when thread starts running
                System.out.println("running file reader task");
                fileReaderTask();//execute our file reader and data sender task
            }
        };
        //schedule our newly created task whit initial delay, period and time unit
        scheduler.scheduleAtFixedRate(task, Main.INITIAL_DELAY, Main.PERIOD, TIME_UNIT);
    }

    //    schedule task for running periodically
    private static void scheduleDummyTask() {
        scheduler.scheduleAtFixedRate(new FirebaseDummyDataSender(), Main.INITIAL_DELAY_DUMMY, Main.PERIOD_DUMMY, TIME_UNIT_DUMMY);
    }

    //    take exit command and start exit procedure
    private static void exitProcedure() {
        Scanner in = new Scanner(System.in);//input stream to take input
        String s;

        do {
            System.out.println("enter string  :");
        } while (!((s = in.nextLine()).contentEquals("exit")));//take input while it is not exit

        //if exit is called, we'll reach here

        System.out.println("shut down sequence started.");
        scheduler.shutdown();//shutdown scheduled task

        while (!scheduler.isTerminated()) {//wait for schedular to terminate
            try {
                System.out.println("please wait for pending execution to complete...");
                Thread.sleep(5000);//wait for another 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("exiting..");
        System.exit(0);//quiting program
    }


    //    read the file and send it to firebase
    public static void fileReaderTask() {
        //read excel sheet and return sheet data list
        ArrayList<SheetData> sheetDataList = ExcelToJava.readFile(Main.FILE_NAME);

        //send sheet data list to firebase
        new FirebaseDataSender(sheetDataList);
    }

}
