package com.example.mtlc.opencv;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class JsonUtils {

    public JsonUtils(){

    }

    public  static JSONObject convertToJsonObject(String name,String date,String photo ) throws JSONException {
           JSONObject json = new JSONObject();
                      json.put("name",name);
                       json.put("photo",photo);
                        json.put("date", date);
        return  json;
    }



  public static  void updateSetting(String name,boolean value,Activity activity){
       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
       SharedPreferences.Editor editor = prefs.edit();
       editor.putBoolean(name, value);
       editor.apply();
   }

    public static void addActivity(String name, String date , String photo,Activity activity) throws JSONException {
        JSONObject object= convertToJsonObject(name,date,photo);
        JSONArray array= getArrayList("activities",activity);
        array.put(object);
        saveArrayList("activities",array,activity);

    }

    public static void saveArrayList(String key ,JSONArray json, Activity activity){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, json.toString());
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static JSONArray getArrayList(String key ,Activity activity) throws JSONException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
     String str=   prefs.getString(key,"");

     JSONArray array =new JSONArray(str);
     return  array;
    }




}
