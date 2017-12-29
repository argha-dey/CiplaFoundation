package com.ciplafoundation.model;

import java.io.Serializable;

public class UserClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private String userId = "";
	private String email="";
	private String role="";
	private String roleId="";
	private String divisionId;
	private String password = "";
	private boolean isRemember = false;
	private boolean isLoggedin = false;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	private String tokenId="";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public boolean getIsLoggedin() {
		return isLoggedin;
	}

	public void setIsLoggedin(boolean loggedin) {
		isLoggedin = loggedin;
	}

	public boolean getIsRemember() {
		return isRemember;
	}

	public void seIsRemember(boolean remember) {
		isRemember = remember;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
