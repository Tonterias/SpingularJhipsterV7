package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Blog} entity.
 */
public class BlogDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    @Lob
    private byte[] image;

    private String imageContentType;
    private AppuserDTO appuser;

    private CommunityDTO community;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public CommunityDTO getCommunity() {
        return community;
    }

    public void setCommunity(CommunityDTO community) {
        this.community = community;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlogDTO)) {
            return false;
        }

        BlogDTO blogDTO = (BlogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlogDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", title='" + getTitle() + "'" +
            ", image='" + getImage() + "'" +
            ", appuser=" + getAppuser() +
            ", community=" + getCommunity() +
            "}";
    }
}
