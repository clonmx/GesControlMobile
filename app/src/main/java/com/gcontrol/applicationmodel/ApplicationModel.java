package com.gcontrol.applicationmodel;

import android.content.Context;

import java.util.List;

/**
 * Created by chris on 03-11-2016.
 */

public class ApplicationModel {


    private static ApplicationModel instance;
    private Context context;
    private String user;
    private String password;
    private String imei;
    private String token;
    private List<Dispositivo> dispositivos;
    private List<Mensaje> mensajes;



    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public List<Dispositivo> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public static synchronized ApplicationModel getInstance() {
        if (instance == null) {
            instance = new ApplicationModel();
            instance.initInstance();
        }
        return instance;
    }

    private void initInstance() {

    }

}
