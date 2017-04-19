package com.ciplafoundation.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User-66-pc on 3/23/2017.
 */

public class UserRoleClass implements Serializable {

    private static final long serialVersionUID = 3L;

    public ArrayList<UserRole> details = new ArrayList<>();
    public String status;
    public String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<UserRole> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<UserRole> details) {
        this.details = details;
    }
}
