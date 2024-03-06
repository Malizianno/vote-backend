package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class CandidateFilterDTO {
    private CandidateDTO candidate;
    private Paging paging;

    public CandidateDTO getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateDTO filter) {
        this.candidate = filter;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
