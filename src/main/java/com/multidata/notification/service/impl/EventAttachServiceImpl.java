package com.multidata.notification.service.impl;

import com.multidata.notification.service.EventAttachService;
import com.multidata.notification.domain.EventAttach;
import com.multidata.notification.repository.EventAttachRepository;
import com.multidata.notification.service.dto.EventAttachDTO;
import com.multidata.notification.service.mapper.EventAttachMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing EventAttach.
 */
@Service
@Transactional
public class EventAttachServiceImpl implements EventAttachService {

    private final Logger log = LoggerFactory.getLogger(EventAttachServiceImpl.class);

    private final EventAttachRepository eventAttachRepository;

    private final EventAttachMapper eventAttachMapper;

    public EventAttachServiceImpl(EventAttachRepository eventAttachRepository, EventAttachMapper eventAttachMapper) {
        this.eventAttachRepository = eventAttachRepository;
        this.eventAttachMapper = eventAttachMapper;
    }

    /**
     * Save a eventAttach.
     *
     * @param eventAttachDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EventAttachDTO save(EventAttachDTO eventAttachDTO) {
        log.debug("Request to save EventAttach : {}", eventAttachDTO);

        EventAttach eventAttach = eventAttachMapper.toEntity(eventAttachDTO);
        eventAttach = eventAttachRepository.save(eventAttach);
        return eventAttachMapper.toDto(eventAttach);
    }

    /**
     * Get all the eventAttaches.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EventAttachDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventAttaches");
        return eventAttachRepository.findAll(pageable)
            .map(eventAttachMapper::toDto);
    }


    /**
     * Get one eventAttach by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EventAttachDTO> findOne(Long id) {
        log.debug("Request to get EventAttach : {}", id);
        return eventAttachRepository.findById(id)
            .map(eventAttachMapper::toDto);
    }

    /**
     * Delete the eventAttach by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventAttach : {}", id);
        eventAttachRepository.deleteById(id);
    }
}
