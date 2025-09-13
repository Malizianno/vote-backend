package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.ElectionCampaignDTO;
import ro.cristiansterie.vote.service.ElectionsHelperService;

@RestController
@RequestMapping(path = "/election/helper")
public class ElectionHelperController {

    private final ElectionsHelperService service;

    public ElectionHelperController(ElectionsHelperService service) {
        this.service = service;
    }

    @GetMapping(path = "/status")
    public ResponseEntity<ElectionCampaignDTO> status() {
        var result = service.getElectionCampaignStatus();

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<Boolean> vote(@RequestBody CandidateDTO candidate, @RequestParam("userID") Integer userID) {
        Boolean voted = service.vote(candidate, userID);

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
