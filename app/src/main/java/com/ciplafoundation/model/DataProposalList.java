package com.ciplafoundation.model;

import java.io.Serializable;

/**
 * Created by User-129-pc on 24-03-2017.
 */

public class DataProposalList implements Serializable {

    public String getProposal_name() {
        return proposal_name;
    }

    public void setProposal_name(String proposal_name) {
        this.proposal_name = proposal_name;
    }

    public String proposal_name;

}
