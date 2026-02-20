package ro.cristiansterie.vote.repository;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ActiveProfiles;

import ro.cristiansterie.vote.entity.VoteDAO;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VoteRepositoryIT {

    @Autowired
    private VoteRepository repository;

    @BeforeEach
    void cleanupDB() {
        repository.deleteAll();
    }

    @Test
    void findAll_returnsAllEntities() {
        VoteDAO vote = new VoteDAO();
        vote.setElectionId(1L);
        vote.setCandidateID(1L);
        vote.setId(1L);
        vote.setParty(PartyTypeEnum.IND);
        vote.setTimestamp(System.currentTimeMillis());

        repository.save(vote);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findFiltered_returnsEntitiesMatchingFilters() {
        VoteDAO vote1 = new VoteDAO();
        vote1.setElectionId(1L);
        vote1.setCandidateID(1L);
        vote1.setId(1L);
        vote1.setParty(PartyTypeEnum.IND);
        vote1.setTimestamp(System.currentTimeMillis());

        VoteDAO vote2 = new VoteDAO();
        vote2.setElectionId(2L);
        vote2.setCandidateID(2L);
        vote2.setId(2L);
        vote2.setParty(PartyTypeEnum.IND);
        vote2.setTimestamp(System.currentTimeMillis());

        repository.save(vote1);
        repository.save(vote2);

        VoteDAO filter = new VoteDAO();
        filter.setElectionId(1L);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        var foundEvents = repository.findAll(Example.of(filter, matcher));

        assertEquals(1, foundEvents.size());
        assertEquals(filter.getElectionId(), foundEvents.get(0).getElectionId());
    }

    @Test
    void save_returnsSavedEntity() {
        VoteDAO vote = new VoteDAO();
        vote.setElectionId(1L);
        vote.setCandidateID(1L);
        vote.setId(1L);
        vote.setParty(PartyTypeEnum.IND);
        vote.setTimestamp(System.currentTimeMillis());

        var savedVote = repository.save(vote);

        assertEquals(vote.getElectionId(), savedVote.getElectionId());
        assertEquals(vote.getCandidateID(), savedVote.getCandidateID());
        assertEquals(vote.getParty(), savedVote.getParty());
        assertEquals(vote.getTimestamp(), savedVote.getTimestamp());
    }

    @Test
    void deleteByElectionId_deletesEntitiesWithGivenElectionId() {
        VoteDAO vote1 = new VoteDAO();
        vote1.setElectionId(1L);
        vote1.setCandidateID(1L);
        vote1.setId(1L);
        vote1.setParty(PartyTypeEnum.IND);
        vote1.setTimestamp(System.currentTimeMillis());

        VoteDAO vote2 = new VoteDAO();
        vote2.setElectionId(2L);
        vote2.setCandidateID(2L);
        vote2.setId(2L);
        vote2.setParty(PartyTypeEnum.IND);
        vote2.setTimestamp(System.currentTimeMillis());

        repository.save(vote1);
        repository.save(vote2);

        repository.deleteByElectionId(1L);

        assertEquals(0, repository.findAll(Example.of(vote1)).size());
        assertEquals(1, repository.findAll(Example.of(vote2)).size());
    }
}
