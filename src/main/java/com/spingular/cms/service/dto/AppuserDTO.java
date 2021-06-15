package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Appuser} entity.
 */
public class AppuserDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @Size(max = 7500)
    private String bio;

    @Size(max = 50)
    private String facebook;

    @Size(max = 50)
    private String twitter;

    @Size(max = 50)
    private String linkedin;

    @Size(max = 50)
    private String instagram;

    private Instant birthdate;

    private UserDTO user;

    private String userFirstName;

    private String userLastName;

    @Lob
    private byte[] appPhotoImage;

    private String appPhotoImageContentType;

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public Instant getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Instant birthdate) {
        this.birthdate = birthdate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public byte[] getAppPhotoImage() {
        return appPhotoImage;
    }

    public void setAppPhotoImage(byte[] appPhotoImage) {
        this.appPhotoImage = appPhotoImage;
    }

    public String getAppPhotoImageContentType() {
        return appPhotoImageContentType;
    }

    public void setAppPhotoImageContentType(String appPhotoImageContentType) {
        this.appPhotoImageContentType = appPhotoImageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppuserDTO)) {
            return false;
        }

        AppuserDTO appuserDTO = (AppuserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appuserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppuserDTO{" + "id=" + getId() + ", userFirstName='" + getUserFirstName() + "'" + ", userLastName='"
                + getUserLastName() + "'" + ", creationDate='" + getCreationDate() + "'" + ", appPhotoImage='" + getAppPhotoImage()
                + "'" + ", appPhotoImageContentType='" + getAppPhotoImageContentType() + "'" + ", bio='" + getBio() + "'" + ", facebook='" + getFacebook() + "'" + ", twitter='" + getTwitter()
                + "'" + ", linkedin='" + getLinkedin() + "'" + ", instagram='" + getInstagram() + "'" + ", birthdate='"
                + getBirthdate() + "'" + ", user=" + getUser() + "}";
    }
}
