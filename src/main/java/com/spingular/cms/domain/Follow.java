package com.spingular.cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Follow.
 */
@Entity
@Table(name = "follow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "user",
            "blogs",
            "communities",
            "notifications",
            "comments",
            "posts",
            "followeds",
            "followings",
            "blockedusers",
            "blockingusers",
            "appphoto",
            "interests",
            "activities",
            "celebs",
        },
        allowSetters = true
    )
    private Appuser followed;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "user",
            "blogs",
            "communities",
            "notifications",
            "comments",
            "posts",
            "followeds",
            "followings",
            "blockedusers",
            "blockingusers",
            "appphoto",
            "interests",
            "activities",
            "celebs",
        },
        allowSetters = true
    )
    private Appuser following;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "blogs", "cfolloweds", "cfollowings", "cblockedusers", "cblockingusers", "appuser", "cinterests", "cactivities", "ccelebs",
        },
        allowSetters = true
    )
    private Community cfollowed;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "blogs", "cfolloweds", "cfollowings", "cblockedusers", "cblockingusers", "appuser", "cinterests", "cactivities", "ccelebs",
        },
        allowSetters = true
    )
    private Community cfollowing;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Follow id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Follow creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Appuser getFollowed() {
        return this.followed;
    }

    public Follow followed(Appuser appuser) {
        this.setFollowed(appuser);
        return this;
    }

    public void setFollowed(Appuser appuser) {
        this.followed = appuser;
    }

    public Appuser getFollowing() {
        return this.following;
    }

    public Follow following(Appuser appuser) {
        this.setFollowing(appuser);
        return this;
    }

    public void setFollowing(Appuser appuser) {
        this.following = appuser;
    }

    public Community getCfollowed() {
        return this.cfollowed;
    }

    public Follow cfollowed(Community community) {
        this.setCfollowed(community);
        return this;
    }

    public void setCfollowed(Community community) {
        this.cfollowed = community;
    }

    public Community getCfollowing() {
        return this.cfollowing;
    }

    public Follow cfollowing(Community community) {
        this.setCfollowing(community);
        return this;
    }

    public void setCfollowing(Community community) {
        this.cfollowing = community;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Follow)) {
            return false;
        }
        return id != null && id.equals(((Follow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Follow{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
