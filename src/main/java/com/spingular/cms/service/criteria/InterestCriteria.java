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
 * Criteria class for the {@link com.spingular.cms.domain.Interest} entity. This class is used
 * in {@link com.spingular.cms.web.rest.InterestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InterestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter interestName;

    private LongFilter appuserId;

    public InterestCriteria() {}

    public InterestCriteria(InterestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.interestName = other.interestName == null ? null : other.interestName.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
    }

    @Override
    public InterestCriteria copy() {
        return new InterestCriteria(this);
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

    public StringFilter getInterestName() {
        return interestName;
    }

    public StringFilter interestName() {
        if (interestName == null) {
            interestName = new StringFilter();
        }
        return interestName;
    }

    public void setInterestName(StringFilter interestName) {
        this.interestName = interestName;
    }

    public LongFilter getAppuserId() {
        return appuserId;
    }

    public LongFilter appuserId() {
        if (appuserId == null) {
            appuserId = new LongFilter();
        }
        return appuserId;
    }

    public void setAppuserId(LongFilter appuserId) {
        this.appuserId = appuserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterestCriteria that = (InterestCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(interestName, that.interestName) && Objects.equals(appuserId, that.appuserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interestName, appuserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (interestName != null ? "interestName=" + interestName + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            "}";
    }
}
