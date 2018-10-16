package com.multidata.notification.service.mapper;

import com.multidata.notification.domain.*;
import com.multidata.notification.service.dto.EventAttachDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EventAttach and its DTO EventAttachDTO.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface EventAttachMapper extends EntityMapper<EventAttachDTO, EventAttach> {

    @Mapping(source = "event.id", target = "eventId")
    EventAttachDTO toDto(EventAttach eventAttach);

    @Mapping(source = "eventId", target = "event")
    EventAttach toEntity(EventAttachDTO eventAttachDTO);

    default EventAttach fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventAttach eventAttach = new EventAttach();
        eventAttach.setId(id);
        return eventAttach;
    }
}
