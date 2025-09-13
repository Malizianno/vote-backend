package ro.cristiansterie.vote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.CandidateDAO;

public interface CandidateRepository extends JpaRepository<CandidateDAO, Integer> {
    List<CandidateDAO> findAllByElectionId(Integer electionId);

    int countByElectionId(Integer electionId);

    void deleteByElectionId(Integer electionId);
}
