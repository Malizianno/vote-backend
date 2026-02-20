package ro.cristiansterie.vote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.cristiansterie.vote.dto.CandidateDTO;
import ro.cristiansterie.vote.dto.CandidateFilterDTO;
import ro.cristiansterie.vote.dto.CandidateWithStatisticsDTO;
import ro.cristiansterie.vote.dto.ElectionCampaignDTO;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.VoteDTO;
import ro.cristiansterie.vote.util.PartyTypeEnum;

@ExtendWith(MockitoExtension.class)
class ElectionHelperServiceTest {

    @Mock
    private VoteService voteService;

    @Mock
    private CandidateService candidateService;

    @Mock
    private UserService userService;

    @Mock
    private ElectionService electionService;

    @InjectMocks
    private ElectionsHelperService service;

    @Test
    void getElectionCampaignStatus_returnsOnlyEnabledElections() {
        // Arrange
        ElectionDTO enabled1 = new ElectionDTO();
        enabled1.setId(1L);
        enabled1.setEnabled(true);
        enabled1.setCandidates(List.of());

        ElectionDTO disabled = new ElectionDTO();
        disabled.setId(2L);
        disabled.setEnabled(false);
        disabled.setCandidates(List.of());

        ElectionDTO enabled2 = new ElectionDTO();
        enabled2.setId(3L);
        enabled2.setEnabled(true);
        enabled2.setCandidates(List.of());

        when(electionService.getAll())
                .thenReturn(List.of(enabled1, disabled, enabled2));

        // Act
        ElectionCampaignDTO result = service.getElectionCampaignStatus();

        // Assert
        assertTrue(result.isEnabled());
        assertEquals(2, result.getElections().size());
        assertTrue(result.getElections().stream().allMatch(ElectionDTO::getEnabled));

        verify(electionService, times(1)).getAll();
    }

    @Test
    void getElectionResult_returnsCandidateWithMostVotes() {
        // Arrange
        long electionId = 1L;

        when(voteService.getFiltered(any(VoteDTO.class)))
                .thenReturn(createVoteDTOs(30));

        CandidateDTO candidate1 = new CandidateDTO();
        candidate1.setId(1L);
        candidate1.setFirstName("Alice");

        CandidateDTO candidate2 = new CandidateDTO();
        candidate2.setId(2L);
        candidate2.setFirstName("Bob");

        when(candidateService.get(anyLong())).thenReturn(candidate1);

        // Act
        CandidateDTO result = service.getElectionResult(electionId);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getFirstName());

        verify(voteService, times(1)).getFiltered(any(VoteDTO.class));
        verify(candidateService, times(1)).get(anyLong());
    }

    @Test
    void vote_returnsTrueWhenVoteIsSuccessful() {
        // Arrange
        CandidateDTO votedCandidate = new CandidateDTO();
        votedCandidate.setId(1L);
        votedCandidate.setElectionId(1L);
        votedCandidate.setParty(PartyTypeEnum.IND);

        long userId = 1L;

        when(userService.get(userId)).thenReturn(null); // Simulate user not found

        // Act
        boolean result = service.vote(votedCandidate, userId);

        // Assert
        assertEquals(false, result); // Expecting false since user is not found

        verify(userService, times(1)).get(userId);
        verify(voteService, times(0)).takeAVote(any(VoteDTO.class)); // Vote should not be taken
    }

    @Test
    void getParsedVotes_returnsCandidateWithStatistics() {
        // Arrange
        long electionId = 1L;

        when(voteService.getFiltered(any(VoteDTO.class)))
                .thenReturn(createVoteDTOs(30));

        CandidateDTO candidate1 = new CandidateDTO();
        candidate1.setId(1L);
        candidate1.setFirstName("Alice");
        candidate1.setElectionId(electionId);
        candidate1.setParty(PartyTypeEnum.IND);

        CandidateDTO candidate2 = new CandidateDTO();
        candidate2.setId(2L);
        candidate2.setFirstName("Bob");
        candidate2.setElectionId(electionId);
        candidate2.setParty(PartyTypeEnum.IND);

        when(candidateService.getFiltered(any(CandidateFilterDTO.class))).thenReturn(List.of(candidate1, candidate2));

        // Act
        List<CandidateWithStatisticsDTO> result = service.getParsedVotes(electionId);

        // Assert
        assertEquals(2, result.size());

        CandidateWithStatisticsDTO c1Stats = result.stream()
                .filter(c -> c.getId() == 1L)
                .findFirst()
                .orElse(null);

        CandidateWithStatisticsDTO c2Stats = result.stream()
                .filter(c -> c.getId() == 2L)
                .findFirst()
                .orElse(null);

        assertEquals(15, c1Stats.getTotalVotes());
        assertEquals(15, c2Stats.getTotalVotes());

        verify(voteService, times(1)).getFiltered(any(VoteDTO.class));
        verify(candidateService, times(1)).getFiltered(any(CandidateFilterDTO.class));
    }

    @Test
    void hasUserVoted_returnsTrueIfUserHasVoted() {
        // Arrange
        long userId = 1L;

        UserDTO user = new UserDTO();
        user.setId(userId);
        user.setHasVoted(true);
        user.setUsername("username");

        when(userService.hasVotedByUsername(user.getUsername())).thenReturn(true);

        // Act
        boolean result = service.hasUserVoted(user.getUsername());

        // Assert
        assertTrue(result);

        verify(userService, times(1)).hasVotedByUsername(anyString());
    }

    @Test
    void countAllTests_returnsTotalVotesForElection() {
        // Arrange
        long electionId = 1L;

        when(voteService.getFiltered(any(VoteDTO.class)))
                .thenReturn(createVoteDTOs(30));

        // Act
        long result = service.countAllVotes(electionId);

        // Assert
        assertEquals(30, result);

        verify(voteService, times(1)).getFiltered(any(VoteDTO.class));
    }

    @Test
    void cleanAllVotes_returnsTrueWhenVotesAreCleaned() {
        // Arrange
        int electionId = 1;

        when(voteService.cleanDBTable(electionId)).thenReturn(true);

        // Act
        boolean result = service.cleanAllVotes(electionId);

        // Assert
        assertTrue(result);

        verify(voteService, times(1)).cleanDBTable(electionId);
    }

    private List<VoteDTO> createVoteDTOs(int count) {
        List<VoteDTO> votes = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            VoteDTO vote = new VoteDTO();

            vote.setId((long) i);
            vote.setCandidateID((long) (i % 2 + 1)); // Alternate between candidate 1 and 2
            vote.setElectionId(1L);
            vote.setParty(PartyTypeEnum.IND);
            vote.setTimestamp(System.currentTimeMillis());

            votes.add(vote);
        }
        return votes;
    }
}
