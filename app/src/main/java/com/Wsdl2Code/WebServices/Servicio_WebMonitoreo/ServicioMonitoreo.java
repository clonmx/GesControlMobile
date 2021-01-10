package com.Wsdl2Code.WebServices.Servicio_WebMonitoreo;

import org.ksoap2.SoapEnvelope;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.io.IOException;

/**
 * Created by cediaz on 06-11-2016.
 */

public class ServicioMonitoreo {


    private static final String SOAP_ACTION = "http://tempuri.org/Login";
    private static final String METHOD_NAME = "Login";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://gestcontrol.sytes.net/webservice/Servicio_WebMonitoreo.asmx";
    public IWsdl2CodeEvents eventHandler;

    public boolean Login(String user, String pw) {

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            request.addProperty("user", user);
            request.addProperty("pass", pw);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            int timeOut = 100000;
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, timeOut);


            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapObject result = (SoapObject) envelope.getResponse();

            //To get the data.
            // String resultData=result.getProperty(0).toString();


        } catch (Exception e) {
            if (eventHandler != null)
                eventHandler.Wsdl2CodeFinishedWithException(e);
            e.printStackTrace();
        }

        return false;
    }

}

