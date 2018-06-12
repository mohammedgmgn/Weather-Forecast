package com.essam.shimaa.weatherforecast.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;


import com.essam.shimaa.weatherforecast.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by siko on 6/24/2017.
 */

public class NetworkUtils {
    private static final String WEATHER_URL =
            "http://api.openweathermap.org/data/2.5/forecast/daily?";


    /* The format we want our API to return */
    private static final String format = "json";
    /* The units we want our API to return */
    private static final String units = "metric";
    /* The number of days we want our API to return */
    public static final int weeklyDays = 7;
    public static final int CurDays = 2;
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";
    public static String buildUrl(Context context, Double lon,Double lat,int numDays) {
        String apiKey=context.getString(R.string.api_key);
        /** This will be implemented in a future lesson **/
        String url=WEATHER_URL+"lon"+"="+lon+"&"+"lat"+"="+lat+"&"+FORMAT_PARAM+"="+format+"&"+UNITS_PARAM+"="+units+"&"+DAYS_PARAM+"="+numDays+"&"+"APPID="+apiKey;
        Log.v("LONG", "Built URI " + url);

        return url;
    }

}

