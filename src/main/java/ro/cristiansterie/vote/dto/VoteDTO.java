package ro.cristiansterie.vote.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.cristiansterie.vote.util.PartyTypeEnum;

public class VoteDTO {

    @JsonIgnore
    private Long id;

    private Long candidateID;
    private PartyTypeEnum party;
    private Long timestamp;
    private Long electionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(Long candidateID) {
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

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    @Override
    public String toString() {
        return "VoteDTO [id=" + id + ", candidateID=" + candidateID + ", timestamp=" + timestamp + ", electionId="
                + electionId + "]";
    }
}
