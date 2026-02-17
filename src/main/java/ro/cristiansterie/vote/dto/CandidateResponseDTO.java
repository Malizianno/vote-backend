package ro.cristiansterie.vote.dto;

import java.util.List;

public class CandidateResponseDTO {
    private List<CandidateDTO> candidates;
    private Long total;

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
