package ro.cristiansterie.vote.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ro.cristiansterie.vote.service.NewsfeedAutomatedService;

@Component
public class SchedulerStarter {

    private final NewsfeedAutomatedService service;

    public SchedulerStarter(NewsfeedAutomatedService service) {
        this.service = service;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        service.checkingMilestones();
    }
}
