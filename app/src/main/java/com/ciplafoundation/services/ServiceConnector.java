package com.ciplafoundation.services;

import org.json.JSONObject;

abstract class ServiceConnector {


     // public static final String baseURL = "http://192.168.1.88/cipla2/app/";
   // public static final String baseURL = "http://192.168.1.148/cipla2/app/";
   // public static final String baseURL = "http://123.63.224.20/cipla2/app/";
  // public static final String baseURL = "http://192.168.1.88/cipla2/app/";
    public static final String baseURL = "http://192.168.1.89:8080/cipla2/app/";

      //public static final String baseURL = "http://123.63.224.20/cipla/app/";
      //public static final String baseURL = "https://track.ciplafoundation.org/app/";
    //public static final String baseURL = "http://192.168.1.148/cipla2/app/";

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
