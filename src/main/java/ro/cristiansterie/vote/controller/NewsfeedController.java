package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.service.NewsfeedService;

@RestController
@RequestMapping(path = "/newsfeed")
public class NewsfeedController {

    private final NewsfeedService service;

    public NewsfeedController(NewsfeedService service) {
        this.service = service;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<NewsfeedPostDTO>> all() {
        List<NewsfeedPostDTO> posts = service.findAll();
        return new ResponseEntity<>(posts, null != posts ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<NewsfeedPostDTO> add(NewsfeedPostDTO post) {
        NewsfeedPostDTO saved = service.create(service.convert(post));
        return new ResponseEntity<>(saved, null != saved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<NewsfeedPostDTO> update(@PathVariable Long id, NewsfeedPostDTO post) {
        NewsfeedPostDTO updated = service.update(id, service.convert(post));
        return new ResponseEntity<>(updated, null != updated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
