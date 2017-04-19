package com.ciplafoundation.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by User-66-pc on 3/24/2017.
 */

public class UserDivision implements Serializable {

    private static final long serialVersionUID = 4L;

    private String divisionId;
    private String name;
    private String lat;
    private String lng;
    private String state;
    private String count;
   // private com.google.android.gms.maps.model.LatLng LatLng;



    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    /*public com.google.android.gms.maps.model.LatLng getLatLng() {
        return LatLng;
    }

    public void setLatLng(com.google.android.gms.maps.model.LatLng latLng) {
        LatLng = latLng;
    }*/
}
