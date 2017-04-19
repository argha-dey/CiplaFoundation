package com.ciplafoundation.model;

import java.io.Serializable;

/**
 * Created by User-66-pc on 3/23/2017.
 */

public class UserRole implements Serializable {

    private static final long serialVersionUID = 2L;

    private String role_id="";
    private String role_name="";

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
