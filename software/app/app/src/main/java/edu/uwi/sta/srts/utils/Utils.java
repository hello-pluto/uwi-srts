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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import edu.uwi.sta.srts.R;

public class Utils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Function that checks if a given email has a valid regex
     * @param email The email to validate
     * @return Whether or not the email is valid
     */
    public static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Method that returns a bitmap descriptor from a given vector image
     * @param context The context
     * @param vectorResId The resource id of the vector
     * @return a bitmap descriptor from a given vector image
     */
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Method that highlights a view whenever an edit text gains focus
     * @param c The context
     * @param editText The edit text
     * @param view The view to highlight
     */
    public static void setUpActivations(final Context c, EditText editText, final View view){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundColor(ContextCompat.getColor(c, R.color.colorPrimary));
                }else{
                    view.setBackgroundColor(Color.parseColor("#efefef"));
                }
            }
        });
    }

    /**
     * Method that calculates a simple estimated time between two locations
     * @param lat1 The latitude of the first location
     * @param long1 The longitude of the first location
     * @param lat2 The latitude of the second location
     * @param long2 The longitude of the second location
     * @return The eta in minutes
     */
    public static int getEta(double lat1, double long1, double lat2, double long2){
        Location location1 = new Location("");
        location1.setLatitude(lat1);
        location1.setLongitude(long1);

        Location location2 = new Location("");
        location2.setLatitude(lat2);
        location2.setLongitude(long2);

        float distanceInMeters = location1.distanceTo(location2);
        int speedIs10MetersPerMinute = 100;
        return (int) distanceInMeters / speedIs10MetersPerMinute;
    }

    /**
     * Method that formats a given estimated time of arrival to a string
     * @param eta The eta in minutes
     * @return String representation of the eta
     */
    public static String formatEta(int eta){
        if(eta == 0){
            return "A few seconds away";
        }else if(eta == 1){
            return "1 minute away";
        }else{
            return eta + " minutes away";
        }
    }

    /**
     * Method that returns a color for a given urgency level
     * @param urgency The level of urgency; ranges from 1-5
     * @return The color associated with the urgency level
     */
    public static int getUrgencyColor(float urgency){
        return ColorUtils.blendARGB(Color.parseColor("#fbc02d"), Color.parseColor("#f44336"), urgency/5);
    }

    /**
     * Method that creates a Snackbar anchored to a given view and shows it when the application
     * does not have access to the internet
     * @param view The view to anchor the snackbar to
     */
    public static void setupOfflineSnackbarListener(View view){
        final Snackbar offlineSnackbar = Snackbar.make(view,
                "No internet. All changes saved locally.", Snackbar.LENGTH_INDEFINITE);
        offlineSnackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlineSnackbar.dismiss();
            }
        });
        DatabaseHelper.attachIsOnlineListener(offlineSnackbar);
    }

    /**
     * Method that returns the map style
     * @return The string representation of the map style
     */
    public static String getMapStyle(){
        return "[\n" +
                "    {\n" +
                "        \"featureType\": \"all\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"off\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"landscape\",\n" +
                "        \"elementType\": \"all\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"on\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#f3f4f4\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"landscape.man_made\",\n" +
                "        \"elementType\": \"geometry\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"weight\": 0.9\n" +
                "            },\n" +
                "            {\n" +
                "                \"visibility\": \"off\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"poi.park\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"on\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#83cead\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"road\",\n" +
                "        \"elementType\": \"all\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"on\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#ffffff\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"road\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"simplified\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#b5b4b4\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"road.highway\",\n" +
                "        \"elementType\": \"all\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"on\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#fee379\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"road.highway\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"simplified\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"invert_lightness\": true\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"road.arterial\",\n" +
                "        \"elementType\": \"all\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"on\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#fee379\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"road.arterial\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"invert_lightness\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"visibility\": \"simplified\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"lightness\": \"10\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"weight\": \"0.40\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    {\n" +
                "        \"featureType\": \"water\",\n" +
                "        \"elementType\": \"all\",\n" +
                "        \"stylers\": [\n" +
                "            {\n" +
                "                \"visibility\": \"on\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"color\": \"#7fc8ed\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";
    }
}
