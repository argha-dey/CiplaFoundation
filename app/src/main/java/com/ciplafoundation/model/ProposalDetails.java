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
    private String proposal_no;


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
}
