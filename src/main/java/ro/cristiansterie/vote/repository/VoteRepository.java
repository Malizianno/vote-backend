package ro.cristiansterie.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.VoteDAO;

public interface VoteRepository extends JpaRepository<VoteDAO, Long> {
    void deleteByElectionId(Long electionId);
}
