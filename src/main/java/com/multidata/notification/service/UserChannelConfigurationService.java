package com.multidata.notification.service;

import com.multidata.notification.service.dto.UserChannelConfigurationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing UserChannelConfiguration.
 */
public interface UserChannelConfigurationService {

    /**
     * Save a userChannelConfiguration.
     *
     * @param userChannelConfigurationDTO the entity to save
     * @return the persisted entity
     */
    UserChannelConfigurationDTO save(UserChannelConfigurationDTO userChannelConfigurationDTO);

    /**
     * Get all the userChannelConfigurations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UserChannelConfigurationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" userChannelConfiguration.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UserChannelConfigurationDTO> findOne(Long id);

    /**
     * Delete the "id" userChannelConfiguration.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
