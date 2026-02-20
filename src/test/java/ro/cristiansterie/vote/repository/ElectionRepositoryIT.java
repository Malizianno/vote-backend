package ro.cristiansterie.vote.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import ro.cristiansterie.vote.entity.ElectionDAO;
import ro.cristiansterie.vote.util.Paging;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ElectionRepositoryIT {

    @Autowired
    private ElectionRepository repository;

    @Test
    void findAll_returnsAllEntities() {
        assertNotNull(repository.findAll());
    }

    @Test
    void findFiltered_returnsEntitiesMatchingFilters() {
        Paging paging = new Paging();
        ElectionDAO election1 = new ElectionDAO();
        election1.setName("Election 1");
        election1.setDescription("Description 1");
        election1.setEnabled(true);
        election1.setCandidates(new ArrayList<>());

        ElectionDAO election2 = new ElectionDAO();
        election2.setName("Election 2");
        election2.setDescription("Description 2");
        election2.setEnabled(true);
        election2.setCandidates(new ArrayList<>());

        ElectionDAO election3 = new ElectionDAO();
        election3.setName("Election 3");
        election3.setDescription("Description 3");
        election3.setEnabled(false);
        election3.setCandidates(new ArrayList<>());

        repository.save(election1);
        repository.save(election2);
        repository.save(election3);

        ElectionDAO filter = new ElectionDAO();
        filter.setEnabled(true);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("candidates", "startDate", "endDate");
        Pageable pageable = PageRequest.of(paging.getPage(), paging.getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(2, repository.findAll(Example.of(filter, matcher), pageable).getTotalElements());
    }

    @Test
    void count_filtered_returnsCountOfEntitiesMatchingFilters() {
        ElectionDAO election1 = new ElectionDAO();
        election1.setName("Election 1");
        election1.setDescription("Description 1");
        election1.setEnabled(true);
        election1.setCandidates(new ArrayList<>());

        ElectionDAO election2 = new ElectionDAO();
        election2.setName("Election 2");
        election2.setDescription("Description 2");
        election2.setEnabled(true);
        election2.setCandidates(new ArrayList<>());

        ElectionDAO election3 = new ElectionDAO();
        election3.setName("Election 3");
        election3.setDescription("Description 3");
        election3.setEnabled(false);
        election3.setCandidates(new ArrayList<>());

        repository.save(election1);
        repository.save(election2);
        repository.save(election3);

        ElectionDAO filter = new ElectionDAO();
        filter.setEnabled(true);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("candidates", "startDate", "endDate");

        assertEquals(2, repository.count(Example.of(filter, matcher)));
    }

    @Test
    void findFirstByEnabledTrueOrderByStartDateDesc_returnsMostRecentEnabledElection() {
        ElectionDAO election1 = new ElectionDAO();
        election1.setName("Election 1");
        election1.setDescription("Description 1");
        election1.setEnabled(true);
        election1.setStartDate(LocalDate.now().minusDays(10).toString());
        election1.setCandidates(new ArrayList<>());

        ElectionDAO election2 = new ElectionDAO();
        election2.setName("Election 2");
        election2.setDescription("Description 2");
        election2.setEnabled(true);
        election2.setStartDate(LocalDate.now().minusDays(5).toString());
        election2.setCandidates(new ArrayList<>());

        ElectionDAO election3 = new ElectionDAO();
        election3.setName("Election 3");
        election3.setDescription("Description 3");
        election3.setEnabled(false);
        election3.setStartDate(LocalDate.now().toString());
        election3.setCandidates(new ArrayList<>());

        repository.save(election1);
        repository.save(election2);
        repository.save(election3);

        var found = repository.findFirstByEnabledTrueOrderByStartDateDesc();

        assertNotNull(found);
        assertEquals(election2.getId(), found.get().getId());
    }

    @Test
    void findById_returnsEntityForGivenId() {
        ElectionDAO election = new ElectionDAO();
        election.setName("Election 1");
        election.setDescription("Description 1");
        election.setEnabled(true);

        repository.save(election);

        assertNotNull(repository.findById(election.getId()));
    }

    @Test
    void save_persistsEntity() {
        ElectionDAO election = new ElectionDAO();
        election.setName("Election 1");
        election.setDescription("Description 1");
        election.setEnabled(true);

        ElectionDAO saved = repository.save(election);

        assertNotNull(saved.getId());
        assertEquals("Election 1", saved.getName());
        assertEquals("Description 1", saved.getDescription());
    }

    @Test
    void deleteById_removesEntity() {
        ElectionDAO election = new ElectionDAO();
        election.setName("Election 1");
        election.setDescription("Description 1");
        election.setEnabled(true);
        election.setCandidates(new ArrayList<>());

        ElectionDAO saved = repository.save(election);

        repository.deleteById(saved.getId());

        assertEquals(0, repository.count());
    }
}
