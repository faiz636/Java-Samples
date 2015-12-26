package com.rubbersoft.android.valveleakage.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Faiz on 26/12/2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AlmaariDB";

    // Table name for each node
    private static final String TABLE_NODE1 = "node1";
    private static final String TABLE_NODE2 = "node2";
    private static final String TABLE_NODE3 = "node3";
    private static final String TABLE_NODE4 = "node4";

    // Node Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_TEMPERATURE = "temperature";
    private static final String KEY_LPG_CONCENTRATION = "LPGConcentration";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableQuery(db,SQLiteHandler.TABLE_NODE1);
        createTableQuery(db,SQLiteHandler.TABLE_NODE2);
        createTableQuery(db,SQLiteHandler.TABLE_NODE3);
        createTableQuery(db, SQLiteHandler.TABLE_NODE4);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        drop all four tables
        dropTable(db,SQLiteHandler.TABLE_NODE1);
        dropTable(db,SQLiteHandler.TABLE_NODE2);
        dropTable(db,SQLiteHandler.TABLE_NODE3);
        dropTable(db,SQLiteHandler.TABLE_NODE4);

//        Create tables again
        onCreate(db);
    }

    private void createTableQuery(SQLiteDatabase db,String tableName){
        String CREATE_TABLE_QUERY = "CREATE TABLE " + tableName + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIMESTAMP + " INTEGER NOT NULL,"
                + KEY_TEMPERATURE + " REAL NOT NULL,"
                + KEY_LPG_CONCENTRATION + " REAL  NOT NULL"
                + ")";
        db.execSQL(CREATE_TABLE_QUERY);
    }




    private void dropTable(SQLiteDatabase db, String tableName) {
//        Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public static String getTableNode1() {
        return TABLE_NODE1;
    }

    public static String getTableNode2() {
        return TABLE_NODE2;
    }

    public static String getTableNode3() {
        return TABLE_NODE3;
    }

    public static String getTableNode4() {
        return TABLE_NODE4;
    }
}
