package ro.cristiansterie.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.CandidateDAO;

public interface CandidateRepository extends JpaRepository<CandidateDAO, Integer> {

}
