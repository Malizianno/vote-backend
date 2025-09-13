package ro.cristiansterie.vote.dto;

import java.util.List;

/*
 * DTO for election campaign status
 * as in it returns if the campaign is active for certain user (default ALL for ADMINs)
 */
public class ElectionCampaignDTO {
    private Boolean enabled;
    private List<ElectionDTO> elections;

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<ElectionDTO> getElections() {
        return elections;
    }

    public void setElections(List<ElectionDTO> elections) {
        this.elections = elections;
    }
}