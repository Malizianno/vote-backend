package ro.cristiansterie.vote.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.LoginRequestDTO;
import ro.cristiansterie.vote.dto.LoginResponseDTO;
import ro.cristiansterie.vote.dto.LogoutRequestDTO;
import ro.cristiansterie.vote.dto.LogoutResponseDTO;
import ro.cristiansterie.vote.service.LoginService;

@RestController
@RequestMapping(path = "/login")
public class LoginController {

    private LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping(path = "/", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = service.login(dto);

        return new ResponseEntity<>(response, null != response ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/logout", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO dto) {
        LogoutResponseDTO response = service.logout(dto);

        return new ResponseEntity<>(response, null != response ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
