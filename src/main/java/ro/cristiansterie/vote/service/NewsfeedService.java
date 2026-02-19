package ro.cristiansterie.vote.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.dto.NewsfeedPostDTO;
import ro.cristiansterie.vote.dto.NewsfeedPostFilterDTO;
import ro.cristiansterie.vote.entity.NewsfeedPostDAO;
import ro.cristiansterie.vote.repository.NewsfeedRepository;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.Paging;

@Service
public class NewsfeedService extends GenericService {

    private final NewsfeedRepository repo;

    public NewsfeedService(NewsfeedRepository repo) {
        this.repo = repo;
    }

    public List<NewsfeedPostDTO> findAll() {
        return convert(repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    public List<NewsfeedPostDTO> findFiltered(NewsfeedPostFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Pageable pageable = PageRequest.of(filter.getPaging().getPage(), filter.getPaging().getSize(),
                Sort.by(Sort.Direction.DESC, "id"));

        return convert(repo.findAll(Example.of(convert(filter.getObject()), matcher), pageable).getContent());
    }

    public Long countFiltered(NewsfeedPostFilterDTO filter) {
        filter = this.checkFilters(filter);

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        return repo.count(Example.of(convert(filter.getObject()), matcher));
    }

    public NewsfeedPostDTO findById(Long id) {
        return convert(repo.findById(id).orElse(null));
    }

    public NewsfeedPostDTO create(NewsfeedPostDTO post) {
        var context = SecurityContextHolder.getContext();

        var username = AppConstants.SYSTEM_USER_USERNAME; // default to system user if no authenticated user is found

        if (isHuman(context)) {
            username = context.getAuthentication().getName();
        }
        
        post.setCreatedBy(username);

        return convert(repo.save(convert(post)));
    }

    public NewsfeedPostDTO update(Long id, NewsfeedPostDTO updated) {
        if (id == null || updated == null || updated.getId() != id) {
            return null;
        }

        NewsfeedPostDAO existing = repo.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setTitle(updated.getTitle());
        existing.setContent(updated.getContent());
        existing.setImageUrl(updated.getImageUrl());

        return convert(repo.save(existing));
    }

    public boolean delete(Long id) {
        try {
            repo.deleteById(id);

            return true;
        } catch (Exception ex) {
            // log exception;
        }
        return false;
    }

    public NewsfeedPostDTO convert(NewsfeedPostDAO dao) {
        return mapper.map(dao, NewsfeedPostDTO.class);
    }

    public NewsfeedPostDAO convert(NewsfeedPostDTO dto) {
        return mapper.map(dto, NewsfeedPostDAO.class);
    }

    public List<NewsfeedPostDTO> convert(List<NewsfeedPostDAO> daos) {
        return daos.stream().map(this::convert).toList();
    }

    private boolean isHuman(SecurityContext context) {
        return context.getAuthentication() != null && context.getAuthentication().isAuthenticated();
    }

    private NewsfeedPostFilterDTO checkFilters(NewsfeedPostFilterDTO filter) {
        if (filter.getObject() == null) {
            filter.setObject(new NewsfeedPostDTO());
        }

        if (filter.getPaging() == null) {
            filter.setPaging(new Paging());
        }

        if (filter.getObject().getTitle() != null
                && filter.getObject().getTitle().isBlank()) {
            filter.getObject().setTitle(null);
        }

        if (filter.getObject().getContent() != null && filter.getObject().getContent().isBlank()) {
            filter.getObject().setContent(null);
        }

        return filter;
    }
}
