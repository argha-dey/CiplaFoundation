package com.ciplafoundation.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class Prefs {

    private Context context = null;

    public Prefs(Context context) {
        this.context = context;
    }

    private String getString(String key, String def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = prefs.getString(key, def);
        return s;
    }

    private int getInt(String key, int def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int i = Integer.parseInt(prefs.getString(key, Integer.toString(def)));
        return i;
    }

    private float getFloat(String key, float def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        float f = Float.parseFloat(prefs.getString(key, Float.toString(def)));
        return f;
    }

    private long getLong(String key, long def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long l = Long.parseLong(prefs.getString(key, Long.toString(def)));
        return l;
    }

    private void setString(String key, String val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putString(key, val);
        e.commit();
    }

    private void setBoolean(String key, boolean val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }

    private void setInt(String key, int val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putString(key, Integer.toString(val));
        e.commit();
    }

    private void setLong(String key, long val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor e = prefs.edit();
        e.putString(key, Long.toString(val));
        e.commit();
    }

    private boolean getBoolean(String key, boolean def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean b = prefs.getBoolean(key, def);
        return b;
    }

    private String getStringValue(String key, String val) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String str = prefs.getString(key, val);
        return str;
    }


    public String getDivisionId() {
        return getString(Constants.DIVISIONID, "");
    }

    public void setDivisionId(String val) {
        setString(Constants.DIVISIONID, val);
    }

    public String getDivisionName() {
        return getString(Constants.DIVISIONNAME, "");
    }

    public void setDivisionName(String val) {
        setString(Constants.DIVISIONNAME, val);
    }

    public boolean getIsProjectDetails() {
        return getBoolean(Constants.ISPROJECTDETAILS,false);
    }

    public void setIsProjectDetails(boolean val) {
        setBoolean(Constants.ISPROJECTDETAILS, val);
    }





    public void clearPrefdata() {

        setDivisionId("");
        setDivisionName("");
        setIsProjectDetails(false);
    }
}
