package com.multidata.notification.service.impl;

import com.multidata.notification.service.EventDeliveryStatusService;
import com.multidata.notification.domain.EventDeliveryStatus;
import com.multidata.notification.repository.EventDeliveryStatusRepository;
import com.multidata.notification.service.dto.EventDeliveryStatusDTO;
import com.multidata.notification.service.mapper.EventDeliveryStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing EventDeliveryStatus.
 */
@Service
@Transactional
public class EventDeliveryStatusServiceImpl implements EventDeliveryStatusService {

    private final Logger log = LoggerFactory.getLogger(EventDeliveryStatusServiceImpl.class);

    private final EventDeliveryStatusRepository eventDeliveryStatusRepository;

    private final EventDeliveryStatusMapper eventDeliveryStatusMapper;

    public EventDeliveryStatusServiceImpl(EventDeliveryStatusRepository eventDeliveryStatusRepository, EventDeliveryStatusMapper eventDeliveryStatusMapper) {
        this.eventDeliveryStatusRepository = eventDeliveryStatusRepository;
        this.eventDeliveryStatusMapper = eventDeliveryStatusMapper;
    }

    /**
     * Save a eventDeliveryStatus.
     *
     * @param eventDeliveryStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EventDeliveryStatusDTO save(EventDeliveryStatusDTO eventDeliveryStatusDTO) {
        log.debug("Request to save EventDeliveryStatus : {}", eventDeliveryStatusDTO);

        EventDeliveryStatus eventDeliveryStatus = eventDeliveryStatusMapper.toEntity(eventDeliveryStatusDTO);
        eventDeliveryStatus = eventDeliveryStatusRepository.save(eventDeliveryStatus);
        return eventDeliveryStatusMapper.toDto(eventDeliveryStatus);
    }

    /**
     * Get all the eventDeliveryStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EventDeliveryStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventDeliveryStatuses");
        return eventDeliveryStatusRepository.findAll(pageable)
            .map(eventDeliveryStatusMapper::toDto);
    }


    /**
     * Get one eventDeliveryStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EventDeliveryStatusDTO> findOne(Long id) {
        log.debug("Request to get EventDeliveryStatus : {}", id);
        return eventDeliveryStatusRepository.findById(id)
            .map(eventDeliveryStatusMapper::toDto);
    }

    /**
     * Delete the eventDeliveryStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventDeliveryStatus : {}", id);
        eventDeliveryStatusRepository.deleteById(id);
    }
}
