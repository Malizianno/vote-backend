package ro.cristiansterie.vote.dto;

import java.util.List;

public class ElectionResponseDTO {
    private List<ElectionDTO> elections;
    private Long total;

    public List<ElectionDTO> getElections() {
        return elections;
    }

    public void setElections(List<ElectionDTO> elections) {
        this.elections = elections;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
