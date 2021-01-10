package com.gcontrol.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import com.gcontrol.applicationmodel.ApplicationModel;

import com.gcontrol.businesslogic.BusinessLogicManager;
import com.gcontrol.utils.Utils;

import net.simplifiedcoding.androidgcm.R;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogActivity extends AppCompatActivity {


    private EditText edtUser;
    private EditText edtPassword;
    private Context myContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        myContext = this.getApplicationContext();
        ApplicationModel model = ApplicationModel.getInstance();
        Utils util = new Utils(this);
        ActionBar actionBar = getSupportActionBar();
        util.customActionBar(actionBar);

        String user = model.getUser();
        String pw = model.getPassword();

        if(user != null && pw != null){
        edtUser = (EditText) findViewById(R.id.edtUsuario);
            edtPassword = (EditText) findViewById(R.id.edtPassword);
            edtUser.setText(user);
            edtPassword.setText(pw);
        }

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button btnSignIn = (Button) findViewById(R.id.btnSingIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                IniciarSesion();

            }
        });





    }

    private void IniciarSesion(){

        EditText usuario = (EditText) findViewById(R.id.edtUsuario);
        EditText password = (EditText) findViewById(R.id.edtPassword);
        ApplicationModel.getInstance().setContext(LogActivity.this);

        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        //final String pw = md5(password.getText().toString());
        final String pw = password.getText().toString();
        final String user = usuario.getText().toString();

        final String imei = checkPermisionTManager();
        ApplicationModel.getInstance().setPassword(pw);
        ApplicationModel.getInstance().setUser(user);
        ApplicationModel.getInstance().setImei(imei);

      if(pw.equals("") || pw.trim().equals("")|| user.equals("") || user.trim().equals("") ){
          String message = "Ingrese usuario o contraseña correcta";
          String title = "Error login";
          Utils utils = new Utils(LogActivity.this);
          utils.displayAlertDialogAfirmation(title, message);
          return;
      }else{
          AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

              boolean success;
              String message = "";
              ProgressDialog progressDialog;

              private boolean doLogin(String user, String pw) {
                  boolean exist = false;
                  BusinessLogicManager blm = new BusinessLogicManager(
                          LogActivity.this);

                  exist = blm.checkUserInService(user, pw);

                  if(exist){
                      blm.dropUserDataTable();
                      blm.insertUserInDataBaseIfNotExists();

                  }

                  return exist;

              }

              @Override
              protected Void doInBackground(Void... params) {
                  try{
                    success =  doLogin(user, pw);
                  }catch(Exception ex){
                    success = false;
                  }
                  return null;
              }

              // declare other objects as per your need
              @Override
              protected void onPreExecute() {
                  progressDialog = ProgressDialog.show(LogActivity.this,
                          "Ingreso al sistema",
                          "Verificando las credenciales del usuario.", true);
                  // do initialization of required objects objects here
              };


              @Override
              protected void onPostExecute(Void result) {

                  super.onPostExecute(result);
                  progressDialog.dismiss();

                  if (success) {

                      message = "Ingreso exitoso.";
                      Toast.makeText(myContext, message, Toast.LENGTH_LONG)
                              .show();


                      final Intent intent = new Intent(LogActivity.this,
                              LoadActivity.class);
                      startActivity(intent);
                      finish();

                  } else {
                      message = "Usuario o Contraseña incorrecto";
                      Toast.makeText(myContext, message, Toast.LENGTH_LONG)
                              .show();
                  }
              }

          };
          task.execute();
        }
    }

    private void doSendData(){



    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public void showDialog(String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setMessage(message);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface di, int id) {
            }
        });
        AlertDialog ad = adb.create();
        ad.setTitle("GesControl");
        ad.show();

    }


    public String checkPermisionTManager(){
        String identifier = null;
        try{
            TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            identifier = tm.getDeviceId();
        }catch(Exception ex){
            identifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        return identifier;

    }

    private void customActionBar(){

        ActionBar actionBar = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());
        tv.setText(actionBar.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        actionBar.setLogo(R.drawable.logo);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);



    }



}
