package ro.cristiansterie.vote.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CandidateRepositoryIT {

    @Autowired
    private CandidateRepository repository;

    @Test
    void findAll_returnsAllEntities() {
        CandidateDAO candidate1 = new CandidateDAO();
        candidate1.setFirstName("Firstname1");
        candidate1.setLastName("lastname1");
        candidate1.setDescription("description1");
        candidate1.setImage("image1");
        candidate1.setElectionId(1L);
        candidate1.setParty(PartyTypeEnum.IND);

        CandidateDAO candidate2 = new CandidateDAO();
        candidate2.setFirstName("Firstname2");
        candidate2.setLastName("lastname2");
        candidate2.setDescription("description2");
        candidate2.setImage("image2");
        candidate2.setElectionId(1L);
        candidate2.setParty(PartyTypeEnum.IND);

        repository.save(candidate1);
        repository.save(candidate2);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void finaAllByElectionId_returnsEntitiesForGivenElection() {
        CandidateDAO candidate1 = new CandidateDAO();
        candidate1.setFirstName("Firstname1");
        candidate1.setLastName("lastname1");
        candidate1.setDescription("description1");
        candidate1.setImage("image1");
        candidate1.setElectionId(1L);
        candidate1.setParty(PartyTypeEnum.IND);

        CandidateDAO candidate2 = new CandidateDAO();
        candidate2.setFirstName("Firstname2");
        candidate2.setLastName("lastname2");
        candidate2.setDescription("description2");
        candidate2.setImage("image2");
        candidate2.setElectionId(2L);
        candidate2.setParty(PartyTypeEnum.IND);

        repository.save(candidate1);
        repository.save(candidate2);

        assertEquals(1, repository.findAllByElectionId(1L).size());
    }

    @Test
    void count_returnsNumberOfEntities() {
        CandidateDAO candidate1 = new CandidateDAO();
        candidate1.setFirstName("Firstname1");
        candidate1.setLastName("lastname1");
        candidate1.setDescription("description1");
        candidate1.setImage("image1");
        candidate1.setElectionId(1L);
        candidate1.setParty(PartyTypeEnum.IND);

        CandidateDAO candidate2 = new CandidateDAO();
        candidate2.setFirstName("Firstname2");
        candidate2.setLastName("lastname2");
        candidate2.setDescription("description2");
        candidate2.setImage("image2");
        candidate2.setElectionId(1L);
        candidate2.setParty(PartyTypeEnum.IND);

        repository.save(candidate1);
        repository.save(candidate2);

        assertEquals(2, repository.count());
    }

    @Test
    void countAllByElectionId_returnsNumberOfEntitiesForGivenElection() {
        CandidateDAO candidate1 = new CandidateDAO();
        candidate1.setFirstName("Firstname1");
        candidate1.setLastName("lastname1");
        candidate1.setDescription("description1");
        candidate1.setImage("image1");
        candidate1.setElectionId(1L);
        candidate1.setParty(PartyTypeEnum.IND);

        CandidateDAO candidate2 = new CandidateDAO();
        candidate2.setFirstName("Firstname2");
        candidate2.setLastName("lastname2");
        candidate2.setDescription("description2");
        candidate2.setImage("image2");
        candidate2.setElectionId(1L);
        candidate2.setParty(PartyTypeEnum.IND);

        repository.save(candidate1);
        repository.save(candidate2);

        assertEquals(2, repository.countByElectionId(1L));
    }

    @Test
    void findById_returnsEntityIfExists() {
        CandidateDAO candidate = new CandidateDAO();
        candidate.setFirstName("Firstname");
        candidate.setLastName("lastname");
        candidate.setDescription("description");
        candidate.setImage("image");
        candidate.setElectionId(1L);
        candidate.setParty(PartyTypeEnum.IND);

        CandidateDAO saved = repository.save(candidate);

        CandidateDAO found = repository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getFirstName(), found.getFirstName());
    }

    @Test
    void save_persistsEntity() {
        CandidateDAO candidate = new CandidateDAO();
        candidate.setFirstName("Firstname");
        candidate.setLastName("lastname");
        candidate.setDescription("description");
        candidate.setImage("image");
        candidate.setElectionId(1L);
        candidate.setParty(PartyTypeEnum.IND);

        CandidateDAO saved = repository.save(candidate);

        assertNotNull(saved.getId());
        assertEquals("Firstname", saved.getFirstName());
        assertEquals("lastname", saved.getLastName());
    }

    @Test
    void deleteById_removesEntity() {
        CandidateDAO candidate = new CandidateDAO();
        candidate.setFirstName("Firstname");
        candidate.setLastName("lastname");
        candidate.setDescription("description");
        candidate.setImage("image");
        candidate.setElectionId(1L);
        candidate.setParty(PartyTypeEnum.IND);

        CandidateDAO saved = repository.save(candidate);
        Long id = saved.getId();

        repository.deleteById(id);

        assertEquals(0, repository.count());
    }

    @Test
    void deleteByElectionId_removesEntitiesForGivenElection() {
        CandidateDAO candidate1 = new CandidateDAO();
        candidate1.setFirstName("Firstname1");
        candidate1.setLastName("lastname1");
        candidate1.setDescription("description1");
        candidate1.setImage("image1");
        candidate1.setElectionId(1L);
        candidate1.setParty(PartyTypeEnum.IND);

        CandidateDAO candidate2 = new CandidateDAO();
        candidate2.setFirstName("Firstname2");
        candidate2.setLastName("lastname2");
        candidate2.setDescription("description2");
        candidate2.setImage("image2");
        candidate2.setElectionId(1L);
        candidate2.setParty(PartyTypeEnum.IND);

        CandidateDAO candidate3 = new CandidateDAO();
        candidate3.setFirstName("Firstname3");
        candidate3.setLastName("lastname3");
        candidate3.setDescription("description3");
        candidate3.setImage("image3");
        candidate3.setElectionId(2L);
        candidate3.setParty(PartyTypeEnum.IND);

        repository.save(candidate1);
        repository.save(candidate2);
        repository.save(candidate3);

        assertEquals(3, repository.count());

        repository.deleteByElectionId(1L);

        assertEquals(1, repository.count());
    }
}
