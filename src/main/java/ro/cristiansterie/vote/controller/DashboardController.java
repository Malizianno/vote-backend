package ro.cristiansterie.vote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.service.DashboardService;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

    private DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping(path = "/totals/{electionID}")
    public ResponseEntity<DashboardTotalsDTO> totals(@PathVariable int electionID) {
        DashboardTotalsDTO totals = service.getTotals(electionID);
        return new ResponseEntity<>(totals, null != totals ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/fake/candidates/{electionId}")
    public ResponseEntity<Boolean> fakeCandidates(@PathVariable int electionId) {
        boolean status = service.generateFakeCandidates(electionId);

        return new ResponseEntity<>(status, status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/fake/users/{no}")
    public ResponseEntity<Boolean> fakeUsers(@PathVariable int no) {
        boolean status = service.generateFakeUsers(no);

        return new ResponseEntity<>(status, status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/fake/votes/{no}/{electionId}")
    public ResponseEntity<Boolean> fakeVotes(@PathVariable int no, @PathVariable int electionId) {
        Boolean status = service.generateFakeVotes(no, electionId);

        return new ResponseEntity<>(status, status != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
