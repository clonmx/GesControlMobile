package com.gcontrol.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.widget.TextView;

import com.gcontrol.activities.WebViewActivity;

import net.simplifiedcoding.androidgcm.R;

/**
 * Created by cediaz on 09-11-2016.
 */


public class Utils {

    private Context context;
    public Utils(Context context) {
       this.context = context;
    }

    public void displayPopUp(String message){
        new AlertDialog.Builder(context)

                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //marcar como leido


                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void displayAlertDialogAfirmation(String title, String question){

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(question)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete


                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void openUrl(String url){

        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);


    }

    public void customActionBar(ActionBar actionBar){

        TextView tv = new TextView(context);
        tv.setText(actionBar.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setText(actionBar.getTitle());
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(null, Typeface.BOLD);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(tv);

    }

}
