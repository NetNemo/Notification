package com.multidata.notification.service.impl;

import com.multidata.notification.service.UserChannelConfigurationService;
import com.multidata.notification.domain.UserChannelConfiguration;
import com.multidata.notification.repository.UserChannelConfigurationRepository;
import com.multidata.notification.service.dto.UserChannelConfigurationDTO;
import com.multidata.notification.service.mapper.UserChannelConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing UserChannelConfiguration.
 */
@Service
@Transactional
public class UserChannelConfigurationServiceImpl implements UserChannelConfigurationService {

    private final Logger log = LoggerFactory.getLogger(UserChannelConfigurationServiceImpl.class);

    private final UserChannelConfigurationRepository userChannelConfigurationRepository;

    private final UserChannelConfigurationMapper userChannelConfigurationMapper;

    public UserChannelConfigurationServiceImpl(UserChannelConfigurationRepository userChannelConfigurationRepository, UserChannelConfigurationMapper userChannelConfigurationMapper) {
        this.userChannelConfigurationRepository = userChannelConfigurationRepository;
        this.userChannelConfigurationMapper = userChannelConfigurationMapper;
    }

    /**
     * Save a userChannelConfiguration.
     *
     * @param userChannelConfigurationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UserChannelConfigurationDTO save(UserChannelConfigurationDTO userChannelConfigurationDTO) {
        log.debug("Request to save UserChannelConfiguration : {}", userChannelConfigurationDTO);

        UserChannelConfiguration userChannelConfiguration = userChannelConfigurationMapper.toEntity(userChannelConfigurationDTO);
        userChannelConfiguration = userChannelConfigurationRepository.save(userChannelConfiguration);
        return userChannelConfigurationMapper.toDto(userChannelConfiguration);
    }

    /**
     * Get all the userChannelConfigurations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserChannelConfigurationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserChannelConfigurations");
        return userChannelConfigurationRepository.findAll(pageable)
            .map(userChannelConfigurationMapper::toDto);
    }


    /**
     * Get one userChannelConfiguration by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserChannelConfigurationDTO> findOne(Long id) {
        log.debug("Request to get UserChannelConfiguration : {}", id);
        return userChannelConfigurationRepository.findById(id)
            .map(userChannelConfigurationMapper::toDto);
    }

    /**
     * Delete the userChannelConfiguration by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserChannelConfiguration : {}", id);
        userChannelConfigurationRepository.deleteById(id);
    }
}
