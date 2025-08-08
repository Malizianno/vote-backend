package ro.cristiansterie.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.ElectionDAO;

public interface ElectionRepository extends JpaRepository<ElectionDAO, Integer> {

}
