package com.Wsdl2Code.WebServices.Servicio_WebMonitoreo;

//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version  2.6
//
// Date Of Creation: 11/9/2016 5:44:12 PM
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code  Version
//
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

public class Mensajes implements KvmSerializable {
    
    public String titulo;
    public String mensaje;
    public String entidadMensaje;
    public String fechaEnvio;
    public String usuarioId;
    public String tituloMensaje;
    
    public Mensajes(){}
    
    public Mensajes(SoapObject soapObject)
    {
        if (soapObject == null)
            return;
        if (soapObject.hasProperty("Titulo"))
        {
            Object obj = soapObject.getProperty("Titulo");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                titulo = j.toString();
            }else if (obj!= null && obj instanceof String){
                titulo = (String) obj;
            }
        }
        if (soapObject.hasProperty("Mensaje"))
        {
            Object obj = soapObject.getProperty("Mensaje");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                mensaje = j.toString();
            }else if (obj!= null && obj instanceof String){
                mensaje = (String) obj;
            }
        }
        if (soapObject.hasProperty("EntidadMensaje"))
        {
            Object obj = soapObject.getProperty("EntidadMensaje");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                entidadMensaje = j.toString();
            }else if (obj!= null && obj instanceof String){
                entidadMensaje = (String) obj;
            }
        }
        if (soapObject.hasProperty("FechaEnvio"))
        {
            Object obj = soapObject.getProperty("FechaEnvio");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                fechaEnvio = j.toString();
            }else if (obj!= null && obj instanceof String){
                fechaEnvio = (String) obj;
            }
        }
        if (soapObject.hasProperty("UsuarioId"))
        {
            Object obj = soapObject.getProperty("UsuarioId");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                usuarioId = j.toString();
            }else if (obj!= null && obj instanceof String){
                usuarioId = (String) obj;
            }
        }
        if (soapObject.hasProperty("TituloMensaje"))
        {
            Object obj = soapObject.getProperty("TituloMensaje");
            if (obj != null && obj.getClass().equals(SoapPrimitive.class)){
                SoapPrimitive j =(SoapPrimitive) obj;
                tituloMensaje = j.toString();
            }else if (obj!= null && obj instanceof String){
                tituloMensaje = (String) obj;
            }
        }
    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return titulo;
            case 1:
                return mensaje;
            case 2:
                return entidadMensaje;
            case 3:
                return fechaEnvio;
            case 4:
                return usuarioId;
            case 5:
                return tituloMensaje;
        }
        return null;
    }
    
    @Override
    public int getPropertyCount() {
        return 6;
    }
    
    @Override
    public void getPropertyInfo(int index, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info) {
        switch(index){
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Titulo";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "Mensaje";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "EntidadMensaje";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "FechaEnvio";
                break;
            case 4:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "UsuarioId";
                break;
            case 5:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "TituloMensaje";
                break;
        }
    }
    

    public String getInnerText() {
        return null;
    }
    
    

    public void setInnerText(String s) {
    }
    
    
    @Override
    public void setProperty(int arg0, Object arg1) {
    }
    
}
