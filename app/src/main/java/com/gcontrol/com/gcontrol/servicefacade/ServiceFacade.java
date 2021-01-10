package com.gcontrol.com.gcontrol.servicefacade;

import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.Dispositivos;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.ServicioMonitoreo;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.Servicio_WebMonitoreo;
import com.Wsdl2Code.WebServices.Servicio_WebMonitoreo.VectorDispositivos;
import com.gcontrol.applicationmodel.Dispositivo;
import java.util.ArrayList;
import java.util.List;

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


        int size = vector.size();
        //recorrer resultado y completar lista

        return dispositivoList;

    }


    public boolean sendDispositivo(String terminal, String estatus) {
        boolean process = false;
        //"17" significa que cambio de estado satifactoriamente el dispositivo
        String result = ws.SendOutDevicetoDevice(terminal, estatus);

        if (result.equals("17")) {
            process = true;
        }

        return process;
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
}




