package com.mahmoud.mohammed.weatherforecast.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mahmoud.mohammed.weatherforecast.data.WeatherPreferences;
import com.mahmoud.mohammed.weatherforecast.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by siko on 6/22/2017.
 */

public class Util {
    public static final class Operations {
        /* Location information */
        private static final String OWM_CITY = "city";

        private static final String CITY_NAME = "name";

        private static final String OWM_LIST = "list";

        private static final String OWM_TEMPERATURE = "temp";

        /* Max temperature for the day */
        private static final String OWM_MAX = "max";
        private static final String OWM_MIN = "min";


        private Operations() throws InstantiationException {
            throw new InstantiationException("This class is not for instantiation");
        }

        /**
         * Checks to see if the device is online before carrying out any operations.
         *
         * @return
         */
        public static boolean isOnline(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }

        public static String getCountryName(double latitude, double longitude, Context context) {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String knownName = addresses.get(0).getFeatureName();
            String curName = addresses.get(0).getCountryName();
            return curName;

        }

        public static List<WeatherModel> getWeatherValuesFromJson(String forecastJsonStr) throws JSONException {
            List<WeatherModel> weatherData = new ArrayList<>();

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
            String cityname = cityJson.getString(CITY_NAME);

            JSONArray jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST);
            for (int i = 0; i < jsonWeatherArray.length(); i++) {
                WeatherModel weatherModel = new WeatherModel();

                JSONObject jsonlistobject = jsonWeatherArray.getJSONObject(i);
                JSONObject temp = jsonlistobject.getJSONObject("temp");
                double day = temp.getDouble("day");
                double max = temp.getDouble(OWM_MAX);
                double min = temp.getDouble(OWM_MIN);
                weatherModel.setMin(min);
                weatherModel.setMax(max);

                JSONArray wetherjsondetail = jsonlistobject.getJSONArray("weather");
                for (int j = 0; j < wetherjsondetail.length(); j++) {
                    JSONObject jsonobjectwether = wetherjsondetail.getJSONObject(j);
                    String description = jsonobjectwether.getString("description");
                    weatherModel.setDescription(description);

                }

                weatherData.add(weatherModel);
            }

            return weatherData;

        }

        public static void updateSharedPrefrence(String jsonResponse, Context ctx) {
            SharedPreferences.Editor editor = WeatherPreferences.getSharedPreferences(ctx).edit();
            editor.clear(); //clear all stored data
            editor.commit();
            WeatherPreferences.setJson(ctx, jsonResponse);

        }

    }

    private Util() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

}
