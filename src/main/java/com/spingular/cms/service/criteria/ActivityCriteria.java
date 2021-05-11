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
 * Criteria class for the {@link com.spingular.cms.domain.Activity} entity. This class is used
 * in {@link com.spingular.cms.web.rest.ActivityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /activities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActivityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter activityName;

    private LongFilter appuserId;

    public ActivityCriteria() {}

    public ActivityCriteria(ActivityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.activityName = other.activityName == null ? null : other.activityName.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
    }

    @Override
    public ActivityCriteria copy() {
        return new ActivityCriteria(this);
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

    public StringFilter getActivityName() {
        return activityName;
    }

    public StringFilter activityName() {
        if (activityName == null) {
            activityName = new StringFilter();
        }
        return activityName;
    }

    public void setActivityName(StringFilter activityName) {
        this.activityName = activityName;
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
        final ActivityCriteria that = (ActivityCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(activityName, that.activityName) && Objects.equals(appuserId, that.appuserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activityName, appuserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (activityName != null ? "activityName=" + activityName + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            "}";
    }
}
