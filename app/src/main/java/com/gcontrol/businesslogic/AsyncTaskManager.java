package com.gcontrol.businesslogic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gcontrol.applicationmodel.ApplicationModel;
import com.gcontrol.applicationmodel.Dispositivo;

/**
 * Created by cediaz on 05-11-2016.
 */



public class AsyncTaskManager extends Activity {

    // No somos nada si no tenemos contexto
    private Context context;
    private BusinessLogicManager blm;
    private ApplicationModel model = ApplicationModel.getInstance();
    private boolean parallel = false;

    public AsyncTaskManager(Context context) {
        super();

        this.context = context;
        this.blm = new BusinessLogicManager(context);

        this.model = ApplicationModel.getInstance();

    }

    public void  doSendDispositivos(final Dispositivo dispositivo, final ToggleButton button){



        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {


            ProgressDialog progressDialog;
            boolean process = false;


            String message = "";

            // declare other objects as per your need
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(context,
                        "Enviando Datos", "Cambiando estado a " + dispositivo.getAlerta(),
                        true);
                //   button.setEnabled(false);
            };

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);


                if (process) {
                    Toast.makeText(context, "Accción realizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se pudo realizar la acción", Toast.LENGTH_SHORT).show();
                    //vuelve el boton al estado original
                    if (button.isChecked()) {
                        button.setChecked(false);


                    } else {
                        button.setChecked(true);
                    }
                }

                progressDialog.dismiss();
            }


            // original
            @Override
            protected Void doInBackground(Void... arg0) {
                String result = blm.enviarDispositivos(dispositivo.getTerminal(), dispositivo.getEstado());

                if (result.equals("0") || result.equals("1") ){
                    process = true;
                }
                return null;
            }

        };


        task.execute();


    }




    public void doGetEstadoDispositivos(){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {


            ProgressDialog progressDialog;
            boolean sucess = false;

            String message = "";

            // declare other objects as per your need
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(context,
                        "Cargando Datos", "Obteniendo estados de los dispositivos",
                        true);
            };

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);
                progressDialog.dismiss();

            }

            // original
            @Override
            protected Void doInBackground(Void... arg0) {

                blm = new BusinessLogicManager(context);
                blm.getEstadosDispositivos();

                return null;
            }

        };

        task.execute();

    }


    public void doEnviarMensajes(final String mensaje, final String tipo, final EditText edtMensaje, final Spinner spinViewTipo){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {


            ProgressDialog progressDialog;
            boolean sucess = false;

            String message = "";

            // declare other objects as per your need
            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(context,
                        "Enviando Mensaje", "Conectando con el servidor...",
                        true);
            };

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);

                if(sucess){
                    edtMensaje.setText("");
                    spinViewTipo.setSelection(0);
                    Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            // original
            @Override
            protected Void doInBackground(Void... arg0) {

                blm = new BusinessLogicManager(context);
                sucess = blm.enviarMensaje(mensaje, tipo);

                return null;
            }

        };

        task.execute();

    }



}
