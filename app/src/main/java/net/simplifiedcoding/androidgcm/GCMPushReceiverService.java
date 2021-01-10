package net.simplifiedcoding.androidgcm;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.gcontrol.activities.ConfiguracionActivity;
import com.gcontrol.activities.LoadActivity;
import com.gcontrol.businesslogic.AsyncTaskManager;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import static android.R.id.message;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService  {

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String title = data.getString("title");
        String message = data.getString("message");
        String tipo = data.getString("subtipo");
        String url = data.getString("url");
        String iconUrl = data.getString("iconURL");
        //Displaying a notiffication with the message
        sendNotification(title, message, tipo, iconUrl, url);
    }



    //This method is generating a notification and displaying the notification
    private void sendNotification(String titulo, String message, String tipo, String iconUrl, String url ) {
        Intent intent = new Intent(this, LoadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        notificationManager(titulo, message, url, tipo, intent);

    }

    private int randomUniqueId (){
        int max = 100;
        int min = 0;
        Random rn = new Random();
        int n = max - min + 1;
        int i = rn.nextInt() % n;
        int randomNum =  min + i;
        return randomNum;
    }

    private Integer selectIcon(String tipo){
         tipo = tipo;
         if(tipo.equals("2")){
            return R.mipmap.humedad;
        }else if(tipo.equals("3")){
            return  R.mipmap.incendio;
         }else if(tipo.equals("1")){
            return R.mipmap.gas;
         }else if (tipo.equals("4")){
            return R.mipmap.medico;
         }else if (tipo.equals("5")) {
             return R.mipmap.www;
         }else{
             return R.mipmap.informacion;
         }

    }

    private PendingIntent createPendingIntentURL(String url, int requestCode){

        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }


    private void notificationManager ( String title, String message, String url, String tipoNotification, Intent intent){
        int withURL;
       if(tipoNotification.equals("5")){
           notificationWithURL(message, url, intent, tipoNotification, title);
       }else{
           notification(message, intent, tipoNotification, title);
       }
    }



    private void notificationWithURL(String message, String url, Intent intent, String tipo, String title){

        int requestCode = 0;
        requestCode = randomUniqueId();

        PendingIntent intentURL = createPendingIntentURL(url, requestCode);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(selectIcon(tipo))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(intentURL)
               //.addAction(R.drawable.url, "Ir Url", intentURL)
        .setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
        //.setLargeIcon(bitmapUrl());

        //intent.getStringExtra("nombre de lo que se envio");
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, noBuilder.build()); //0 = ID of notification
    }

    private void notification(String message, Intent intent, String tipo, String title){


        int requestCode = 0;
        requestCode = randomUniqueId();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(selectIcon(tipo))
                .setContentTitle(title.trim())
                .setContentText(message.trim())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND| Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, noBuilder.build()); //0 = ID of notification

    }

    private Bitmap bitmapUrl(){


        InputStream in;
        String linkImage = "http://image.flaticon.com/icons/svg/124/124010.svg";
        try {
           URL url = new URL(linkImage);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;




        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
