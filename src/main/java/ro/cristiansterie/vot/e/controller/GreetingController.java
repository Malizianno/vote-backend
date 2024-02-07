package ro.cristiansterie.vot.e.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.cristiansterie.vot.e.service.GreetingService;

@RestController
@RequestMapping(path = "/")
public class GreetingController {

    private GreetingService service;

    public GreetingController(GreetingService service) {
        this.service = service;
    }

    @GetMapping("hello/{name}")
    public String hello(@PathVariable(value = "name") String name) {
        return service.sayHello(name);
    }
}
