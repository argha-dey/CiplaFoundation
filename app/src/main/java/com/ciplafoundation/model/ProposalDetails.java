package com.ciplafoundation.model;

import java.io.Serializable;

/**
 * Created by User-66-pc on 4/4/2017.
 */

public class ProposalDetails implements Serializable{

    private static final long serialVersionUID = 7L;

    private String schedule_vii;
    private String ngo_type;
    private String ngo_vendor;
    private String duration_funding;
    private String title;
    private String details;
    private String timeline;
    private String duration;
    private String duration_word;
    private String fund_requested;
    private String district;
    private String block;
    private String village;
    private String location;
    private String distance;
    private String profile_benificiary;
    private String rationale_project;

    public String getApprovalMessage() {
        return approvalMessage;
    }

    public void setApprovalMessage(String approvalMessage) {
        this.approvalMessage = approvalMessage;
    }

    private String approvalMessage;

    public String getProposal_id() {
        return proposal_id;
    }

    public void setProposal_id(String proposal_id) {
        this.proposal_id = proposal_id;
    }

    private String proposal_id;
    private String view_fund_distribution;
    private String proposal_no;
    private String quarter_qise_distribution;
    private String deliverable_wise_distribution;
    private  String download_track;

    public boolean getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(boolean approval_status) {
        this.approval_status = approval_status;
    }

    private boolean approval_status;

    public String getApproved_track() {
        return approved_track;
    }

    public void setApproved_track(String approved_track) {
        this.approved_track = approved_track;
    }

    public String getDownload_track() {
        return download_track;
    }

    public void setDownload_track(String download_track) {
        this.download_track = download_track;
    }

    private String approved_track;

    public String getSchedule_vii() {
        return schedule_vii;
    }

    public void setSchedule_vii(String schedule_vii) {
        this.schedule_vii = schedule_vii;
    }

    public String getNgo_type() {
        return ngo_type;
    }

    public void setNgo_type(String ngo_type) {
        this.ngo_type = ngo_type;
    }

    public String getNgo_vendor() {
        return ngo_vendor;
    }

    public void setNgo_vendor(String ngo_vendor) {
        this.ngo_vendor = ngo_vendor;
    }

    public String getDuration_funding() {
        return duration_funding;
    }

    public void setDuration_funding(String duration_funding) {
        this.duration_funding = duration_funding;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public String getFund_requested() {
        return fund_requested;
    }

    public void setFund_requested(String fund_requested) {
        this.fund_requested = fund_requested;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getProfile_benificiary() {
        return profile_benificiary;
    }

    public void setProfile_benificiary(String profile_benificiary) {
        this.profile_benificiary = profile_benificiary;
    }

    public String getRationale_project() {
        return rationale_project;
    }

    public void setRationale_project(String rationale_project) {
        this.rationale_project = rationale_project;
    }

    public String getProposal_no() {
        return proposal_no;
    }

    public void setProposal_no(String proposal_no) {
        this.proposal_no = proposal_no;
    }

    public String getQuarter_qise_distribution() {
        return quarter_qise_distribution;
    }

    public void setQuarter_qise_distribution(String quarter_qise_distribution) {
        this.quarter_qise_distribution = quarter_qise_distribution;
    }

    public String getDeliverable_wise_distribution() {
        return deliverable_wise_distribution;
    }

    public void setDeliverable_wise_distribution(String deliverable_wise_distribution) {
        this.deliverable_wise_distribution = deliverable_wise_distribution;
    }
    public String getView_fund_distribution() {
        return view_fund_distribution;
    }

    public void setView_fund_distribution(String view_fund_distribution) {
        this.view_fund_distribution = view_fund_distribution;
    }
}
