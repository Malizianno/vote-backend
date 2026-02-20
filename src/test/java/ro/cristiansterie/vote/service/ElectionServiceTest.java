package ro.cristiansterie.vote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.entity.ElectionDAO;
import ro.cristiansterie.vote.repository.ElectionRepository;
import ro.cristiansterie.vote.util.Paging;

@ExtendWith(MockitoExtension.class)
class ElectionServiceTest {

    @Mock
    private ElectionRepository repository;

    @InjectMocks
    private ElectionService service;

    @BeforeEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void getAll_returnsExpectedValue() {
        // Arrange
        ElectionDAO election1 = new ElectionDAO();
        election1.setId(1L);
        election1.setEnabled(true);
        election1.setCandidates(List.of());

        ElectionDAO election2 = new ElectionDAO();
        election2.setId(2L);
        election2.setEnabled(false);
        election2.setCandidates(List.of());

        when(repository.findAll()).thenReturn(List.of(election1, election2));

        // Act
        List<ElectionDTO> result = service.getAll();

        // Assert
        assertEquals(2, result.size());

        verify(repository, times(1)).findAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getFiltered_returnsExpectedValue() {
        // Arrange
        ElectionDAO election1 = new ElectionDAO();
        election1.setId(1L);
        election1.setEnabled(true);
        election1.setCandidates(List.of());

        ElectionDAO election2 = new ElectionDAO();
        election2.setId(2L);
        election2.setEnabled(false);
        election2.setCandidates(List.of());

        Paging paging = new Paging();
        ElectionFilterDTO filter = new ElectionFilterDTO();
        filter.setElection(service.convert(election1));
        filter.setPaging(paging);

        when(repository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(new PageImpl<ElectionDAO>(List.of(election1)));

        // Act
        List<ElectionDTO> result = service.getFiltered(filter);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(repository, times(1)).findAll(any(Example.class), any(Pageable.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void countFiltered_returnsExpectedValue() {
        // Arrange
        ElectionDAO election1 = new ElectionDAO();
        election1.setId(1L);
        election1.setEnabled(true);
        election1.setCandidates(List.of());

        ElectionFilterDTO filter = new ElectionFilterDTO();
        filter.setElection(service.convert(election1));

        when(repository.count(any(Example.class))).thenReturn(1L);

        // Act
        long count = service.countFiltered(filter);

        // Assert
        assertEquals(1, count);

        verify(repository, times(1)).count(any(Example.class));
    }

    @Test
    void getLastActiveElection_returnsExpectedValue() {
        // Arrange
        ElectionDAO election1 = new ElectionDAO();
        election1.setId(1L);
        election1.setEnabled(true);
        election1.setCandidates(List.of());

        when(repository.findFirstByEnabledTrueOrderByStartDateDesc()).thenReturn(java.util.Optional.of(election1));

        // Act
        var result = service.getLastActiveElection();

        // Assert
        assertEquals(1L, result.getId());

        verify(repository, times(1)).findFirstByEnabledTrueOrderByStartDateDesc();
    }

    @Test
    void get_returnsExpectedValue() {
        // Arrange
        Long electionId = 1L;

        ElectionDAO election1 = new ElectionDAO();
        election1.setId(electionId);
        election1.setEnabled(true);
        election1.setCandidates(List.of());

        when(repository.findById(electionId)).thenReturn(java.util.Optional.of(election1));

        // Act
        ElectionDTO result = service.get(electionId);

        // Assert
        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(electionId);
    }

    @Test
    void save_persistsEntity() {
        // Arrange
        ElectionDTO election = new ElectionDTO();
        election.setEnabled(true);
        election.setCandidates(List.of());

        ElectionDAO savedElection = new ElectionDAO();
        savedElection.setId(1L);
        savedElection.setEnabled(true);
        savedElection.setCandidates(List.of());

        when(repository.save(any(ElectionDAO.class))).thenReturn(savedElection);

        // Act
        ElectionDTO result = service.save(election);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(true, result.getEnabled());
        verify(repository, times(1)).save(any(ElectionDAO.class));
    }

    @Test
    void delete_removesEntity() {
        // Arrange
        Long electionId = 1L;

        // Act
        service.delete(electionId);

        // Assert
        verify(repository, times(1)).deleteById(electionId);
    }

    @Test
    void changeStatus_updatesEntity() {
        // Arrange
        Long electionId = 1L;

        ElectionDAO election = new ElectionDAO();
        election.setId(electionId);
        election.setEnabled(true);
        election.setCandidates(List.of());

        ElectionDAO updatedElection = new ElectionDAO();
        updatedElection.setId(electionId);
        updatedElection.setEnabled(false);
        updatedElection.setCandidates(List.of());

        when(repository.findById(electionId)).thenReturn(java.util.Optional.of(election));
        when(repository.save(any(ElectionDAO.class))).thenReturn(updatedElection);

        // Act
        boolean result = service.changeStatus(electionId, false);

        // Assert
        assertEquals(true, result);
        verify(repository, times(1)).findById(electionId);
        verify(repository, times(1)).save(any(ElectionDAO.class));
    }
}
