package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.EventDTO;
import ro.cristiansterie.vote.dto.EventFilterDTO;
import ro.cristiansterie.vote.dto.EventResponseDTO;
import ro.cristiansterie.vote.service.EventService;

@RestController
@RequestMapping(path = "/events")
public class EventController {

    private EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventDTO> one(@PathVariable int id) {
        EventDTO event = service.get(id);
        return new ResponseEntity<>(event, null != event ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<EventDTO>> all() {
        List<EventDTO> events = service.getAll();

        return new ResponseEntity<>(events, null != events ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/last10")
    public ResponseEntity<List<EventDTO>> last10() {
        List<EventDTO> events = service.getLast10();

        return new ResponseEntity<>(events, null != events ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/filtered")
    public ResponseEntity<EventResponseDTO> filtered(@RequestBody EventFilterDTO filter) {
        EventResponseDTO response = new EventResponseDTO();

        // set response data
        response.setEvents(service.getFiltered(filter));
        response.setTotal(service.countFiltered(filter));

        return new ResponseEntity<>(response, null != response.getEvents() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<EventDTO> save(@RequestBody EventDTO event) {
        EventDTO saved = service.save(event);
        return new ResponseEntity<>(saved, null != saved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        Boolean deleted = service.delete(id);
        return new ResponseEntity<>(deleted, null != deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
