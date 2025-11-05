package ro.cristiansterie.vote.entity;

import java.util.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.UserGenderEnum;
import ro.cristiansterie.vote.util.UserNationalityEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Entity(name = AppConstants.TABLE_USER)
public class UserDAO {

    // app user type
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;
    private UserRoleEnum role;
    private Boolean hasVoted;

    // voter user type
    private String firstname;
    private String lastname;
    private UserGenderEnum gender;
    @Column(name = "id_series")
    private String idSeries;
    @Column(name = "id_number")
    private Integer idNumber;
    private Long cnp;
    private UserNationalityEnum nationality;
    @Column(name = "residence_address")
    private String residenceAddress;
    @Column(name = "validity_start_date")
    private Long validityStartDate;
    @Column(name = "validity_end_date")
    private Long validityEndDate;
    @Lob
    @Column(name = "id_image", columnDefinition = "LONGBLOB")
    private byte[] idImage;
    @Lob
    @Column(name = "face_image", columnDefinition = "LONGBLOB")
    private byte[] faceImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

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

    public byte[] getIdImage() {
        return idImage;
    }

    public String getBase64IdImage() {
        return Base64.getEncoder().encodeToString(this.idImage);
    }

    public void setIdImage(byte[] idImage) {
        this.idImage = idImage;
    }

    public void setBase64IdImage(String base64IdImage) {
        this.idImage = Base64.getDecoder().decode(base64IdImage);
    }

    public byte[] getFaceImage() {
        return faceImage;
    }

    public String getBase64FaceImage() {
        return Base64.getEncoder().encodeToString(this.faceImage);
    }

    public void setFaceImage(byte[] faceImage) {
        this.faceImage = faceImage;
    }

    public void setBase64FaceImage(String base64FaceImage) {
        this.faceImage = Base64.getDecoder().decode(base64FaceImage);
    }
}
