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
import ro.cristiansterie.vote.aspect.Loggable;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.entity.ElectionDAO;
import ro.cristiansterie.vote.repository.ElectionRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.Paging;

@Service
public class ElectionService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ElectionRepository repo;

    public ElectionService(ElectionRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void init() {
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPropertyCondition(ctx -> ctx.getSource() != null);
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ALL, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_GET_ALL)
    public List<ElectionDTO> getAll() {
        return convert(repo.findAll());
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_FILTERED, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_GET_FILTERED)
    public List<ElectionDTO> getFiltered(ElectionFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("candidates", "startDate", "endDate");
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getObject()), matcher), pageable).getContent());
    }

    @Loggable(action = AppConstants.EVENT_ACTION_COUNT_FILTERED, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_COUNT_FILTERED)
    public Long countFiltered(ElectionFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("candidates", "startDate", "endDate");

        return repo.count(Example.of(convert(filter.getObject()), matcher));
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ONE, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_GET_LAST)
    public ElectionDTO getLastActiveElection() {
        var found = repo.findFirstByEnabledTrueOrderByStartDateDesc();

        return found.isPresent() ? convert(found.get()) : null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_GET_ONE, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_GET_ONE)
    public ElectionDTO get(@NonNull Long id) {
        ElectionDAO returnable = repo.findById(id).orElse(null);

        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_SAVE, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_SAVE)
    public ElectionDTO save(ElectionDTO toSave) {
        var preSave = convert(toSave);
        preSave.setCandidates(null);
        var saved = repo.save(preSave);

        return convert(saved);
    }

    @Loggable(action = AppConstants.EVENT_ACTION_DELETE, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_DELETE)
    public Boolean delete(Long id) {
        try {
            repo.deleteById(id);

            return true;
        } catch (Exception e) {
            log.error("Cannot delete election {} because: {}", id, e);
        }

        return false;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_CHANGE_STATUS, screen = AppConstants.EVENT_SCREEN_ELECTIONS, message = AppConstants.EVENT_ELECTIONS_CHANGE_STATUS)
    public Boolean changeStatus(long id, boolean enabled) {
        ElectionDAO election = repo.findById(id).orElse(null);

        if (null == election || null == election.getId()) {
            return false;
        }

        election.setEnabled(enabled);
        repo.save(election);

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
