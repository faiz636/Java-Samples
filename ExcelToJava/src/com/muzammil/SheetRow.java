package com.muzammil;

import java.util.ArrayList;

/**
 * Created by Muhammad Muzammil on 22-Dec-15.
 */
public class SheetRow {

    private ArrayList<String> column;

    SheetRow(){
        column =  new ArrayList<String>();
    }

    /**
     * Returns the whole row.
     *
     * @return all the columns in the row
     */
    public ArrayList<String> getColumn() {
        return column;
    }

    /**
     * Returns the specified column in the row.
     * @param i return the ith column
     * @return the ith column of the row
     */
    public String getColumn(int i) {
        return column.get(i);
    }


    /**
     * Sets the whole row.
     *
     * @param column  whole row to be set
     */
    public void setColumn(ArrayList<String> column) {
        this.column = column;
    }


    /**
     * Sets the string at the end of row
     *
     * @param string  column to be add the at the end of row
     */
    public void setColumn(String string) {
        this.column.add(string);
    }



    /**
     * Sets the string at the specified index of the row
     *
     * @param string  column to be add the at the specified index
     * @param index specified index
     */
    public void setColumn(int index, String string) {
        this.column.add(index, string);
    }



    /**
     * Returns the length of the row
     *
     * @return the numbers of columns in the row
     */
    public int size() {
        return this.column.size();
    }
}
