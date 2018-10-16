package com.multidata.notification.repository;

import com.multidata.notification.domain.EventDeliveryStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventDeliveryStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventDeliveryStatusRepository extends JpaRepository<EventDeliveryStatus, Long> {

}
