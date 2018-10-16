package com.multidata.notification.service.mapper;

import com.multidata.notification.domain.*;
import com.multidata.notification.service.dto.DeliveryChannelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DeliveryChannel and its DTO DeliveryChannelDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryChannelMapper extends EntityMapper<DeliveryChannelDTO, DeliveryChannel> {


    @Mapping(target = "userChannelConfigurations", ignore = true)
    DeliveryChannel toEntity(DeliveryChannelDTO deliveryChannelDTO);

    default DeliveryChannel fromId(Long id) {
        if (id == null) {
            return null;
        }
        DeliveryChannel deliveryChannel = new DeliveryChannel();
        deliveryChannel.setId(id);
        return deliveryChannel;
    }
}
