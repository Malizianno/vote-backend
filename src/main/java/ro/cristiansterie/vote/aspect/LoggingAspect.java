package ro.cristiansterie.vote.aspect;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ro.cristiansterie.vote.dto.EventDTO;
import ro.cristiansterie.vote.service.EventService;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Aspect
@Component
public class LoggingAspect {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EventService eventService;

    public LoggingAspect(EventService eventService) {
        this.eventService = eventService;
    }

    @Around("@annotation(loggable)")
    public Object logExecution(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        var auth = SecurityContextHolder.getContext().getAuthentication();

        var username = AppConstants.SYSTEM_USER_USERNAME; // default to system user if no authenticated user is found
        var role = UserRoleEnum.ADMIN; // default to admin role for system actions

        if (null != auth && auth.isAuthenticated()) {
            username = auth.getName();
            role = UserRoleEnum.valueOf(auth.getAuthorities().stream().findFirst().orElseThrow().getAuthority());
        }

        EventDTO event = new EventDTO();

        event.setAction(EventActionEnum.valueOf(loggable.action()));
        event.setTimestamp(String.valueOf(new Date().getTime()));
        event.setScreen(EventScreenEnum.valueOf(loggable.screen()));
        event.setMessage(loggable.message());
        event.setRole(role);
        event.setUsername(username);

        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("Exception in {}: {}", methodName, ex.getMessage());
            throw ex;
        }

        try {
            var saved = eventService.save(event);
            log.info("Saved event: {} for method call: {}", saved.getId(), methodName);
        } catch (Exception e) {
            log.error("Failed to save event: {}", e.getMessage());
        }

        return result;
    }

}
