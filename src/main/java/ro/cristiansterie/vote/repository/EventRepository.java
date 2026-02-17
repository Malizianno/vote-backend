package ro.cristiansterie.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.cristiansterie.vote.entity.EventDAO;

public interface EventRepository extends JpaRepository<EventDAO, Long> {
}
