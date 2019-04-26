/*
 * Copyright (c) 2019. Razor Sharp Software Solutions
 *
 * Azel Daniel (816002285)
 * Michael Bristol (816003612)
 * Amanda Seenath (816002935)
 *
 * INFO 3604
 * Project
 *
 * UWI Shuttle Routing and Tracking System
 */

package edu.uwi.sta.srts.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import edu.uwi.sta.srts.R;

public class InfoDialogHelper {

    /**
     * Method that shows an information dialog with the given title and message
     * @param context The context
     * @param title The title of the information being conveyed to the user
     * @param message The message of the information being conveyed to the user
     */
    public static void showInfoDialog(Context context, String title, String message){
        View view = LayoutInflater.from(context).inflate(R.layout.info_header, null);
        TextView titleText =  view.findViewById(R.id.title);
        final TextView messageText = view.findViewById(R.id.message);
        titleText.setText(title + " Guide");
        messageText.setMovementMethod(new ScrollingMovementMethod());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            messageText.setText(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
        } else {
            messageText.setText(Html.fromHtml(message));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                messageText.scrollTo(0,0);
            }
        }, 100);
        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
