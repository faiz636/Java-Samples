package com.rubbersoft.model;

import java.util.ArrayList;

/**
 * Created by Muhammad Muzammil on 22-Dec-15.
 */
public class SheetData {

    ArrayList<SheetRow> rows;
    public SheetData(){
        rows = new ArrayList<SheetRow>();
    }

    /**
     * Returns all rows.
     *
     * @return all data in the sheet
     */
    public ArrayList<SheetRow> getAllRows() {
        return rows;
    }

    /**
     * Returns the specified rows in the sheet.
     *
     * @param i return the ith row
     * @return the ith row of the sheet
     */
    public SheetRow getRow(int i) {
        return rows.get(i);
    }


    /**
     * Sets the whole rows.
     *
     * @param row rows to be added
     */
    public void setRows(ArrayList<SheetRow> row) {
        this.rows = row;
    }


    /**
     * Add a rows at the end of sheet
     *
     * @param row  rows to be add the end of sheet
     */
    public void addRow(SheetRow row) {
        this.rows.add(row);
    }



    /**
     * Add the row at the specified index on the sheet
     *
     * @param row  row to be add the at the specified index
     * @param index specified index
     */
    public void addRow(int index, SheetRow row) {
        this.rows.add(index, row);
    }



    /**
     * Returns the number of rows in the sheet
     *
     * @return the size of sheet
     */
    public int size() {
        return this.rows.size();
    }

}
