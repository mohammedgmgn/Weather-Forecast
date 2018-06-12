package com.essam.shimaa.weatherforecast.model;

/**
 * Created by siko on 6/24/2017.
 */

public class WeatherModel {
    private String CityName;
    private double Max;
    private String description;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public double getMax() {
        return Max;
    }

    public void setMax(double max) {
        Max = max;
    }

    public double getMin() {
        return Min;
    }

    public void setMin(double min) {
        Min = min;
    }

    private double Min;
}
