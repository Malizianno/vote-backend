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

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.entity.CandidateDAO;
import ro.cristiansterie.vote.repository.CandidateRepository;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository repository;

    @InjectMocks
    private CandidateService service;

    @BeforeEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void getAll_returnsExpectedValue() {
        // Arrange
        CandidateDAO c1 = new CandidateDAO();
        c1.setId(1L);
        c1.setFirstName("Alice");

        CandidateDAO c2 = new CandidateDAO();
        c2.setId(2L);
        c2.setFirstName("Bob");

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        // Act
        List<CandidateDTO> result = service.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Bob", result.get(1).getFirstName());

        verify(repository, times(1)).findAll();
    }

    @Test
    void getAllForElection_returnsExpectedValue() {
        // Arrange
        Long electionId = 1L;

        CandidateDAO c1 = new CandidateDAO();
        c1.setId(1L);
        c1.setFirstName("Alice");
        c1.setElectionId(electionId);

        CandidateDAO c2 = new CandidateDAO();
        c2.setId(2L);
        c2.setFirstName("Bob");
        c2.setElectionId(electionId);

        when(repository.findAllByElectionId(electionId)).thenReturn(List.of(c1, c2));

        // Act
        List<CandidateDTO> result = service.getAllForElection(electionId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Bob", result.get(1).getFirstName());

        verify(repository, times(1)).findAllByElectionId(electionId);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getFiltered_returnsExpectedValue() {
        // Arrange
        CandidateDAO c1 = new CandidateDAO();
        c1.setId(1L);
        c1.setFirstName("Alice");

        CandidateDAO c2 = new CandidateDAO();
        c2.setId(2L);
        c2.setFirstName("Bob");

        CandidateFilterDTO filter = new CandidateFilterDTO();
        filter.setCandidate(service.convert(c1));

        when(repository.findAll(any(Example.class))).thenReturn(List.of(c1));

        // Act
        List<CandidateDTO> result = service.getFiltered(filter);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());

        verify(repository, times(1)).findAll(any(Example.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void countFiltered_returnsExpectedValue() {
        // Arrange
        CandidateDAO c1 = new CandidateDAO();
        c1.setId(1L);
        c1.setFirstName("Alice");

        CandidateFilterDTO filter = new CandidateFilterDTO();
        filter.setCandidate(service.convert(c1));

        when(repository.count(any(Example.class))).thenReturn(1L);

        // Act
        long count = service.countFiltered(filter);

        // Assert
        assertEquals(1, count);

        verify(repository, times(1)).count(any(Example.class));
    }

    @Test
    void get_returnsExpectedValue() {
        // Arrange
        Long candidateId = 1L;

        CandidateDAO c1 = new CandidateDAO();
        c1.setId(candidateId);
        c1.setFirstName("Alice");

        when(repository.findById(candidateId)).thenReturn(java.util.Optional.of(c1));

        // Act
        CandidateDTO result = service.get(candidateId);

        // Assert
        assertEquals("Alice", result.getFirstName());
        verify(repository, times(1)).findById(candidateId);
    }

    @Test
    void save_persistsEntity() {
        // Arrange
        CandidateDTO candidate = new CandidateDTO();
        candidate.setFirstName("Alice");

        CandidateDAO savedCandidate = new CandidateDAO();
        savedCandidate.setId(1L);
        savedCandidate.setFirstName("Alice");

        when(repository.save(any(CandidateDAO.class))).thenReturn(savedCandidate);

        // Act
        CandidateDTO result = service.save(candidate);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getFirstName());
        verify(repository, times(1)).save(any(CandidateDAO.class));
    }

    @Test
    void delete_removesEntity() {
        // Arrange
        Long candidateId = 1L;

        // Act
        service.delete(candidateId);

        // Assert
        verify(repository, times(1)).deleteById(candidateId);
    }
}
