package ro.cristiansterie.vote.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.entity.UserDAO;
import ro.cristiansterie.vote.repository.UserRepository;

@Service
public class UserService {

    private UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserDAO save(UserDAO user) {
        return null != user ? repo.save(user) : null;
    }

    public List<UserDAO> getAll() {
        return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
