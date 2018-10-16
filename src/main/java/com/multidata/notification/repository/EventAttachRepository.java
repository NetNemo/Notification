package com.multidata.notification.repository;

import com.multidata.notification.domain.EventAttach;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventAttach entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventAttachRepository extends JpaRepository<EventAttach, Long> {

}
