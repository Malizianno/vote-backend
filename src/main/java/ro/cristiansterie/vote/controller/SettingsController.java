package ro.cristiansterie.vote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.service.SettingsService;

@RestController
@RequestMapping(path = "/settings")
public class SettingsController {

    private SettingsService service;

    public SettingsController(SettingsService service) {
        this.service = service;
    }

    @PostMapping(path = "/fake/candidates/{electionId}")
    public ResponseEntity<Boolean> fakeCandidates(@PathVariable Long electionId) {
        boolean status = service.generateFakeCandidates(electionId);

        return new ResponseEntity<>(status, status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/fake/users/{no}")
    public ResponseEntity<Boolean> fakeUsers(@PathVariable int no) {
        boolean status = service.generateFakeUsers(no);

        return new ResponseEntity<>(status, status ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/fake/votes/{no}/{electionId}")
    public ResponseEntity<Boolean> fakeVotes(@PathVariable int no, @PathVariable Long electionId) {
        Boolean status = service.generateFakeVotes(no, electionId);

        return new ResponseEntity<>(status, status != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
