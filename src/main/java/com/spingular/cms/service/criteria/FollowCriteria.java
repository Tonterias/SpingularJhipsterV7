package com.spingular.cms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.spingular.cms.domain.Follow} entity. This class is used
 * in {@link com.spingular.cms.web.rest.FollowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /follows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FollowCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private LongFilter followedId;

    private LongFilter followingId;

    private LongFilter cfollowedId;

    private LongFilter cfollowingId;

    public FollowCriteria() {}

    public FollowCriteria(FollowCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.followedId = other.followedId == null ? null : other.followedId.copy();
        this.followingId = other.followingId == null ? null : other.followingId.copy();
        this.cfollowedId = other.cfollowedId == null ? null : other.cfollowedId.copy();
        this.cfollowingId = other.cfollowingId == null ? null : other.cfollowingId.copy();
    }

    @Override
    public FollowCriteria copy() {
        return new FollowCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public InstantFilter creationDate() {
        if (creationDate == null) {
            creationDate = new InstantFilter();
        }
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    public LongFilter getFollowedId() {
        return followedId;
    }

    public LongFilter followedId() {
        if (followedId == null) {
            followedId = new LongFilter();
        }
        return followedId;
    }

    public void setFollowedId(LongFilter followedId) {
        this.followedId = followedId;
    }

    public LongFilter getFollowingId() {
        return followingId;
    }

    public LongFilter followingId() {
        if (followingId == null) {
            followingId = new LongFilter();
        }
        return followingId;
    }

    public void setFollowingId(LongFilter followingId) {
        this.followingId = followingId;
    }

    public LongFilter getCfollowedId() {
        return cfollowedId;
    }

    public LongFilter cfollowedId() {
        if (cfollowedId == null) {
            cfollowedId = new LongFilter();
        }
        return cfollowedId;
    }

    public void setCfollowedId(LongFilter cfollowedId) {
        this.cfollowedId = cfollowedId;
    }

    public LongFilter getCfollowingId() {
        return cfollowingId;
    }

    public LongFilter cfollowingId() {
        if (cfollowingId == null) {
            cfollowingId = new LongFilter();
        }
        return cfollowingId;
    }

    public void setCfollowingId(LongFilter cfollowingId) {
        this.cfollowingId = cfollowingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FollowCriteria that = (FollowCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(followedId, that.followedId) &&
            Objects.equals(followingId, that.followingId) &&
            Objects.equals(cfollowedId, that.cfollowedId) &&
            Objects.equals(cfollowingId, that.cfollowingId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, followedId, followingId, cfollowedId, cfollowingId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FollowCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (followedId != null ? "followedId=" + followedId + ", " : "") +
            (followingId != null ? "followingId=" + followingId + ", " : "") +
            (cfollowedId != null ? "cfollowedId=" + cfollowedId + ", " : "") +
            (cfollowingId != null ? "cfollowingId=" + cfollowingId + ", " : "") +
            "}";
    }
}
