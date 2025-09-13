package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.ElectionCampaignDTO;
import ro.cristiansterie.vote.dto.ElectionDTO;
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

    @GetMapping(path = "/result/{electionId}")
    public ResponseEntity<CandidateDTO> result(@PathVariable int electionId) {
        CandidateDTO result = service.getElectionResult(electionId);

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/cleanDB/{electionId}")
    public ResponseEntity<Boolean> cleanDB(@PathVariable int electionId) {
        Boolean result = service.cleanAllVotes(electionId);

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/vote")
    public ResponseEntity<Boolean> vote(@RequestBody CandidateDTO candidate, @RequestParam("userID") Integer userID) {
        Boolean voted = service.vote(candidate, userID);

        return new ResponseEntity<>(voted, null != voted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/countAllVotes/{electionId}")
    public ResponseEntity<Integer> countAllVotes(@PathVariable int electionId) {
        Integer count = service.countAllVotes(electionId);

        return new ResponseEntity<>(count, null != count ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/getParsedVotes/{electionId}")
    public ResponseEntity<List<CandidateWithStatisticsDTO>> getParsedVotes(@PathVariable int electionId) {
        List<CandidateWithStatisticsDTO> result = service.getParsedVotes(electionId);

        return new ResponseEntity<>(result, null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/lastElection")
    public ResponseEntity<ElectionDTO> lastElection() {
        ElectionDTO result = service.getLastElection();

        return new ResponseEntity<>(null != result ? result : null,
                null != result ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
