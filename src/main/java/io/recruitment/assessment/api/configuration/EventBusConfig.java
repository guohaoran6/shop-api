package io.recruitment.assessment.api.configuration;

import com.google.common.eventbus.EventBus;
import io.recruitment.assessment.api.event.DeadEventSubscriber;
import io.recruitment.assessment.api.event.EventSaveSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class EventBusConfig {

    @Autowired
    DeadEventSubscriber deadEventSubscriber;
    @Autowired
    EventBus eventBus;
    @Autowired
    EventSaveSubscriber eventSaveSubscriber;

    @PostConstruct
    public void register() {
        eventBus.register(deadEventSubscriber);
        eventBus.register(eventSaveSubscriber);
    }
}
