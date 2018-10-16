package com.multidata.notification.service.mapper;

import com.multidata.notification.domain.*;
import com.multidata.notification.service.dto.UserChannelConfigurationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserChannelConfiguration and its DTO UserChannelConfigurationDTO.
 */
@Mapper(componentModel = "spring", uses = {EventDeliveryStatusMapper.class, DeliveryChannelMapper.class})
public interface UserChannelConfigurationMapper extends EntityMapper<UserChannelConfigurationDTO, UserChannelConfiguration> {

    @Mapping(source = "eventDeliveryStatus.id", target = "eventDeliveryStatusId")
    @Mapping(source = "deliveryChannel.id", target = "deliveryChannelId")
    UserChannelConfigurationDTO toDto(UserChannelConfiguration userChannelConfiguration);

    @Mapping(target = "applicationEvents", ignore = true)
    @Mapping(target = "systemEvents", ignore = true)
    @Mapping(target = "userIDS", ignore = true)
    @Mapping(source = "eventDeliveryStatusId", target = "eventDeliveryStatus")
    @Mapping(source = "deliveryChannelId", target = "deliveryChannel")
    UserChannelConfiguration toEntity(UserChannelConfigurationDTO userChannelConfigurationDTO);

    default UserChannelConfiguration fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserChannelConfiguration userChannelConfiguration = new UserChannelConfiguration();
        userChannelConfiguration.setId(id);
        return userChannelConfiguration;
    }
}
