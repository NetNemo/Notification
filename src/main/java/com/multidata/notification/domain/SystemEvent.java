package com.multidata.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SystemEvent.
 */
@Entity
@Table(name = "system_event")
public class SystemEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "systemEvent")
    private Set<Event> events = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("systemEvents")
    private UserChannelConfiguration userChannelConfiguration;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public SystemEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public SystemEvent events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public SystemEvent addEvent(Event event) {
        this.events.add(event);
        event.setSystemEvent(this);
        return this;
    }

    public SystemEvent removeEvent(Event event) {
        this.events.remove(event);
        event.setSystemEvent(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public UserChannelConfiguration getUserChannelConfiguration() {
        return userChannelConfiguration;
    }

    public SystemEvent userChannelConfiguration(UserChannelConfiguration userChannelConfiguration) {
        this.userChannelConfiguration = userChannelConfiguration;
        return this;
    }

    public void setUserChannelConfiguration(UserChannelConfiguration userChannelConfiguration) {
        this.userChannelConfiguration = userChannelConfiguration;
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
        SystemEvent systemEvent = (SystemEvent) o;
        if (systemEvent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), systemEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SystemEvent{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
