package com.spingular.cms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.spingular.cms.domain.Cceleb} entity. This class is used
 * in {@link com.spingular.cms.web.rest.CcelebResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ccelebs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CcelebCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter celebName;

    private LongFilter communityId;

    public CcelebCriteria() {}

    public CcelebCriteria(CcelebCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.celebName = other.celebName == null ? null : other.celebName.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
    }

    @Override
    public CcelebCriteria copy() {
        return new CcelebCriteria(this);
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

    public StringFilter getCelebName() {
        return celebName;
    }

    public StringFilter celebName() {
        if (celebName == null) {
            celebName = new StringFilter();
        }
        return celebName;
    }

    public void setCelebName(StringFilter celebName) {
        this.celebName = celebName;
    }

    public LongFilter getCommunityId() {
        return communityId;
    }

    public LongFilter communityId() {
        if (communityId == null) {
            communityId = new LongFilter();
        }
        return communityId;
    }

    public void setCommunityId(LongFilter communityId) {
        this.communityId = communityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CcelebCriteria that = (CcelebCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(celebName, that.celebName) && Objects.equals(communityId, that.communityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, celebName, communityId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CcelebCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (celebName != null ? "celebName=" + celebName + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            "}";
    }
}
