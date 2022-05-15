package io.recruitment.assessment.api.event;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeadEventSubscriber {
    @Subscribe
    public void handleDeadEvent(DeadEvent deadEvent) {
        log.error("DEAD EVENT: {}", deadEvent.getEvent());
    }
}
