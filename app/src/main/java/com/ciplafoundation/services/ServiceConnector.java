package com.ciplafoundation.services;

import org.json.JSONObject;

abstract class ServiceConnector {


      //public static final String baseURL = "http://192.168.1.88/cipla/app/";
      public static final String baseURL = "http://123.63.224.20/cipla/app/";

    protected JSONObject outputJson;

    public static String getBaseURL() {
        return baseURL;
    }

    public JSONObject getOutputJson() {

        return outputJson;
    }

    public void setOutputJson(JSONObject outputJson) {
        this.outputJson = outputJson;
    }

}
