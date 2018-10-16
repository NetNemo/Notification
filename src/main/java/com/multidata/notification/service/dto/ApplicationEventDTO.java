package com.multidata.notification.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ApplicationEvent entity.
 */
public class ApplicationEventDTO implements Serializable {

    private Long id;

    private String description;

    private Long userChannelConfigurationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        ApplicationEventDTO applicationEventDTO = (ApplicationEventDTO) o;
        if (applicationEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationEventDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", userChannelConfiguration=" + getUserChannelConfigurationId() +
            "}";
    }
}
