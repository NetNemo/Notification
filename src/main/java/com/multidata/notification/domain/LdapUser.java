package com.multidata.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A LdapUser.
 */
@Entity
@Table(name = "ldap_user")
public class LdapUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userID;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("userIDS")
    private UserChannelConfiguration userChannelConfiguration;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public LdapUser userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public LdapUser name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserChannelConfiguration getUserChannelConfiguration() {
        return userChannelConfiguration;
    }

    public LdapUser userChannelConfiguration(UserChannelConfiguration userChannelConfiguration) {
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
        LdapUser ldapUser = (LdapUser) o;
        if (ldapUser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ldapUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LdapUser{" +
            "id=" + getId() +
            ", userID='" + getUserID() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
