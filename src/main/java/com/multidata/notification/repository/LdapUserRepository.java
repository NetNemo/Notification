package com.multidata.notification.repository;

import com.multidata.notification.domain.LdapUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LdapUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LdapUserRepository extends JpaRepository<LdapUser, Long> {

}
