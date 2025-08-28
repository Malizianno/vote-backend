package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.UserGenderEnum;
import ro.cristiansterie.vote.util.UserNationalityEnum;

public class UserVoterDTO extends UserDTO {

    private String firstname;
    private String lastname;
    private UserGenderEnum gender;
    private String idSeries;
    private Integer idNumber;
    private Long cnp;
    private UserNationalityEnum nationality;
    private String birthAddress;
    private String residenceAddress;
    private Long validityStartDate;
    private Long validityEndDate;
    private String idImage;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public UserGenderEnum getGender() {
        return gender;
    }

    public void setGender(UserGenderEnum gender) {
        this.gender = gender;
    }

    public String getIdSeries() {
        return idSeries;
    }

    public void setIdSeries(String idSeries) {
        this.idSeries = idSeries;
    }

    public Integer getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Integer idNumber) {
        this.idNumber = idNumber;
    }

    public Long getCnp() {
        return cnp;
    }

    public void setCnp(Long cnp) {
        this.cnp = cnp;
    }

    public UserNationalityEnum getNationality() {
        return nationality;
    }

    public void setNationality(UserNationalityEnum nationality) {
        this.nationality = nationality;
    }

    public String getBirthAddress() {
        return birthAddress;
    }

    public void setBirthAddress(String birthAddress) {
        this.birthAddress = birthAddress;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public Long getValidityStartDate() {
        return validityStartDate;
    }

    public void setValidityStartDate(Long validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public Long getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Long validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

}
