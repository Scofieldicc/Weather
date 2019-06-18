package com.example.administrator.weather.Bean;

import com.example.administrator.weather.Bean.FutureWeather;
import com.example.administrator.weather.Bean.TodayWeather;

import org.json.JSONException;
import org.json.JSONObject;

public class GetBean {

    public TodayWeather GetTodayBean(JSONObject Object) {

        TodayWeather bean = new TodayWeather();
        try {
            bean.setTemp(Object.getString("temp"));
            bean.setHumidity(Object.getString("humidity"));
            bean.setWind_direction(Object.getString("wind_direction"));
            bean.setWind_strength(Object.getString("wind_strength"));
            bean.setWeather(Object.getString("weather"));
            bean.setTemperature(Object.getString("temperature"));
            bean.setExercise_index(Object.getString("exercise_index"));
            bean.setWash_index(Object.getString("wash_index"));
            bean.setDressing_index(Object.getString("dressing_index"));
            bean.setDressing_advice(Object.getString("dressing_advice"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return bean;
    }
    public FutureWeather GetFutureBean(JSONObject Object) {
        FutureWeather bean = new FutureWeather();
        try {
            bean.setWeather(Object.getString("weather"));
            bean.setTemperature(Object.getString("temperature"));
            bean.setDate(Object.getString("date"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }


}
