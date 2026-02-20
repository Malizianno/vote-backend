package ro.cristiansterie.vote.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cristiansterie.vote.dto.VoteDTO;
import ro.cristiansterie.vote.entity.VoteDAO;
import ro.cristiansterie.vote.repository.VoteRepository;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private VoteService service;

    @BeforeEach
    void cleanup() {
        repository.deleteAll();
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Test
    void getFiltered_returnsExpectedValue() {
        // Arrange
        VoteDAO vote1 = new VoteDAO();
        vote1.setId(1L);
        vote1.setElectionId(1L);
        vote1.setCandidateID(1L);
        vote1.setParty(PartyTypeEnum.IND);
        vote1.setTimestamp(System.currentTimeMillis());

        VoteDAO vote2 = new VoteDAO();
        vote2.setId(2L);
        vote2.setElectionId(1L);
        vote2.setCandidateID(2L);
        vote2.setParty(PartyTypeEnum.USR);
        vote2.setTimestamp(System.currentTimeMillis());

        VoteDAO vote3 = new VoteDAO();
        vote3.setId(3L);
        vote3.setElectionId(2L);
        vote3.setCandidateID(2L);
        vote3.setParty(PartyTypeEnum.PNL);
        vote3.setTimestamp(System.currentTimeMillis());

        VoteDTO filter = new VoteDTO();
        filter.setElectionId(1L);

        when(repository.findAll(any(Example.class))).thenReturn(List.of(vote1, vote3));

        // Act
        List<VoteDTO> result = service.getFiltered(filter);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(repository, times(1)).findAll(any(Example.class));
    }

    @SuppressWarnings("null")
    @Test
    void takeAVote_checkingTheVotingSystemMechanics() {
        // Arrange
        VoteDAO vote = new VoteDAO();
        vote.setId(1L);
        vote.setElectionId(1L);
        vote.setCandidateID(1L);
        vote.setParty(PartyTypeEnum.IND);
        vote.setTimestamp(System.currentTimeMillis());

        Authentication authentication = new UsernamePasswordAuthenticationToken("test", "password",
                List.of(() -> "ADMIN"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(repository.save(any(VoteDAO.class))).thenReturn(vote);

        // Act
        boolean result = service.takeAVote(service.convert(vote));

        // Assert
        assertTrue(result);

        verify(repository, times(1)).save(any(VoteDAO.class));
        verify(userService, times(1)).setHasVotedTrue(anyString());
    }

    @Test
    void cleanDBTable_removeAllEntities() {
        // Arrange
        VoteDAO vote1 = new VoteDAO();
        vote1.setId(1L);
        vote1.setElectionId(1L);
        vote1.setCandidateID(1L);
        vote1.setParty(PartyTypeEnum.IND);
        vote1.setTimestamp(System.currentTimeMillis());

        VoteDAO vote2 = new VoteDAO();
        vote2.setId(2L);
        vote2.setElectionId(1L);
        vote2.setCandidateID(2L);
        vote2.setParty(PartyTypeEnum.USR);
        vote2.setTimestamp(System.currentTimeMillis());

        VoteDAO vote3 = new VoteDAO();
        vote3.setId(3L);
        vote3.setElectionId(2L);
        vote3.setCandidateID(2L);
        vote3.setParty(PartyTypeEnum.PNL);
        vote3.setTimestamp(System.currentTimeMillis());

        when(repository.count()).thenReturn(3L);

        // Act
        boolean result = service.cleanDBTable(1L);

        // Assert
        assertTrue(result);

        verify(repository, times(1)).count();
        verify(repository, times(1)).deleteByElectionId(anyLong());
    }

}
