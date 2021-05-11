package com.spingular.cms.service.criteria;

import com.spingular.cms.domain.enumeration.NotificationReason;
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
 * Criteria class for the {@link com.spingular.cms.domain.Notification} entity. This class is used
 * in {@link com.spingular.cms.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationReason
     */
    public static class NotificationReasonFilter extends Filter<NotificationReason> {

        public NotificationReasonFilter() {}

        public NotificationReasonFilter(NotificationReasonFilter filter) {
            super(filter);
        }

        @Override
        public NotificationReasonFilter copy() {
            return new NotificationReasonFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private InstantFilter notificationDate;

    private NotificationReasonFilter notificationReason;

    private StringFilter notificationText;

    private BooleanFilter isDelivered;

    private LongFilter appuserId;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.notificationDate = other.notificationDate == null ? null : other.notificationDate.copy();
        this.notificationReason = other.notificationReason == null ? null : other.notificationReason.copy();
        this.notificationText = other.notificationText == null ? null : other.notificationText.copy();
        this.isDelivered = other.isDelivered == null ? null : other.isDelivered.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public InstantFilter getNotificationDate() {
        return notificationDate;
    }

    public InstantFilter notificationDate() {
        if (notificationDate == null) {
            notificationDate = new InstantFilter();
        }
        return notificationDate;
    }

    public void setNotificationDate(InstantFilter notificationDate) {
        this.notificationDate = notificationDate;
    }

    public NotificationReasonFilter getNotificationReason() {
        return notificationReason;
    }

    public NotificationReasonFilter notificationReason() {
        if (notificationReason == null) {
            notificationReason = new NotificationReasonFilter();
        }
        return notificationReason;
    }

    public void setNotificationReason(NotificationReasonFilter notificationReason) {
        this.notificationReason = notificationReason;
    }

    public StringFilter getNotificationText() {
        return notificationText;
    }

    public StringFilter notificationText() {
        if (notificationText == null) {
            notificationText = new StringFilter();
        }
        return notificationText;
    }

    public void setNotificationText(StringFilter notificationText) {
        this.notificationText = notificationText;
    }

    public BooleanFilter getIsDelivered() {
        return isDelivered;
    }

    public BooleanFilter isDelivered() {
        if (isDelivered == null) {
            isDelivered = new BooleanFilter();
        }
        return isDelivered;
    }

    public void setIsDelivered(BooleanFilter isDelivered) {
        this.isDelivered = isDelivered;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(notificationDate, that.notificationDate) &&
            Objects.equals(notificationReason, that.notificationReason) &&
            Objects.equals(notificationText, that.notificationText) &&
            Objects.equals(isDelivered, that.isDelivered) &&
            Objects.equals(appuserId, that.appuserId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, notificationDate, notificationReason, notificationText, isDelivered, appuserId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (notificationDate != null ? "notificationDate=" + notificationDate + ", " : "") +
            (notificationReason != null ? "notificationReason=" + notificationReason + ", " : "") +
            (notificationText != null ? "notificationText=" + notificationText + ", " : "") +
            (isDelivered != null ? "isDelivered=" + isDelivered + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            "}";
    }
}
