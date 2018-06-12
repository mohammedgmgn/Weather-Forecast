package com.essam.shimaa.weatherforecast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.essam.shimaa.weatherforecast.data.WeatherPreferences;
import com.essam.shimaa.weatherforecast.model.WeatherModel;
import com.essam.shimaa.weatherforecast.utilities.NetworkUtils;
import com.essam.shimaa.weatherforecast.utilities.Util;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public String DefaultCityCode;
    RecyclerView mRecyclerView;
    private static int mRequestCode = 1;
    List<WeatherModel> MyWeatherData;
    ForecastAdapter adapter;
    Toolbar toolbar;
    String CityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DefaultCityCode = getString(R.string.mountain_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        toolbar = (Toolbar) findViewById(R.id.toolbartest);
        setSupportActionBar(toolbar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        MyWeatherData = new ArrayList<>();

        if (Util.Operations.isOnline(this)&&savedInstanceState==null) {
                Toast.makeText(MainActivity.this,"select location from menu",Toast.LENGTH_LONG).show();
            loadWeatherData();
        } else if(savedInstanceState==null||!Util.Operations.isOnline(this)) {
            //if in offlline Mode
            String StoredJson = WeatherPreferences.getJson(MainActivity.this);
            try {
                MyWeatherData = Util.Operations.getWeatherValuesFromJson(StoredJson);
                adapter = new ForecastAdapter(MyWeatherData, MainActivity.this, new ForecastAdapter.RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {
                        //cleck item for  if we need to show more details
                    }
                });
                mRecyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private void loadWeatherData() {
        //Deafult coord location (Cairo)
        double lon=31.2357;
        double lat = 30.0444;
        String url = NetworkUtils.buildUrl(this, lon, lat, NetworkUtils.weeklyDays);
        sendJsonRequest(url);
    }

    public void sendJsonRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MyWeatherData = new ArrayList<>();
                try {
                    MyWeatherData = Util.Operations.getWeatherValuesFromJson(response.toString());
                    Util.Operations.updateSharedPrefrence(response.toString(), MainActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("TAG", response.toString());
                adapter = new ForecastAdapter(MyWeatherData, MainActivity.this, new ForecastAdapter.RecyclerViewClickListener() {
                    @Override
                    public void recyclerViewListClicked(View v, int position) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this,getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            }
        });
        ApplicationController.getInstance().addToRequestQueue(request);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.location_id) {
            // locationDialog();
            if (Util.Operations.isOnline(MainActivity.this)) {
                launchIntentBuilder();
            } else {
                Toast.makeText(MainActivity.this,getString(R.string.connection_check), Toast.LENGTH_SHORT).show();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Place mPlace = PlaceAutocomplete.getPlace(this, data);
            double lon = mPlace.getLatLng().longitude;
            double lat = mPlace.getLatLng().latitude;
            CityName = Util.Operations.getCountryName(lat, lon, this);
            Log.v("cur", CityName);
            String url = NetworkUtils.buildUrl(this, lon, lat, NetworkUtils.weeklyDays);
            sendJsonRequest(url);

        }
    }

    private void launchIntentBuilder() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, mRequestCode);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }


}
