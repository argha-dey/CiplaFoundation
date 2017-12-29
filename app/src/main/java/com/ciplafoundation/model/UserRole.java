package com.ciplafoundation.model;

import java.io.Serializable;

/**
 * Created by User-66-pc on 3/23/2017.
 */

public class UserRole implements Serializable {

    private static final long serialVersionUID = 2L;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String userId="";
    private String userName="";
    private String   roleId="";
    private String role="";

    public  UserRole(String userId,String userName,String roleId,String role){
        this.userId=userId;
        this.userName=userName;
        this.roleId=roleId;
        this.role=role;

    }
    public UserRole(){

    }


}
