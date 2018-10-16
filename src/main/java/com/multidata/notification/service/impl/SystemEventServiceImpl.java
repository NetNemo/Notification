package com.multidata.notification.service.impl;

import com.multidata.notification.service.SystemEventService;
import com.multidata.notification.domain.SystemEvent;
import com.multidata.notification.repository.SystemEventRepository;
import com.multidata.notification.service.dto.SystemEventDTO;
import com.multidata.notification.service.mapper.SystemEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing SystemEvent.
 */
@Service
@Transactional
public class SystemEventServiceImpl implements SystemEventService {

    private final Logger log = LoggerFactory.getLogger(SystemEventServiceImpl.class);

    private final SystemEventRepository systemEventRepository;

    private final SystemEventMapper systemEventMapper;

    public SystemEventServiceImpl(SystemEventRepository systemEventRepository, SystemEventMapper systemEventMapper) {
        this.systemEventRepository = systemEventRepository;
        this.systemEventMapper = systemEventMapper;
    }

    /**
     * Save a systemEvent.
     *
     * @param systemEventDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SystemEventDTO save(SystemEventDTO systemEventDTO) {
        log.debug("Request to save SystemEvent : {}", systemEventDTO);

        SystemEvent systemEvent = systemEventMapper.toEntity(systemEventDTO);
        systemEvent = systemEventRepository.save(systemEvent);
        return systemEventMapper.toDto(systemEvent);
    }

    /**
     * Get all the systemEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SystemEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemEvents");
        return systemEventRepository.findAll(pageable)
            .map(systemEventMapper::toDto);
    }


    /**
     * Get one systemEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SystemEventDTO> findOne(Long id) {
        log.debug("Request to get SystemEvent : {}", id);
        return systemEventRepository.findById(id)
            .map(systemEventMapper::toDto);
    }

    /**
     * Delete the systemEvent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemEvent : {}", id);
        systemEventRepository.deleteById(id);
    }
}
