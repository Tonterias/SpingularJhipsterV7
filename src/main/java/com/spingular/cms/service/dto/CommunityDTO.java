package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Community} entity.
 */
public class CommunityDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 100)
    private String communityName;

    @NotNull
    @Size(min = 2, max = 7500)
    private String communityDescription;

    @Lob
    private byte[] image;

    private String imageContentType;
    private Boolean isActive;

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

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityDescription() {
        return communityDescription;
    }

    public void setCommunityDescription(String communityDescription) {
        this.communityDescription = communityDescription;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        if (!(o instanceof CommunityDTO)) {
            return false;
        }

        CommunityDTO communityDTO = (CommunityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, communityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommunityDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", communityName='" + getCommunityName() + "'" +
            ", communityDescription='" + getCommunityDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", appuser=" + getAppuser() +
            "}";
    }
}
