package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.dto.NewsfeedPostFilterDTO;
import ro.cristiansterie.vote.dto.NewsfeedResponseDTO;
import ro.cristiansterie.vote.service.NewsfeedService;

@RestController
@RequestMapping(path = "/newsfeed")
public class NewsfeedController {

    private final NewsfeedService service;

    public NewsfeedController(NewsfeedService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<NewsfeedPostDTO> getById(@PathVariable Long id) {
        NewsfeedPostDTO post = service.findById(id);
        return new ResponseEntity<>(post, null != post ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @Deprecated
    @GetMapping(path = "/all")
    public ResponseEntity<List<NewsfeedPostDTO>> all() {
        List<NewsfeedPostDTO> posts = service.findAll();
        return new ResponseEntity<>(posts, null != posts ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/filtered")
    public ResponseEntity<NewsfeedResponseDTO> filtered(@RequestBody NewsfeedPostFilterDTO filter) {
        NewsfeedResponseDTO response = new NewsfeedResponseDTO();

        response.setPosts(service.findFiltered(filter));
        response.setTotal(service.countFiltered(filter));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<NewsfeedPostDTO> add(@RequestBody NewsfeedPostDTO post) {
        NewsfeedPostDTO saved = service.create(post);
        return new ResponseEntity<>(saved, null != saved ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<NewsfeedPostDTO> update(@PathVariable Long id, @RequestBody NewsfeedPostDTO post) {
        NewsfeedPostDTO updated = service.update(id, post);
        return new ResponseEntity<>(updated, null != updated ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
