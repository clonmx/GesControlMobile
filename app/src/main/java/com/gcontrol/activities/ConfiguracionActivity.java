package com.gcontrol.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gcontrol.applicationmodel.ApplicationModel;
import com.gcontrol.applicationmodel.Dispositivo;
import com.gcontrol.applicationmodel.Mensaje;
import com.gcontrol.businesslogic.AsyncTaskManager;
import com.gcontrol.businesslogic.BusinessLogicManager;
import com.gcontrol.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import net.simplifiedcoding.androidgcm.GCMRegistrationIntentService;
import net.simplifiedcoding.androidgcm.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguracionActivity extends AppCompatActivity {

    //Creating a broadcast receiver for gcm registration
    private String token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView disp1, disp2, disp3, disp4, disp5, disp6, disp7, disp8;
    private ToggleButton device1, device2, device3, device4, device5, device6, device7, device8;
    private BusinessLogicManager blm = new BusinessLogicManager(this);
    private ApplicationModel model = ApplicationModel.getInstance();
    private EditText edtMensaje;
    private Spinner spinTipo;
    private Button btnEnviar;
    private ListView listViewMensajes;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_configuracion);

        device1 = (ToggleButton) findViewById(R.id.tbtnDispositivo1);
        device2 = (ToggleButton) findViewById(R.id.tbtnDispositivo2);
        device3 = (ToggleButton) findViewById(R.id.tbtnDispositivo3);
        device4 = (ToggleButton) findViewById(R.id.tbtnDispositivo4);
        device5 = (ToggleButton) findViewById(R.id.tbtnDispositivo5);
        device6 = (ToggleButton) findViewById(R.id.tbtnDispositivo6);
        device7 = (ToggleButton) findViewById(R.id.tbtnDispositivo7);
        device8 = (ToggleButton) findViewById(R.id.tbtnDispositivo8);

        disp1 = (TextView) findViewById(R.id.txtDispositivo1);
        disp2 = (TextView) findViewById(R.id.txtDispositivo2);
        disp3 = (TextView) findViewById(R.id.txtDispositivo3);
        disp4 = (TextView) findViewById(R.id.txtDispositivo4);
        disp5 = (TextView) findViewById(R.id.txtDispositivo5);
        disp6 = (TextView) findViewById(R.id.txtDispositivo6);
        disp7 = (TextView) findViewById(R.id.txtDispositivo7);
        disp8 = (TextView) findViewById(R.id.txtDispositivo8);
            edtMensaje = (EditText) findViewById(R.id.edtMensaje);
            spinTipo = (Spinner) findViewById(R.id.spinTipo);

        Utils util = new Utils(this);
        ActionBar actionBar = getSupportActionBar();
        util.customActionBar(actionBar);

            initTab();
            toggleButtonManagaer();

            initGCM();
            actualizarToggleButton();
            populateMensaje();


        btnEnviar = (Button) findViewById(R.id.tbnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String tipo = spinTipo.getSelectedItem().toString();
                String mensaje = edtMensaje.getText().toString();

                if(!tipo.trim().equals("") && spinTipo.getSelectedItemPosition() > 0 ){
                    if(!mensaje.trim().equals("")){
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doEnviarMensajes(mensaje, tipo, edtMensaje, spinTipo);
                    }else{
                        //Enviar mensaje de alerta

                    }
                }else{
                    //Enviar mensaje de alerta

                }
                //Enviar mensaje
            }
        });

        listViewMensajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> item = (HashMap<String, String>) listViewMensajes.getItemAtPosition(position);
                String mensaje = (String) item.get("mensaje");
                Utils util =  new Utils(ConfiguracionActivity.this);
                util.displayPopUp(mensaje);
            }
        });
        }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        actualizarToggleButton();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    private void initGCM(){

//Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully

                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    token = intent.getStringExtra("token");
                    intent.getStringExtra("notificationID");

                    ApplicationModel model = ApplicationModel.getInstance();
                    model.setToken(token);
                    enviarToken();

                    //Displaying the token as toast
                   //Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

    }


    private void toggleButtonManagaer(){


        final List<Dispositivo>  dispositivoList = model.getDispositivos();
        device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device1.isChecked()) {

                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(0);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo , device1);
                    }
                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(0);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device1);
                    }
                }
            }
        });

        device2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device2.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(1);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device2);
                    }

                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(1);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device2);
                    }
                }
            }
        });

        device3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device3.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(2);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device3);
                    }

                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(2);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device3);
                    }
                }
            }
        });

        device4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device4.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(3);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device4);
                    }

                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(3);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device4);
                    }
                }
            }
        });

        device5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device5.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(4);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device5);
                    }

                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(4);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device5);
                    }
                }
            }
        });

        device6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device6.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(5);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device6);
                    }

                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(5);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device6);
                    }
                }
            }
        });

        device7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device7.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(6);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device7);
                    }
                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(6);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device7);
                    }
                }
            }
        });

        device8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (device8.isChecked()) {
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(7);
                        dispositivo.setEstado("1");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device8);
                    }

                }
                else{
                    if(dispositivoList != null && dispositivoList.size() > 0){
                        Dispositivo dispositivo = dispositivoList.get(7);
                        dispositivo.setEstado("0");
                        AsyncTaskManager atm = new AsyncTaskManager(ConfiguracionActivity.this);
                        atm.doSendDispositivos(dispositivo, device8);
                    }
                }
            }
        });


    }

    private void alterDialog(){

        new AlertDialog.Builder(this)
                .setTitle("Guardar cambios")
                .setMessage("¿Esta seguro que quiere guardar los cambios?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void enviarToken(){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {




            // declare other objects as per your need
            @Override
            protected void onPreExecute() {

            };

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);

            }

            // original
            @Override
            protected Void doInBackground(Void... arg0) {
                BusinessLogicManager blm = new BusinessLogicManager(ConfiguracionActivity.this);
                blm.enviarToken();
                return null;
            }

        };

        task.execute();

    }



    public void actualizarToggleButton(){


        int num = 0;
        ToggleButton [] device = {device1,device2, device3,device4,device5,device6,device7, device8};
        TextView [] dispositivos = {disp1,disp2, disp3,disp4,disp5,disp6,disp7, disp8};
        ApplicationModel model = ApplicationModel.getInstance();
        try{
            List<Dispositivo> dispositivoList = model.getDispositivos();
            if (dispositivoList != null){
                for ( Dispositivo disp : dispositivoList ){
                    dispositivos[num].setText(disp.getAlerta());
                    if (disp.getEstado().equals("0") ){
                        device[num].setChecked(false);
                    }else{
                        device[num].setChecked(true);
                    }
                    num++;
                }



            }
        }catch (Exception ex){

        }


    }

    private void initTab(){

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Admin.");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Envio");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Mensajes");
        host.addTab(spec);
    }


    private void populateMensaje(){


        listViewMensajes = (ListView) findViewById(R.id.listViewMensajes);
        List<Mensaje> mensajes = model.getMensajes();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        if(mensajes != null || mensajes.size() > 0){

        for (Mensaje mensaje: mensajes) {
            Map<String, String> datum = new HashMap<String, String>();


            String datetime =  mensaje.getFecha();
            String datos[] = datetime.split("T");
            String fecha =  datos[0];
            String horas = datos[1];
            String hora =  horas.substring(0,8);

            datum.put("title", mensaje.getTitulo());
            datum.put("subtitle", mensaje.getNombreEntidad() + " - " + fecha + " " + hora);
            datum.put("mensaje", mensaje.getMensaje());
            data.add(datum);
        }

            SimpleAdapter adapter = new SimpleAdapter(this, data,
                    android.R.layout.simple_list_item_2,
                    new String[] {"title", "subtitle"},
                    new int[] {android.R.id.text1,
                            android.R.id.text2});
            listViewMensajes.setAdapter(adapter);
        }

        //}


    }


    public void sincronizar(){

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            // declare other objects as per your need
            @Override
            protected void onPreExecute() {

            };

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);

            }

            // original
            @Override
            protected Void doInBackground(Void... arg0) {
                BusinessLogicManager blm = new BusinessLogicManager(ConfiguracionActivity.this);
                blm.sincronizar();
                return null;
            }

        };

        task.execute();

    }





}




