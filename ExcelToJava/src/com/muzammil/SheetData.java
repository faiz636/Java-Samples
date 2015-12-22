package com.muzammil;

import java.util.ArrayList;

/**
 * Created by Muhammad Muzammil on 22-Dec-15.
 */
public class SheetData {

    ArrayList<SheetRow> row;
    SheetData(){
        row = new ArrayList<SheetRow>();
    }



    /**
     * Returns all rows.
     *
     * @return all data in the sheet
     */
    public ArrayList<SheetRow> getRow() {
        return row;
    }

    /**
     * Returns the specified row in the sheet.
     *
     * @param i return the ith row
     * @return the ith row of the sheet
     */
    public SheetRow getRow(int i) {
        return row.get(i);
    }


    /**
     * Sets the whole row.
     *
     * @param row rows to be added
     */
    public void setRow(ArrayList<SheetRow> row) {
        this.row = row;
    }


    /**
     * Sets the row at the end of sheet
     *
     * @param row  row to be add the end of sheet
     */
    public void setRow(SheetRow row) {
        this.row.add(row);
    }



    /**
     * Sets the row at the specified index on the sheet
     *
     * @param row  row to be add the at the specified index
     * @param index specified index
     */
    public void setRow(int index, SheetRow row) {
        this.row.add(index, row);
    }



    /**
     * Returns the number of rows in the sheet
     *
     * @return the size of sheet
     */
    public int size() {
        return this.row.size();
    }

}
