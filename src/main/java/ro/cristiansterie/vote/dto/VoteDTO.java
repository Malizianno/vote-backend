package ro.cristiansterie.vote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.cristiansterie.vote.util.PartyTypeEnum;

public class VoteDTO {

    @JsonIgnore
    private Integer id;

    private Integer candidateID;
    private PartyTypeEnum party;
    private Long timestamp;
    private Integer electionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(Integer candidateID) {
        this.candidateID = candidateID;
    }

    public PartyTypeEnum getParty() {
        return party;
    }

    public void setParty(PartyTypeEnum party) {
        this.party = party;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public void setElectionId(Integer electionId) {
        this.electionId = electionId;
    }

    @Override
    public String toString() {
        return "VoteDTO [id=" + id + ", candidateID=" + candidateID + ", timestamp=" + timestamp + ", electionId="
                + electionId + "]";
    }
}
