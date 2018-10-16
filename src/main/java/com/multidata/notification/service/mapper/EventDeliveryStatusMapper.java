package com.multidata.notification.service.mapper;

import com.multidata.notification.domain.*;
import com.multidata.notification.service.dto.EventDeliveryStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EventDeliveryStatus and its DTO EventDeliveryStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventDeliveryStatusMapper extends EntityMapper<EventDeliveryStatusDTO, EventDeliveryStatus> {


    @Mapping(target = "userChannelConfigurations", ignore = true)
    EventDeliveryStatus toEntity(EventDeliveryStatusDTO eventDeliveryStatusDTO);

    default EventDeliveryStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventDeliveryStatus eventDeliveryStatus = new EventDeliveryStatus();
        eventDeliveryStatus.setId(id);
        return eventDeliveryStatus;
    }
}
