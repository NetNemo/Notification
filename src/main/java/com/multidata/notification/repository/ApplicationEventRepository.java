package com.multidata.notification.repository;

import com.multidata.notification.domain.ApplicationEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ApplicationEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationEventRepository extends JpaRepository<ApplicationEvent, Long> {

}
