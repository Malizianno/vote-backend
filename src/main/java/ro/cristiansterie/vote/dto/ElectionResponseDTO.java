package ro.cristiansterie.vote.dto;

import java.util.List;

public class ElectionResponseDTO {
    private List<ElectionDTO> elections;
    private int total;

    public List<ElectionDTO> getElections() {
        return elections;
    }

    public void setElections(List<ElectionDTO> elections) {
        this.elections = elections;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
