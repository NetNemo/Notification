package com.multidata.notification.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the EventAttach entity.
 */
public class EventAttachDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] file;
    private String fileContentType;

    private Long eventId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventAttachDTO eventAttachDTO = (EventAttachDTO) o;
        if (eventAttachDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventAttachDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventAttachDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", event=" + getEventId() +
            "}";
    }
}
