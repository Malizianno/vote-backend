package ro.cristiansterie.vote.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.cristiansterie.vote.dto.ElectionCampaignDTO;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.dto.UserFilterDTO;

@ExtendWith(MockitoExtension.class)
class NewsfeedAutomatedServiceTest {

    @Mock
    private NewsfeedService newsfeedService;

    @Mock
    private UserService userService;

    @Mock
    private ElectionsHelperService electionsHelperService;

    @InjectMocks
    private NewsfeedAutomatedService service;

    @Test
    void checkingMilestones_createsNewsfeedPosts() {
        // Arrange
        ElectionDTO election1 = new ElectionDTO();
        election1.setId(1L);
        election1.setEnabled(true);
        election1.setCandidates(List.of());

        ElectionDTO election2 = new ElectionDTO();
        election2.setId(2L);
        election2.setEnabled(false);
        election2.setCandidates(List.of());

        ElectionDTO election3 = new ElectionDTO();
        election3.setId(3L);
        election3.setEnabled(true);
        election3.setCandidates(List.of());

        ElectionCampaignDTO electionCampaign = new ElectionCampaignDTO();
        electionCampaign.setEnabled(true);
        electionCampaign.setElections(List.of(election1, election2, election3));

        when(electionsHelperService.getElectionCampaignStatus()).thenReturn(electionCampaign);
        when(userService.countFiltered(any(UserFilterDTO.class))).thenReturn(100L);
        when(electionsHelperService.countAllVotes(anyLong())).thenReturn(50L);

        // Act
        service.checkingMilestones();

        // Assert
        verify(userService, atLeastOnce()).countFiltered(any(UserFilterDTO.class));
        verify(electionsHelperService, atLeastOnce()).getElectionCampaignStatus();
        verify(electionsHelperService, atLeastOnce()).countAllVotes(anyLong());
        verify(newsfeedService, atLeastOnce()).create(any(NewsfeedPostDTO.class));
    }
}
