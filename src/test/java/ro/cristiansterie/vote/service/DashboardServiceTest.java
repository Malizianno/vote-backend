package ro.cristiansterie.vote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.UserRoleEnum;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private CandidateRepository candidates;

    @Mock
    private UserRepository users;

    @InjectMocks
    private DashboardService service;

    @Test
    void getTotals_returnsExpectedValue() {
        // Arrange
        when(candidates.countByElectionId(any(Long.class))).thenReturn(5);
        when(users.countByRole(any(UserRoleEnum.class))).thenReturn(10L);

        // Act
        DashboardTotalsDTO result = service.getTotals(1L);

        // Assert
        assertEquals(5L, result.getCandidates());
        assertEquals(10L, result.getUsers());

        verify(candidates, times(1)).countByElectionId(any(Long.class));
        verify(users, times(1)).countByRole(any(UserRoleEnum.class));
    }
}
