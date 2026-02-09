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

import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.dto.ElectionResponseDTO;
import ro.cristiansterie.vote.service.ElectionService;

@RestController
@RequestMapping(path = "/election")
public class ElectionsController {

    private ElectionService service;

    public ElectionsController(ElectionService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ElectionDTO> one(@PathVariable int id) {
        ElectionDTO election = service.get(id);
        return new ResponseEntity<>(election, null != election ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/last")
    public ResponseEntity<ElectionDTO> getLastActive() {
        ElectionDTO result = service.getLastActiveElection();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ElectionDTO>> all() {
        List<ElectionDTO> elections = service.getAll();
        return new ResponseEntity<>(elections, null != elections ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/filtered")
    public ResponseEntity<ElectionResponseDTO> filtered(@RequestBody ElectionFilterDTO filter) {
        ElectionResponseDTO response = new ElectionResponseDTO();

        response.setElections(service.getFiltered(filter));
        response.setTotal(service.countFiltered(filter));

        return new ResponseEntity<>(response,
                null != response.getElections() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<ElectionDTO> add(@RequestBody ElectionDTO toSave) {
        ElectionDTO saved = service.save(toSave);
        return new ResponseEntity<>(saved, null != saved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        Boolean deleted = service.delete(id);
        return new ResponseEntity<>(deleted, null != deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/changeStatus/{id}/{enabled}")
    public ResponseEntity<Boolean> changeStatus(@PathVariable int id, @PathVariable boolean enabled) {
        Boolean changed = service.changeStatus(id, enabled);
        return new ResponseEntity<>(changed, null != changed ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
