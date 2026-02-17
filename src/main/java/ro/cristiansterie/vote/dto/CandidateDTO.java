package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.PartyTypeEnum;

public class CandidateDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private PartyTypeEnum party;

    private String image;
    private String description;

    private Long electionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PartyTypeEnum getParty() {
        return party;
    }

    public void setParty(PartyTypeEnum party) {
        this.party = party;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    @Override
    public String toString() {
        return "CandidateDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", party=" + party
                + ", image=" + image + ", description=" + description + ", electionId=" + electionId + "]";
    }
}
