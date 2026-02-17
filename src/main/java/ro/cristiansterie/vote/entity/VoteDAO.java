package ro.cristiansterie.vote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@Entity(name = AppConstants.TABLE_VOTE)
public class VoteDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "candidate")
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
}
