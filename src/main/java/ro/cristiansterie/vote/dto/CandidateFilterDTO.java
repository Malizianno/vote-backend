package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class CandidateFilterDTO implements Filterable<CandidateDTO> {
    private CandidateDTO candidate;
    private Paging paging;

    public CandidateDTO getObject() {
        return candidate;
    }

    public void setObject(CandidateDTO filter) {
        this.candidate = filter;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
