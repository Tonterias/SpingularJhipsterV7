package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Appphoto} entity.
 */
public class AppphotoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @Lob
    private byte[] image;

    private String imageContentType;
    private AppuserDTO appuser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppphotoDTO)) {
            return false;
        }

        AppphotoDTO appphotoDTO = (AppphotoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appphotoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppphotoDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", image='" + getImage() + "'" +
            ", appuser=" + getAppuser() +
            "}";
    }
}
