package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class ElectionFilterDTO implements Filterable<ElectionDTO> {
    private ElectionDTO election;
    private Paging paging;

    public ElectionDTO getElection() {
        return getObject();
    }

    public void setElection(ElectionDTO election) {
        setObject(election);
    }

    public ElectionDTO getObject() {
        return election;
    }

    public void setObject(ElectionDTO filter) {
        this.election = filter;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
