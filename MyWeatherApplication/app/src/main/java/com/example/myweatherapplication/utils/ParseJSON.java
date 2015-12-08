package com.example.myweatherapplication.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;

/**
 * Created by Влад on 15.11.2015.
 */
public class ParseJSON {

    private JSONObject jsonObject = null;
    private JSONObject jCity = null;
    private JSONObject jMain = null;
    private JSONObject jNull = null;
    private JSONArray jsonArray = null;

    public ParseJSON(JSONObject weatherInfo) {
        jsonObject = weatherInfo;
    }
    public void setCountInArray(int count) {
        try {
            jNull = jsonArray.getJSONObject(count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean isNull() {
        if (jsonObject == null)
            return true;
        else {
            try {
                jsonArray = jsonObject.getJSONArray("list");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try{
                jCity = jsonObject.getJSONObject("city");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    /*Получаем страну */
    public String getCountry(){
        String country = "";
        try {
            country = jCity.getString("country");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return country;
    }
    /*Получаем город */
    public String getCity(){
        String city = "";
        try {
            city = jCity.getString("name");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return city;
    }
    /*Получаем температуру */
    public double getTemp(){
        double temp = 0;

        try{
            jMain = jNull.getJSONObject("main");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try{
            temp = jMain.getDouble("temp");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return Math.round(temp);
    }
    /*Получаем погоду */
    public String getDescription(){
        String description = "";
        JSONArray jsonArr = null;
        JSONObject jDescription = null;

        try{
            jsonArr = jNull.getJSONArray("weather");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try{
            jDescription = jsonArr.getJSONObject(0);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try{
            description = jDescription.getString("main");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return description;
    }
    /*Получаем скорость ветра */
    public double getSpeedWind(){
        double speedWind = 0;
        JSONObject jWind = null;

        try {
            jWind = jNull.getJSONObject("wind");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try {
            speedWind = jWind.getDouble("speed");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return Math.round(speedWind);
    }
    /*Получаем давление */
    public double getPressure(){
        double pressure = 0;

        try{
            jMain = jNull.getJSONObject("main");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try {
            pressure = jMain.getDouble("pressure");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return Math.round(pressure * 0.750064);
    }
    /*Получаем картинку*/
    public String getIconURL() {
        JSONObject jIco = null;
        String ico = "";
        JSONArray jArr = null;
        try{
            jArr = jNull.getJSONArray("weather");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try{
            jIco = jArr.getJSONObject(0);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        try{
            ico = jIco.getString("icon");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return ico;
    }
    /*Получаем дату*/
    public String getDateAndTime(){
        String dateAndTime = "";
        try {
            dateAndTime = jNull.getString("dt_txt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dateAndTime = dateAndTime.substring(8,10) + "." + dateAndTime.substring(5,7) + "." + dateAndTime.substring(0, 4)
        + " " + dateAndTime.substring(11, 16);
        return dateAndTime;
    }
    public int getSizeOfList() {
        int cnt = 0;
        try {
            cnt = jsonObject.getInt("cnt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cnt;
    }
}
