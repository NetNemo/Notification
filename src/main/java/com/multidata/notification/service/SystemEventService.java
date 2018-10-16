package com.multidata.notification.service;

import com.multidata.notification.service.dto.SystemEventDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SystemEvent.
 */
public interface SystemEventService {

    /**
     * Save a systemEvent.
     *
     * @param systemEventDTO the entity to save
     * @return the persisted entity
     */
    SystemEventDTO save(SystemEventDTO systemEventDTO);

    /**
     * Get all the systemEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SystemEventDTO> findAll(Pageable pageable);


    /**
     * Get the "id" systemEvent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SystemEventDTO> findOne(Long id);

    /**
     * Delete the "id" systemEvent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
