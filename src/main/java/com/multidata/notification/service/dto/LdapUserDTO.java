package com.multidata.notification.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the LdapUser entity.
 */
public class LdapUserDTO implements Serializable {

    private Long id;

    private String userID;

    private String name;

    private Long userChannelConfigurationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserChannelConfigurationId() {
        return userChannelConfigurationId;
    }

    public void setUserChannelConfigurationId(Long userChannelConfigurationId) {
        this.userChannelConfigurationId = userChannelConfigurationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LdapUserDTO ldapUserDTO = (LdapUserDTO) o;
        if (ldapUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ldapUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LdapUserDTO{" +
            "id=" + getId() +
            ", userID='" + getUserID() + "'" +
            ", name='" + getName() + "'" +
            ", userChannelConfiguration=" + getUserChannelConfigurationId() +
            "}";
    }
}
