package com.rubbersoft.model;

import java.util.ArrayList;

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
}
