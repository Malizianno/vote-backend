package ro.cristiansterie.vote.dto;

import java.util.List;

public class CandidateResponseDTO {
    private List<CandidateDTO> candidates;
    private int total;

    public List<CandidateDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
