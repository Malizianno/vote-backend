package ro.cristiansterie.vote.dto;

import java.util.List;

public class EventResponseDTO {
    private List<EventDTO> events;
    private int total;

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
