package com.multidata.notification.service.impl;

import com.multidata.notification.service.ApplicationEventService;
import com.multidata.notification.domain.ApplicationEvent;
import com.multidata.notification.repository.ApplicationEventRepository;
import com.multidata.notification.service.dto.ApplicationEventDTO;
import com.multidata.notification.service.mapper.ApplicationEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ApplicationEvent.
 */
@Service
@Transactional
public class ApplicationEventServiceImpl implements ApplicationEventService {

    private final Logger log = LoggerFactory.getLogger(ApplicationEventServiceImpl.class);

    private final ApplicationEventRepository applicationEventRepository;

    private final ApplicationEventMapper applicationEventMapper;

    public ApplicationEventServiceImpl(ApplicationEventRepository applicationEventRepository, ApplicationEventMapper applicationEventMapper) {
        this.applicationEventRepository = applicationEventRepository;
        this.applicationEventMapper = applicationEventMapper;
    }

    /**
     * Save a applicationEvent.
     *
     * @param applicationEventDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationEventDTO save(ApplicationEventDTO applicationEventDTO) {
        log.debug("Request to save ApplicationEvent : {}", applicationEventDTO);

        ApplicationEvent applicationEvent = applicationEventMapper.toEntity(applicationEventDTO);
        applicationEvent = applicationEventRepository.save(applicationEvent);
        return applicationEventMapper.toDto(applicationEvent);
    }

    /**
     * Get all the applicationEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicationEvents");
        return applicationEventRepository.findAll(pageable)
            .map(applicationEventMapper::toDto);
    }


    /**
     * Get one applicationEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationEventDTO> findOne(Long id) {
        log.debug("Request to get ApplicationEvent : {}", id);
        return applicationEventRepository.findById(id)
            .map(applicationEventMapper::toDto);
    }

    /**
     * Delete the applicationEvent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationEvent : {}", id);
        applicationEventRepository.deleteById(id);
    }
}
