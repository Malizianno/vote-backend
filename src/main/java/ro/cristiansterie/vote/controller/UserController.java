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

import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;
import ro.cristiansterie.vote.dto.UserResponseDTO;
import ro.cristiansterie.vote.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> one(@PathVariable int id) {
        UserDTO user = service.get(id);
        return new ResponseEntity<>(user, null != user ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<UserDTO>> all() {
        List<UserDTO> users = service.getAll();

        return new ResponseEntity<>(users, null != users ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/filtered")
    public ResponseEntity<UserResponseDTO> filtered(@RequestBody UserFilterDTO filter) {
        UserResponseDTO response = new UserResponseDTO();

        response.setUsers(service.getFiltered(filter));
        response.setTotal(service.countFiltered(filter));

        return new ResponseEntity<>(response, null != response.getUsers() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO user) {
        UserDTO saved = service.save(user);
        return new ResponseEntity<>(saved, null != saved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        Boolean deleted = service.delete(id);
        return new ResponseEntity<>(deleted, null != deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
