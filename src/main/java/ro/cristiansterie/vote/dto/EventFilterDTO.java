package ro.cristiansterie.vote.dto;

import ro.cristiansterie.vote.util.Paging;

public class EventFilterDTO {
    private EventDTO event;
    private Paging paging;

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }
}
