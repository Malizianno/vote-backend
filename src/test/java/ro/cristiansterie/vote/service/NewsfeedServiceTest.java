package ro.cristiansterie.vote.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.dto.NewsfeedPostFilterDTO;
import ro.cristiansterie.vote.repository.NewsfeedRepository;
import ro.cristiansterie.vote.util.Paging;

@ExtendWith(MockitoExtension.class)
class NewsfeedServiceTest {

    @Mock
    private NewsfeedRepository repository;

    @InjectMocks
    private NewsfeedService service;

    @Test
    void findAll_returnsExpectedValue() {
        // Arrange
        NewsfeedPostDTO post1 = new NewsfeedPostDTO();
        post1.setId(1L);
        post1.setContent("Post 1");

        NewsfeedPostDTO post2 = new NewsfeedPostDTO();
        post2.setId(2L);
        post2.setContent("Post 2");

        when(repository.findAll(any(Sort.class))).thenReturn(List.of(service.convert(post1), service.convert(post2)));

        // Act
        List<NewsfeedPostDTO> result = service.findAll();

        // Assert
        assertEquals(2, result.size());

        verify(repository).findAll(any(Sort.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findFiltered_returnsExpectedValue() {
        // Arrange
        NewsfeedPostDTO post1 = new NewsfeedPostDTO();
        post1.setId(1L);
        post1.setContent("Post 1");

        NewsfeedPostDTO post2 = new NewsfeedPostDTO();
        post2.setId(2L);
        post2.setContent("Post 2");

        NewsfeedPostFilterDTO filter = new NewsfeedPostFilterDTO();
        filter.setObject(post1);
        filter.setPaging(new Paging());

        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(service.convert(post1))));

        // Act
        List<NewsfeedPostDTO> result = service.findFiltered(filter);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(post1.getContent(), result.get(0).getContent());

        verify(repository).findAll(any(Example.class), any(Pageable.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void countFiltered_returnsExpectedValue() {
        // Arrange
        NewsfeedPostDTO post1 = new NewsfeedPostDTO();
        post1.setId(1L);
        post1.setContent("Post 1");

        NewsfeedPostFilterDTO filter = new NewsfeedPostFilterDTO();
        filter.setObject(post1);

        when(repository.count(any(Example.class))).thenReturn(5L);

        // Act
        long result = service.countFiltered(filter);

        // Assert
        assertEquals(5L, result);

        verify(repository).count(any(Example.class));
    }

    @Test
    void findById_returnsExpectedValue() {
        // Arrange
        NewsfeedPostDTO post = new NewsfeedPostDTO();
        post.setId(1L);
        post.setContent("Post 1");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(service.convert(post)));

        // Act
        NewsfeedPostDTO result = service.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals(post.getContent(), result.getContent());

        verify(repository).findById(anyLong());
    }

    @Test
    void create_persistsEntity() {
        // Arrange
        NewsfeedPostDTO post = new NewsfeedPostDTO();
        post.setId(1L);
        post.setContent("New Post");

        when(repository.save(any())).thenReturn(service.convert(post));

        // Act
        NewsfeedPostDTO result = service.create(post);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals(post.getContent(), result.getContent());

        verify(repository).save(any());
    }

    @Test
    void update_persistsEntity() {
        // Arrange
        NewsfeedPostDTO post = new NewsfeedPostDTO();
        post.setId(1L);
        post.setContent("Updated Post");

        when(repository.findById(anyLong())).thenReturn(Optional.of(service.convert(post)));
        when(repository.save(any())).thenReturn(service.convert(post));

        // Act
        NewsfeedPostDTO result = service.update(post.getId(), post);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals(post.getContent(), result.getContent());

        verify(repository).save(any());
        verify(repository).findById(anyLong());
    }

    @Test
    void delete_removesEntity() {
        // Arrange
        NewsfeedPostDTO post = new NewsfeedPostDTO();
        post.setId(1L);
        post.setContent("Post to Delete");

        // Act
        service.delete(1L);

        // Assert
        verify(repository).deleteById(anyLong());
    }
}
