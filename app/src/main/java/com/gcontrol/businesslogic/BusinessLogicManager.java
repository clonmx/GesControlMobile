package com.gcontrol.businesslogic;

import android.content.Context;

import com.gcontrol.applicationmodel.ApplicationModel;
import com.gcontrol.applicationmodel.Dispositivo;
import com.gcontrol.applicationmodel.Mensaje;
import com.gcontrol.persistence.Sqlite;
import com.gcontrol.persistence.SqliteModel;
import com.gcontrol.persistence.SqliteQuery;
import com.gcontrol.servicefacade.ServiceFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 03-11-2016.
 */

public class BusinessLogicManager {

    private ApplicationModel model = ApplicationModel.getInstance();
    private Context context;

    //private PersistenceService ps;
    private ServiceFacade facade;

    public BusinessLogicManager(Context context) {
        this.context = context;
        //this.ps = new PersistenceService(context);
        this.facade = new ServiceFacade();
    }


    public boolean checkUserInService (String user, String pwmd5){
        boolean result;
        result = facade.LoginResponde(user, pwmd5);

        return result;
    }

    /**
     *
     * @param terminal  nombre del dispositivo ej Terminal_1
     * @param estado  orden para cambiar de estado dispositivo
     * @return         resultado del dispotivo
     */
    public String enviarDispositivos (String terminal, String estado){

        String result =  facade.sendDispositivo(terminal, estado);
        return result;

    }

    public boolean enviarToken(){
        boolean process = false;
        String imei =  model.getImei();
        String token =  model.getToken();
        String user =  model.getUser();

        facade.enviarToken(user,token, imei);

        return true;
    }

    /**
     * Actualiza los valores de base de datos del estado de los dispositivos
     * @return  true: Datos actualizados false: no se pudo actualizar
     */
    public String inicia(){

        String result = "";
        try{
           result = facade.inicia();
        }catch (Exception ex){
            return ex.getMessage().toString();
        }


        return result;
    }

    public List<Dispositivo> getEstadosDispositivos(){
        List<Dispositivo> dispositivoList = new ArrayList<Dispositivo>();
        try{
          dispositivoList = facade.getEstadosDispositivos();
        }catch(Exception ex){
                return null;
        }
           model.setDispositivos(dispositivoList);
         return dispositivoList;
    }

    public boolean  sincronizar() {
        String userId = loadUserFromDataBase();
        inicia();
        getEstadosDispositivos();
        obtenerMensajes(userId);
        return true;
    }


    public boolean enviarMensaje(String mensaje, String tipo){
        String user = model.getUser();
           boolean result =  false;

           result =  facade.enviarMensaje(user,mensaje,tipo);
            return result;
    }


    public void obtenerMensajes(String userId){

        List<Mensaje> mensajes = new ArrayList<Mensaje>();
        try{
            mensajes =   facade.obtenerMensajes(userId);
            model.setMensajes(mensajes);

        }catch(Exception ex){
            ex.getMessage().toString();


        }


    }

    public void createDataBase(){
        SqliteModel sqlmodel = new SqliteModel(context);
        sqlmodel.createDatabase();
    }

    public void insertUserInDataBaseIfNotExists(){

        SqliteQuery sqlquery = new SqliteQuery(context);
        sqlquery.insertUserInDataBase(model.getUser());
    }

    public void dropUserDataTable(){
        SqliteModel sqlModel = new SqliteModel(context);
        sqlModel.dropTableUsers();
    }


    public String loadUserFromDataBase() {



            SqliteQuery sqlquery = new SqliteQuery(context);
           String userid = sqlquery.getUserInDataBase();
            model.setUser(userid);

        return userid;
    }
}

