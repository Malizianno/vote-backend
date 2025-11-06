package ro.cristiansterie.vote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.service.UserService;

@RestController
@RequestMapping(path = "/register")
public class RegistrationController {

    private final UserService service;

    public RegistrationController(UserService service) {
        this.service = service;
    }

    @PostMapping(path = "/", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserDTO> registerProfile(@RequestBody UserVoterDTO user) {
        UserDTO registered = service.registerProfile(user);
        return new ResponseEntity<>(registered, null != registered ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
