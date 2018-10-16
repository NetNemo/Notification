package com.multidata.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.multidata.notification.domain.enumeration.Channel;

/**
 * A DeliveryChannel.
 */
@Entity
@Table(name = "delivery_channel")
public class DeliveryChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private Channel type;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "deliveryChannel")
    private Set<UserChannelConfiguration> userChannelConfigurations = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Channel getType() {
        return type;
    }

    public DeliveryChannel type(Channel type) {
        this.type = type;
        return this;
    }

    public void setType(Channel type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public DeliveryChannel description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserChannelConfiguration> getUserChannelConfigurations() {
        return userChannelConfigurations;
    }

    public DeliveryChannel userChannelConfigurations(Set<UserChannelConfiguration> userChannelConfigurations) {
        this.userChannelConfigurations = userChannelConfigurations;
        return this;
    }

    public DeliveryChannel addUserChannelConfiguration(UserChannelConfiguration userChannelConfiguration) {
        this.userChannelConfigurations.add(userChannelConfiguration);
        userChannelConfiguration.setDeliveryChannel(this);
        return this;
    }

    public DeliveryChannel removeUserChannelConfiguration(UserChannelConfiguration userChannelConfiguration) {
        this.userChannelConfigurations.remove(userChannelConfiguration);
        userChannelConfiguration.setDeliveryChannel(null);
        return this;
    }

    public void setUserChannelConfigurations(Set<UserChannelConfiguration> userChannelConfigurations) {
        this.userChannelConfigurations = userChannelConfigurations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeliveryChannel deliveryChannel = (DeliveryChannel) o;
        if (deliveryChannel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deliveryChannel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeliveryChannel{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
