package ro.cristiansterie.vote.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

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

import ro.cristiansterie.vote.entity.NewsfeedPostDAO;
import ro.cristiansterie.vote.util.Paging;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NewsfeedRepositoryIT {

    @Autowired
    private NewsfeedRepository repository;

    @Test
    void findAll_returnsAllEntities() {
        NewsfeedPostDAO post = new NewsfeedPostDAO();
        post.setTitle("title");
        post.setContent("content");
        post.setImageUrl("image");
        post.setElectionId(1L);

        var savedEvent = repository.save(post);
        assertNotNull(savedEvent);

        List<NewsfeedPostDAO> allEvents = repository.findAll();

        assertNotNull(allEvents);
        assertEquals(1, allEvents.size());
    }

    @Test
    void findFiltered_returnsEntitiesMatchingFilters() {
        NewsfeedPostDAO post1 = new NewsfeedPostDAO();
        post1.setTitle("title");
        post1.setContent("content");
        post1.setImageUrl("image");
        post1.setElectionId(1L);

        NewsfeedPostDAO post2 = new NewsfeedPostDAO();
        post2.setTitle("title2");
        post2.setContent("content2");
        post2.setImageUrl("image2");
        post2.setElectionId(2L);

        var savedEvent1 = repository.save(post1);
        assertNotNull(savedEvent1);

        var savedEvent2 = repository.save(post2);
        assertNotNull(savedEvent2);

        Paging paging = new Paging();
        NewsfeedPostDAO filter = new NewsfeedPostDAO();
        filter.setElectionId(1L);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(paging.getPage(), paging.getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        assertEquals(1, repository.findAll(Example.of(filter, matcher), pageable).getNumberOfElements());
    }

    @Test
    void count_filtered_returnsCountOfEntitiesMatchingFilters() {
        NewsfeedPostDAO post1 = new NewsfeedPostDAO();
        post1.setTitle("title");
        post1.setContent("content");
        post1.setImageUrl("image");
        post1.setElectionId(1L);

        NewsfeedPostDAO post2 = new NewsfeedPostDAO();
        post2.setTitle("title2");
        post2.setContent("content2");
        post2.setImageUrl("image2");
        post2.setElectionId(2L);

        var savedEvent1 = repository.save(post1);
        assertNotNull(savedEvent1);

        var savedEvent2 = repository.save(post2);
        assertNotNull(savedEvent2);

        NewsfeedPostDAO filter = new NewsfeedPostDAO();
        filter.setElectionId(1L);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        assertEquals(1, repository.count(Example.of(filter, matcher)));
    }

    @Test
    void findById_returnsEntityWithGivenId() {
        NewsfeedPostDAO post = new NewsfeedPostDAO();
        post.setTitle("title");
        post.setContent("content");
        post.setImageUrl("image");
        post.setElectionId(1L);

        var savedEvent = repository.save(post);
        assertNotNull(savedEvent);

        var foundEvent = repository.findById(savedEvent.getId()).orElse(null);

        assertNotNull(foundEvent);
        assertEquals(savedEvent.getId(), foundEvent.getId());
    }

    @Test
    void save_createsNewEntity() {
        NewsfeedPostDAO post = new NewsfeedPostDAO();
        post.setTitle("title");
        post.setContent("content");
        post.setImageUrl("image");
        post.setElectionId(1L);

        var savedEvent = repository.save(post);
        assertNotNull(savedEvent);
        assertNotNull(savedEvent.getId());
    }

    @Test
    void deleteById_removesEntity() {
        NewsfeedPostDAO post = new NewsfeedPostDAO();
        post.setTitle("title");
        post.setContent("content");
        post.setImageUrl("image");
        post.setElectionId(1L);

        var savedEvent = repository.save(post);
        assertNotNull(savedEvent);
        Long id = savedEvent.getId();

        repository.deleteById(id);

        assertEquals(0, repository.count());
    }
}
