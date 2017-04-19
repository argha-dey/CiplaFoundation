package com.ciplafoundation.model;

import java.io.Serializable;
import java.util.ArrayList;

public class TreeDataModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String levelId = "";
    private String parentId = "";
    private String levelDesc = "";
    private String parentLevel = "";
    private String breadCrumb = "";
    private boolean isSearched = false;
    private boolean isClicked = false;
    private ArrayList<TreeDataModel> childDataEntity = new ArrayList<TreeDataModel>();

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public String getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(String parentLevel) {
        this.parentLevel = parentLevel;
    }

    public boolean getIsSearched() {
        return isSearched;
    }

    public void setIsSearched(boolean isSearched) {
        this.isSearched = isSearched;
    }

    public boolean getIsClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public ArrayList<TreeDataModel> getChildDataEntity() {
        return childDataEntity;
    }

    public void setChildDataEntity(ArrayList<TreeDataModel> childDataEntity) {
        this.childDataEntity = childDataEntity;
    }

    public String getBreadCrumb() {
        return breadCrumb;
    }

    public void setBreadCrumb(String breadCrumb) {
        this.breadCrumb = breadCrumb;
    }

}

