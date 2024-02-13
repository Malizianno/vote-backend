package ro.cristiansterie.vote.repository;

import org.springframework.data.repository.CrudRepository;

import ro.cristiansterie.vote.entity.UserDAO;

public interface UserRepository extends CrudRepository<UserDAO, Integer> {
}
