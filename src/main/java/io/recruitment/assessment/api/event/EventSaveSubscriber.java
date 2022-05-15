package io.recruitment.assessment.api.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventSaveSubscriber {

    @Autowired
    private EventRepository eventRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Subscribe
    public void saveEvent(Event event) throws JsonProcessingException {
        ModelMapper modelMapperForSave = new ModelMapper();
        modelMapperForSave.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        EventEntity eventEntity = modelMapperForSave.map(event, EventEntity.class);
        eventEntity.setEventMessage(objectMapper.writeValueAsString(event.getEventMessage()));
        eventRepository.save(eventEntity);
    }
}
