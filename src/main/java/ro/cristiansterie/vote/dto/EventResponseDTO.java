package ro.cristiansterie.vote.dto;

import java.util.List;

public class EventResponseDTO {
    private List<EventDTO> events;
    private Long total;

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
