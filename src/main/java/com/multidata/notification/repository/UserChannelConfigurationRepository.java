package com.multidata.notification.repository;

import com.multidata.notification.domain.UserChannelConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserChannelConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserChannelConfigurationRepository extends JpaRepository<UserChannelConfiguration, Long> {

}
