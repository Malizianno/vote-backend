package ro.cristiansterie.vote.service;

import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.aspect.Loggable;
import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class DashboardService extends GenericService {

    private CandidateRepository candidates;
    private UserRepository users;

    public DashboardService(CandidateRepository candidates, UserRepository users, CandidateService candidatesService,
            ElectionsHelperService electionsHelperService, ElectionService electionService) {
        this.candidates = candidates;
        this.users = users;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ALL, screen = AppConstants.EVENT_SCREEN_DASHBOARD, message = AppConstants.EVENT_DASHBOARD_GET_TOTALS)
    public DashboardTotalsDTO getTotals(long electionID) {
        DashboardTotalsDTO totals = new DashboardTotalsDTO();

        totals.setCandidates(candidates.countByElectionId(electionID));
        totals.setUsers(users.countByRole(UserRoleEnum.VOTANT));

        return totals;
    }

}
