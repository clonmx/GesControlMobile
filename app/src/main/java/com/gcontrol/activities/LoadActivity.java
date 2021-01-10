package com.gcontrol.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import android.widget.EditText;
import android.widget.Toast;


import com.gcontrol.businesslogic.BusinessLogicManager;
import com.gcontrol.utils.Utils;

import net.simplifiedcoding.androidgcm.R;

public class LoadActivity extends AppCompatActivity {



    private Context context;
    BusinessLogicManager blm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        blm = new BusinessLogicManager(LoadActivity.this);

        Utils util = new Utils(this);
        ActionBar actionBar = getSupportActionBar();
        util.customActionBar(actionBar);

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            boolean success;
            String message = "";
            ProgressDialog progressDialog;


            @Override
            protected Void doInBackground(Void... params) {
                try{

                    blm.sincronizar();

                    success = true;
                }catch(Exception ex){
                    success = false;
                }
                return null;
            }

            // declare other objects as per your need
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(LoadActivity.this,
                        "Cargando Datos",
                        "Actualizando datos de los dispositivos.", true);
                // do initialization of required objects objects here
            };


            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);
                progressDialog.dismiss();

                if (success) {

                    message = "Datos cargados correctamente";
                    Toast.makeText(LoadActivity.this, message, Toast.LENGTH_LONG)
                            .show();

                    final Intent intent = new Intent(LoadActivity.this,
                            ConfiguracionActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    message = "Error al cargar los datos.";
                    Toast.makeText(LoadActivity.this, message, Toast.LENGTH_LONG)
                            .show();
                    final Intent intent = new Intent(LoadActivity.this,
                            LogActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        };
        task.execute();





    }






}
