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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.dto.ElectionResponseDTO;
import ro.cristiansterie.vote.service.ElectionService;
import ro.cristiansterie.vote.service.ElectionsHelperService;

@RestController
@RequestMapping(path = "/election")
public class ElectionsController {

    private ElectionsHelperService helper;
    private ElectionService service;

    public ElectionsController(ElectionsHelperService helper, ElectionService service) {
        this.helper = helper;
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ElectionDTO> one(@PathVariable int id) {
        ElectionDTO election = service.get(id);
        return new ResponseEntity<>(election, null != election ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
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

    // WIP: helper area starts here

    @GetMapping(path = "/status")
    public ResponseEntity<Boolean> status() {
        Boolean status = helper.getElectionCampaignStatus();

        return new ResponseEntity<>(status, null != status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/switchStatus")
    public ResponseEntity<Boolean> switchStatus() {
        Boolean switched = helper.switchElectionCampaignStatus();

        return new ResponseEntity<>(switched, null != switched ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/result")
    public ResponseEntity<CandidateDTO> result() {
        CandidateDTO result = helper.getElectionResult();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/cleanDB")
    public ResponseEntity<Boolean> cleanDB() {
        Boolean result = helper.cleanAllVotes();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Boolean> vote(@RequestBody CandidateDTO candidate, @RequestParam("userID") Integer userID) {
        Boolean voted = helper.vote(candidate, userID);

        return new ResponseEntity<>(voted, null != voted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/countAllVotes")
    public ResponseEntity<Integer> countAllVotes() {
        Integer count = helper.countAllVotes();

        return new ResponseEntity<>(count, null != count ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/getParsedVotes")
    public ResponseEntity<List<CandidateWithStatisticsDTO>> getParsedVotes() {
        List<CandidateWithStatisticsDTO> result = helper.getParsedVotes();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
