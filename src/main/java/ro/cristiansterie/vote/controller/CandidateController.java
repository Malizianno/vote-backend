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

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.dto.CandidateResponseDTO;
import ro.cristiansterie.vote.service.CandidateService;

@RestController
@RequestMapping(path = "/candidates")
public class CandidateController {

    private CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CandidateDTO> one(@PathVariable int id) {
        CandidateDTO candidate = service.get(id);
        return new ResponseEntity<>(candidate, null != candidate ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<CandidateDTO>> all() {
        List<CandidateDTO> candidates = service.getAll();
        return new ResponseEntity<>(candidates, null != candidates ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/filtered")
    public ResponseEntity<CandidateResponseDTO> filtered(@RequestBody CandidateFilterDTO filter) {
        CandidateResponseDTO response = new CandidateResponseDTO();

        response.setCandidates(service.getFiltered(filter));
        response.setTotal(service.countFiltered(filter));
        
        return new ResponseEntity<>(response, null != response.getCandidates() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<CandidateDTO> add(@RequestBody CandidateDTO toSave) {
        CandidateDTO saved = service.save(toSave);
        return new ResponseEntity<>(saved, null != saved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        Boolean deleted = service.delete(id);
        return new ResponseEntity<>(deleted, null != deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
