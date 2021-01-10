package com.gcontrol.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cediaz on 09-11-2016.
 */



    public class Sqlite extends SQLiteOpenHelper {

        public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {

            // Se elimina la version anterior de la tabla

            // Se crea la nueva version de la tabla

        }

    }

