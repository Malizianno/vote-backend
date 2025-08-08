package ro.cristiansterie.vote.entity;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ro.cristiansterie.vote.service.GenericService;
import ro.cristiansterie.vote.util.AppConstants;

@Entity(name = AppConstants.TABLE_ELECTION)
public class ElectionDAO extends GenericService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Boolean enabled;
    private String name;
    private String description;
    private String startDate;
    private String endDate;

    @Column(columnDefinition = "json")
    private String candidates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<CandidateDAO> getCandidates() {
        try {
            return objectMapper.readValue(candidates, new TypeReference<List<CandidateDAO>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public void setCandidates(List<CandidateDAO> candidates) {
        try {
            this.candidates = objectMapper.writeValueAsString(candidates);
        } catch (JsonProcessingException e) {
            // XXX: Handle exception properly
            e.printStackTrace();
        }
    }

}