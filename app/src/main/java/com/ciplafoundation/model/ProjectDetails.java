package com.ciplafoundation.model;

import java.io.Serializable;

/**
 * Created by User-66-pc on 4/4/2017.
 */

public class ProjectDetails implements Serializable{

    private static final long serialVersionUID = 8L;

    private String id;
    private String project_title;
    private String budget;
    private String deviation_cost;
    private String report_submission_interval;
    private String timeline;
    private String duration;
    private String duration_word;
    private String deviation_time;
    private String details_brief;
    private String expenditure;
    private String project_no;
    private String approved_date;
    private String deliverables;
    private String pan;
    private String deviation;
    private String mou;
    private String monitering;
    private String closure;

    public String getApprovalMessage() {
        return ApprovalMessage;
    }

    public void setApprovalMessage(String approvalMessage) {
        ApprovalMessage = approvalMessage;
    }

    private String ApprovalMessage;
    private boolean ApprovalMessageVisibility;
    public boolean isApprovalMessageVisibility() {
        return ApprovalMessageVisibility;
    }

    public void setApprovalMessageVisibility(boolean approvalMessage) {
        ApprovalMessageVisibility = approvalMessage;

    }







    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    private String documents;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    private String track;
    private String fund_distribution;
    private String view_fund_distribution;


    public String getFund_distribution() {
        return fund_distribution;
    }

    public void setFund_distribution(String fund_distribution) {
        this.fund_distribution = fund_distribution;
    }




    public String getView_fund_distribution() {
        return view_fund_distribution;
    }



    public void setView_fund_distribution(String view_fund_distribution) {
        this.view_fund_distribution = view_fund_distribution;
    }

    public String getDeliverables() {
        return deliverables;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getDeviation() {
        return deviation;
    }

    public void setDeviation(String deviation) {
        this.deviation = deviation;
    }

    public String getMou() {
        return mou;
    }

    public void setMou(String mou) {
        this.mou = mou;
    }

    public String getMonitering() {
        return monitering;
    }

    public void setMonitering(String monitering) {
        this.monitering = monitering;
    }

    public String getClosure() {
        return closure;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }

    public String getProject_title() {
        return project_title;
    }

    public void setProject_title(String project_title) {
        this.project_title = project_title;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDeviation_cost() {
        return deviation_cost;
    }

    public void setDeviation_cost(String deviation_cost) {
        this.deviation_cost = deviation_cost;
    }

    public String getReport_submission_interval() {
        return report_submission_interval;
    }

    public void setReport_submission_interval(String report_submission_interval) {
        this.report_submission_interval = report_submission_interval;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration_word() {
        return duration_word;
    }

    public void setDuration_word(String duration_word) {
        this.duration_word = duration_word;
    }

    public String getDeviation_time() {
        return deviation_time;
    }

    public void setDeviation_time(String deviation_time) {
        this.deviation_time = deviation_time;
    }

    public String getDetails_brief() {
        return details_brief;
    }

    public void setDetails_brief(String details_brief) {
        this.details_brief = details_brief;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getProject_no() {
        return project_no;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(String approved_date) {
        this.approved_date = approved_date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
