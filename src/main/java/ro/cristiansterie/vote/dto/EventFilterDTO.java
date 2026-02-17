package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class EventFilterDTO implements Filterable<EventDTO> {
    private EventDTO event;
    private Paging paging;

    public EventDTO getObject() {
        return event;
    }

    public void setObject(EventDTO event) {
        this.event = event;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
