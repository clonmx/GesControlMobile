package com.gcontrol.servicefacade;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.Dispositivos;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.Mensajes;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.ServicioMonitoreo;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.Servicio_WebMonitoreo;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.VectorDispositivos;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.VectorMensajes;
import com.gcontrol.applicationmodel.Dispositivo;
import com.gcontrol.applicationmodel.Mensaje;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.R.attr.data;
import static android.R.attr.queryActionMsg;

/**
 * Created by chris on 03-11-2016.
 */

public class ServiceFacade {

    private Servicio_WebMonitoreo ws = new Servicio_WebMonitoreo();
    private ServicioMonitoreo sm = new ServicioMonitoreo();

    /**
     * @param user nombre de usuario
     * @param pwd  contraseña
     * @return true:exitoso false; contraseña o usuario incorrecto
     */
    public boolean LoginResponde(String user, String pwd) {

        boolean result = false;
        try {
            //llamar servicio login
            result = ws.Login(user, pwd);
            //result = sm.Login(user, pwd);
            return result;

        } catch (Exception ex) {
            return false;
        }
    }

    public void enviarToken(String user, String token, String imei) {
        boolean process = false;

        process = ws.Registro(imei, token, user);
        }

    /**
     * @return lista con los estados actualizados de los dispositivos
     */
    public List<Dispositivo> getEstadosDispositivos() {
        List<Dispositivo> dispositivoList = new ArrayList<Dispositivo>();
        //llamada webservice
        VectorDispositivos vector = new VectorDispositivos();

        int num = 1;
        try{
            vector = ws.Get_Estado_Dispositivos();
            for ( Dispositivos dispositivo : vector ){
                Dispositivo disp = new Dispositivo();
                disp.setEstado(dispositivo.estado);
                disp.setTerminal("Terminal_" + num );
                disp.setTipo(dispositivo.tipo);
                disp.setAlerta(dispositivo.string_Alerta);
                dispositivoList.add(disp);

                num++;
            }

        }catch(Exception ex){
            return null;
        }


        int size = vector.size();
        //recorrer resultado y completar lista

        return dispositivoList;

    }


    /**
     *
     * @param terminal   Termina_1, Termina_2, Termina_3, Terminal_4, Terminal_5, Terminal_6, Terminal_7, Terminal_8
     * @param estatus  0: Apagar 1: Prender
     * @return    0: apagado correctamente, 1:prendido correctamente, 2: error al conectarse a placa
     */
    public String sendDispositivo(String terminal, String estatus) {
        String result = "";
        //"17" significa que cambio de estado satifactoriamente el dispositivo
        result = ws.SendOutDevicetoDevice(terminal, estatus);
        return result;
    }

    /**
     * Actualiza los valores de la base de datos
     *
     * @return OK:
     */
    public String inicia() {

        try {
            String result = ws.Inicia();
            return result;
        } catch (Exception ex) {
            return ex.getMessage().toString();
        }

    }

    public boolean enviarMensaje(String userId, String mensaje, String tipo){
        boolean result = false;

        result =  ws.InsertarMensajes(userId, mensaje, tipo);
        return result;
    }

    public List<Mensaje> obtenerMensajes(String userId){
        // llamar servicio de mensajeria
        List<Mensaje> mensajeList = new ArrayList<Mensaje>();
        VectorMensajes mensajes = new VectorMensajes();
        mensajes = ws.ObtenerMensajes(userId);
        for (Mensajes data : mensajes){
            Mensaje mensaje = new Mensaje();

            mensaje.setFecha(data.fechaEnvio);
            mensaje.setTitulo(data.titulo);
            mensaje.setMensaje(data.mensaje);
            mensaje.setNombreEntidad(data.entidadMensaje);
            mensajeList.add(mensaje);
        }

        return mensajeList;
    }



}






