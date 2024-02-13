package ro.cristiansterie.vote.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    
    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(path = "/save")
    public @ResponseBody UserDAO save(@RequestParam String name) {
        UserDAO newUser = new UserDAO();
        newUser.setUsername(name);

        return service.save(newUser);
    }

    @GetMapping(path = "all")
    public @ResponseBody List<UserDAO> all() {
        return service.getAll();
    }
}
