package ro.cristiansterie.vote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.CandidateDAO;

public interface CandidateRepository extends JpaRepository<CandidateDAO, Long> {
    List<CandidateDAO> findAllByElectionId(Long electionId);

    int countByElectionId(Long electionId);

    void deleteByElectionId(Long electionId);
}
