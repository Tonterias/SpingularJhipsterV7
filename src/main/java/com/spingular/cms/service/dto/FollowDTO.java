package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.spingular.cms.domain.Follow} entity.
 */
public class FollowDTO implements Serializable {

    private Long id;

    private Instant creationDate;

    private AppuserDTO followed;

    private AppuserDTO following;

    private CommunityDTO cfollowed;

    private CommunityDTO cfollowing;

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

    public AppuserDTO getFollowed() {
        return followed;
    }

    public void setFollowed(AppuserDTO followed) {
        this.followed = followed;
    }

    public AppuserDTO getFollowing() {
        return following;
    }

    public void setFollowing(AppuserDTO following) {
        this.following = following;
    }

    public CommunityDTO getCfollowed() {
        return cfollowed;
    }

    public void setCfollowed(CommunityDTO cfollowed) {
        this.cfollowed = cfollowed;
    }

    public CommunityDTO getCfollowing() {
        return cfollowing;
    }

    public void setCfollowing(CommunityDTO cfollowing) {
        this.cfollowing = cfollowing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FollowDTO)) {
            return false;
        }

        FollowDTO followDTO = (FollowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, followDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FollowDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", followed=" + getFollowed() +
            ", following=" + getFollowing() +
            ", cfollowed=" + getCfollowed() +
            ", cfollowing=" + getCfollowing() +
            "}";
    }
}
