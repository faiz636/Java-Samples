package com.rubbersoft.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Muhammad Muzammil on 22-Dec-15.
 */
public class SheetRow {

    private ArrayList<String> column;

    public SheetRow(){
        column =  new ArrayList<String>();
    }

    /**
     * Returns the whole rows.
     *
     * @return all the columns in the rows
     */
    public ArrayList<String> getColumn() {
        return column;
    }

    /**
     * Returns the specified column in the rows.
     * @param i return the ith column
     * @return the ith column of the rows
     */
    public String getColumn(int i) {
        return column.get(i);
    }


    /**
     * Sets the whole rows.
     *
     * @param column  whole rows to be set
     */
    public void setColumn(ArrayList<String> column) {
        this.column = column;
    }


    /**
     * Sets the string at the end of rows
     *
     * @param string  column to be add the at the end of rows
     */
    public void setColumn(String string) {
        this.column.add(string);
    }



    /**
     * Sets the string at the specified index of the rows
     *
     * @param string  column to be add the at the specified index
     * @param index specified index
     */
    public void setColumn(int index, String string) {
        this.column.add(index, string);
    }



    /**
     * Returns the length of the rows
     *
     * @return the numbers of columns in the rows
     */
    public int size() {
        return this.column.size();
    }

    @Override
    public String toString() {
        String x = "";
        for (String s : column){
            x+=" -- "+s;
        }
        return x;
    }

    /**
     * This class will be used to send data to Firebase
     * It will be passed SheetRow object from which
     * it will store as required for extract the data
     * in the required format
    * */
    public static class FirebaseData {
        public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss a");
        private long timestamp;
        private float temperature,LPGConcentration;

        FirebaseData(){}

        public FirebaseData(SheetRow row){
            int year = Integer.parseInt(row.getColumn(0)),//year
                    month = Integer.parseInt(row.getColumn(1)),//month
                    day = Integer.parseInt(row.getColumn(2)),//date
                    hour = Integer.parseInt(row.getColumn(3)),//hour
                    min = Integer.parseInt(row.getColumn(4)),//minute
                    sec = (int)Float.parseFloat(row.getColumn(5));//second

            timestamp = new GregorianCalendar(
                    year,month-1,day,hour,min,sec
            ).getTimeInMillis();

            temperature = Float.parseFloat(row.getColumn(6));//temperature
            LPGConcentration = Float.parseFloat(row.getColumn(7));//LPGConcentration
        }
        public FirebaseData(long timestamp, long temperature, long LPGConcentration) {
            this(timestamp,(float)temperature,(float)LPGConcentration);
        }

        public FirebaseData(long timestamp, float temperature, float LPGConcentration) {
            this.timestamp = timestamp;
            this.temperature = temperature;
            this.LPGConcentration = LPGConcentration;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public float getLPGConcentration() {
            return LPGConcentration;
        }

        public void setLPGConcentration(float LPGConcentration) {
            this.LPGConcentration = LPGConcentration;
        }

        @Override
        public String toString() {
            return dateFormat.format(timestamp)+" -- "+ temperature +" -- "+ LPGConcentration;
        }

    }
}
