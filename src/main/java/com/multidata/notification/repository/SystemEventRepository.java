package com.multidata.notification.repository;

import com.multidata.notification.domain.SystemEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SystemEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemEventRepository extends JpaRepository<SystemEvent, Long> {

}
