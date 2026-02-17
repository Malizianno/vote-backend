package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.entity.ElectionDAO;
import ro.cristiansterie.vote.repository.ElectionRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.Paging;

@Service
public class ElectionService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ElectionRepository repo;
    private EventService events;

    public ElectionService(ElectionRepository repo, EventService events) {
        this.repo = repo;
        this.events = events;
    }

    @PostConstruct
    public void init() {
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPropertyCondition(ctx -> ctx.getSource() != null);
    }

    public List<ElectionDTO> getAll() {
        // save event
        events.save(EventActionEnum.GET_ALL, EventScreenEnum.ELECTIONS, AppConstants.EVENT_ELECTIONS_GET_ALL);
        // return all elections
        return convert(repo.findAll());
    }

    public List<ElectionDTO> getFiltered(ElectionFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("candidates", "startDate", "endDate");
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        // save event
        events.save(EventActionEnum.GET_FILTERED, EventScreenEnum.ELECTIONS,
                AppConstants.EVENT_ELECTIONS_GET_FILTERED);

        return convert(repo.findAll(Example.of(convert(filter.getObject()), matcher), pageable).getContent());
    }

    public Long countFiltered(ElectionFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("candidates", "startDate", "endDate");

        // save event
        events.save(EventActionEnum.COUNT_FILTERED, EventScreenEnum.ELECTIONS,
                AppConstants.EVENT_ELECTIONS_COUNT_FILTERED);

        return repo.count(Example.of(convert(filter.getObject()), matcher));
    }

    public ElectionDTO getLastActiveElection() {
        // save event
        events.save(EventActionEnum.GET, EventScreenEnum.ELECTIONS, AppConstants.EVENT_ELECTIONS_GET_LAST);

        var found = repo.findFirstByEnabledTrueOrderByStartDateDesc();

        return found.isPresent() ? convert(found.get()) : null;
    }

    public ElectionDTO get(@NonNull Long id) {
        ElectionDAO returnable = repo.findById(id).orElse(null);

        // save event
        events.save(EventActionEnum.GET, EventScreenEnum.ELECTIONS, AppConstants.EVENT_ELECTIONS_GET_ONE + id);

        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public ElectionDTO save(ElectionDTO toSave) {
        var preSave = convert(toSave);
        preSave.setCandidates(null);
        var saved = repo.save(preSave);

        // save event
        events.save(EventActionEnum.CREATE, EventScreenEnum.ELECTIONS,
                AppConstants.EVENT_ELECTIONS_SAVE + saved.getId());

        return convert(saved);
    }

    public boolean delete(Long id) {
        try {
            repo.deleteById(id);

            // save event
            events.save(EventActionEnum.DELETE, EventScreenEnum.ELECTIONS, AppConstants.EVENT_ELECTIONS_DELETE + id);

            return true;
        } catch (Exception e) {
            log.error("Cannot delete election {} because: {}", id, e);
        }

        return false;
    }

    public boolean changeStatus(long id, boolean enabled) {
        ElectionDAO election = repo.findById(id).orElse(null);

        if (null == election || null == election.getId()) {
            return false;
        }

        election.setEnabled(enabled);
        repo.save(election);

        // save event
        events.save(EventActionEnum.UPDATE, EventScreenEnum.ELECTIONS,
                AppConstants.EVENT_ELECTIONS_CHANGE_STATUS + id + ", status: " + enabled);

        return true;
    }

    // CONVERTERS

    protected ElectionDAO convert(ElectionDTO election) {
        return mapper.map(election, ElectionDAO.class);
    }

    protected ElectionDTO convert(ElectionDAO election) {
        return mapper.map(election, ElectionDTO.class);
    }

    protected List<ElectionDTO> convert(List<ElectionDAO> elections) {
        return elections.stream().map(this::convert).collect(Collectors.toList());
    }

    private ElectionFilterDTO checkFilters(ElectionFilterDTO filter) {
        if (filter.getPaging() == null) {
            filter.setPaging(new Paging());
        }

        if (filter.getElection() == null) {
            filter.setElection(new ElectionDTO());
        }

        filter.getElection().setEnabled(null);

        return filter;
    }
}
