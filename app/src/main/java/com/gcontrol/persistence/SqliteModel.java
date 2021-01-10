package com.gcontrol.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by cediaz on 09-11-2016.
 */

public class SqliteModel {

    Context context;
    private final String DATABASE = "GestControl";
    private SQLiteDatabase db;
    private int ref = 0;

    public SqliteModel(Context context) {
        this.context = context;

    }

    public synchronized SQLiteDatabase openDatabase() {
        try {
            ref++;
            if (db == null) {
                final Sqlite usdbh = new Sqlite(context, DATABASE, null, 1);
                db = usdbh.getWritableDatabase();
            }
        } catch (Exception ex) {
            Log.e("openDatabase", ex.getMessage(), ex);
        }
        return db;
    }

    public synchronized void closeDatabase() {
        ref--;
        if (ref == 0) {
            if (db != null) {
                db.close();
                db = null;
            }
        }
   }

    public void execSQL(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    public void createTableUsersIfNotExists() {
        try {
            db = openDatabase();
            // sin office

            execSQL(db, "CREATE TABLE IF NOT EXISTS usuario("
                    + "id text integer, " + " userid primary key, "+  "password text)");


        } catch (Exception ex) {
            Log.e("createTableUserIfNotExi", ex.getMessage(), ex);
        } finally {
            try {
                closeDatabase();
            } catch (Exception ex) {
                Log.e("createTableUserIfNotExi", ex.getMessage(), ex);
            }
        }
    }


    public void dropTableUsers() {
        try {
            db = openDatabase();

            execSQL(db, "DROP TABLE usuario");

        } catch (Exception ex) {
            Log.e("dropTableUsuario", ex.getMessage(), ex);
        } finally {
            try {
                closeDatabase();

            } catch (Exception ex) {
                Log.e("dropTableUsuario", ex.getMessage(), ex);
            }
        }
    }

    public void createDatabase() {
        try {
            db = openDatabase();

            createTableUsersIfNotExists();


        } catch (Exception ex) {
            Log.e("createDatabase", ex.getMessage(), ex);
        } finally {
            try {
                closeDatabase();
            } catch (Exception ex) {
                Log.e("createDatabase", ex.getMessage(), ex);
            }
        }
    }

    public void dropDatabase() {
        try {
            db = openDatabase();

            dropTableUsers();


        } catch (Exception ex) {
            Log.e("dropDatabase", ex.getMessage(), ex);
        } finally {
            try {
                closeDatabase();
            } catch (Exception ex) {
                Log.e("dropDatabase", ex.getMessage(), ex);
            }
        }
    }

}



