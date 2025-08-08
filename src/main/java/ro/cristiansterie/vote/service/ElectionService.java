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

import lombok.NonNull;
import ro.cristiansterie.vote.dto.ElectionDTO;
import ro.cristiansterie.vote.dto.ElectionFilterDTO;
import ro.cristiansterie.vote.entity.ElectionDAO;
import ro.cristiansterie.vote.repository.ElectionRepository;

@Service
public class ElectionService extends GenericService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ElectionRepository repo;

    public ElectionService(ElectionRepository repo) {
        this.repo = repo;
    }

    public List<ElectionDTO> getAll() {
        return convert(repo.findAll());
    }

    public List<ElectionDTO> getFiltered(ElectionFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getElection()), matcher), pageable).getContent());
    }

    public int countFiltered(ElectionFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return (int) repo.count(Example.of(convert(filter.getElection()), matcher));
    }

    public ElectionDTO get(@NonNull Integer id) {
        ElectionDAO returnable = repo.findById(id).orElse(null);
        return null != returnable && null != returnable.getId() ? convert(returnable) : null;
    }

    public ElectionDTO save(ElectionDTO toSave) {
        return convert(repo.save(convert(toSave)));
    }

    public boolean delete(Integer id) {
        try {
            repo.deleteById(id);

            return true;
        } catch (Exception e) {
            log.error("Cannot delete election {} because: {}", id, e);
        }

        return false;
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
        // WIP: set filter checks specific for this

        return filter;
    }
}
