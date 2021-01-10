package com.gcontrol.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cediaz on 09-11-2016.
 */

public class SqliteQuery {

    private Context context;
    private SQLiteDatabase db;

    public SqliteQuery(Context context) {
        this.context = context;

    }
    public String getUserInDataBase() {

        String userId = null;
        SqliteModel sqlModel = new SqliteModel(context);
        Cursor c = null;
        //List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        try {

            sqlModel.createTableUsersIfNotExists();
            db = sqlModel.openDatabase();
            c = db.rawQuery(
                    "SELECT userid FROM usuario;", null);

            // entra solo si existe el registro
            if (c!=null){
                if (c.moveToFirst()) {
                    do {
                        //Map<String, String> row = new HashMap<String, String>();
                        //row.put("userid", c.getString(0));
                        // result.add(row);
                        userId = c.getString(0);



                    } while (c.moveToNext());
                }
            }


        } catch (Exception ex) {
            Log.e("getUserInDataBase", ex.getMessage(), ex);
        } finally {
            try {
                c.close();
                sqlModel.closeDatabase();
            } catch (Exception ex) {
                Log.e("getUserInDataBase", ex.getMessage(), ex);
            }
        }
        return userId;
    }

    public boolean existUserInDataBase(String userId){

        boolean exist = false;
        SqliteModel sqlModel = new SqliteModel(context);
        Cursor c = null;

        try {
            sqlModel.createTableUsersIfNotExists();
            c = db.rawQuery(
                    "SELECT userid FROM usuario WHERE userid = '" + userId + "'", null);

            // entra solo si existe el registro
            if (c!=null){
                if (c.moveToFirst()) {
                  String user =c.getString(0);

                       exist = true;
                }
            }


        } catch (Exception ex) {
            Log.e("existUserInDataBase", ex.getMessage(), ex);
        } finally {
            try {
                c.close();
                sqlModel.closeDatabase();
            } catch (Exception ex) {
                Log.e("existUserInDataBase", ex.getMessage(), ex);
            }
        }
        return exist;



    }




    public void insertUserInDataBase(String userId){

        SqliteModel sqlModel = new SqliteModel(context);
        sqlModel.createTableUsersIfNotExists();
        try {
            db = sqlModel.openDatabase();
            boolean exist = existUserInDataBase(userId);
            if(!exist){
                sqlModel.execSQL(db,
                        "insert into usuario(userid) values('"
                                + userId
                                + "')");
            }


        } catch (Exception e) {
            Log.e("insertUserInDatabase", e.getMessage(), e);
        } finally {
            try {
                sqlModel.closeDatabase();
            } catch (Exception ex) {
                Log.e("insertUserDataBase", ex.getMessage(), ex);
            }
        }



    }

}
