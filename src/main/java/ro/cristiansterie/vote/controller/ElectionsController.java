package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.service.ElectionsService;

@RestController
@RequestMapping(path = "/election")
public class ElectionsController {

    private ElectionsService service;

    public ElectionsController(ElectionsService service) {
        this.service = service;
    }

    @GetMapping(path = "/status")
    public ResponseEntity<Boolean> status() {
        Boolean status = service.getElectionCampaignStatus();

        return new ResponseEntity<>(status, null != status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/switchStatus")
    public ResponseEntity<Boolean> switchStatus() {
        Boolean switched = service.switchElectionCampaignStatus();

        return new ResponseEntity<>(switched, null != switched ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/result")
    public ResponseEntity<CandidateDTO> result() {
        CandidateDTO result = service.getElectionResult();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/cleanDB")
    public ResponseEntity<Boolean> cleanDB() {
        Boolean result = service.cleanAllVotes();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Boolean> vote(@RequestBody CandidateDTO candidate) {
        Boolean voted = service.vote(candidate);

        return new ResponseEntity<>(voted, null != voted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/countAllVotes")
    public ResponseEntity<Integer> countAllVotes() {
        Integer count = service.countAllVotes();

        return new ResponseEntity<>(count, null != count ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/getParsedVotes")
    public ResponseEntity<List<CandidateWithStatisticsDTO>> getParsedVotes() {
        List<CandidateWithStatisticsDTO> result = service.getParsedVotes();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
