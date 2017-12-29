package com.ciplafoundation.model;

import java.io.Serializable;

/**
 * Created by User-66-pc on 3/31/2017.
 */

public class AcceptedProposal implements Serializable {

    private static final long serialVersionUID = 5L;

    private String id;
    private String proposal_no;
    private String time_line_date;
    private String time_line_duration;
    private String ngo_type;
    private String ngo;
    private String title;
    private String budget;
    private String created_by;
    private String created_date;
    private boolean isExpanded=false;

    public String getFund_requested() {
        return fund_requested;
    }

    public void setFund_requested(String fund_requested) {
        this.fund_requested = fund_requested;
    }

    public String getSanctioned_amount() {
        return sanctioned_amount;
    }

    public void setSanctioned_amount(String sanctioned_amount) {
        this.sanctioned_amount = sanctioned_amount;
    }

    public String getCost_treatment() {
        return cost_treatment;
    }

    public void setCost_treatment(String cost_treatment) {
        this.cost_treatment = cost_treatment;
    }

    private String fund_requested;
    private String sanctioned_amount;
    private String cost_treatment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProposal_no() {
        return proposal_no;
    }

    public void setProposal_no(String proposal_no) {
        this.proposal_no = proposal_no;
    }

    public String getTime_line_date() {
        return time_line_date;
    }

    public void setTime_line_date(String time_line_date) {
        this.time_line_date = time_line_date;
    }

    public String getTime_line_duration() {
        return time_line_duration;
    }

    public void setTime_line_duration(String time_line_duration) {
        this.time_line_duration = time_line_duration;
    }

    public String getNgo_type() {
        return ngo_type;
    }

    public void setNgo_type(String ngo_type) {
        this.ngo_type = ngo_type;
    }

    public String getNgo() {
        return ngo;
    }

    public void setNgo(String ngo) {
        this.ngo = ngo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public boolean getisExpanded() {
        return isExpanded;
    }

    public void setisExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
