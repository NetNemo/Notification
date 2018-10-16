package com.multidata.notification.repository;

import com.multidata.notification.domain.DeliveryChannel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DeliveryChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryChannelRepository extends JpaRepository<DeliveryChannel, Long> {

}
