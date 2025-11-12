package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.DashboardTotalsDTO;
import ro.cristiansterie.vote.repository.CandidateRepository;
import ro.cristiansterie.vote.repository.UserRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class DashboardService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CandidateRepository candidates;
    private UserRepository users;
    private EventService events;

    public DashboardService(CandidateRepository candidates, UserRepository users, CandidateService candidatesService,
            ElectionsHelperService electionsHelperService, ElectionService electionService, EventService events) {
        this.candidates = candidates;
        this.users = users;
        this.events = events;
    }

    public DashboardTotalsDTO getTotals(int electionID) {
        DashboardTotalsDTO totals = new DashboardTotalsDTO();

        totals.setCandidates((int) candidates.countByElectionId(electionID));
        totals.setUsers((int) users.countByRole(UserRoleEnum.VOTANT));

        // save event
        events.save(EventActionEnum.GET_ALL, EventScreenEnum.DASHBOARD, AppConstants.EVENT_DASHBOARD_GET_TOTALS);

        return totals;
    }

}
