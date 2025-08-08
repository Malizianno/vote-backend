package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class ElectionFilterDTO {
    private ElectionDTO election;
    private Paging paging;

    public ElectionDTO getElection() {
        return election;
    }

    public void setElection(ElectionDTO filter) {
        this.election = filter;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
