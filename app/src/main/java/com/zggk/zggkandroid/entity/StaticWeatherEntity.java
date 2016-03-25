package com.zggk.zggkandroid.entity;

/**
 * Created by Aaron on 16/3/22.
 */
public class StaticWeatherEntity {
    private String weather;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    private int type;
}
