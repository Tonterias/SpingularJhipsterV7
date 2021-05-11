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
 * Criteria class for the {@link com.spingular.cms.domain.Feedback} entity. This class is used
 * in {@link com.spingular.cms.web.rest.FeedbackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /feedbacks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FeedbackCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter name;

    private StringFilter email;

    private StringFilter feedback;

    public FeedbackCriteria() {}

    public FeedbackCriteria(FeedbackCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.feedback = other.feedback == null ? null : other.feedback.copy();
    }

    @Override
    public FeedbackCriteria copy() {
        return new FeedbackCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getFeedback() {
        return feedback;
    }

    public StringFilter feedback() {
        if (feedback == null) {
            feedback = new StringFilter();
        }
        return feedback;
    }

    public void setFeedback(StringFilter feedback) {
        this.feedback = feedback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FeedbackCriteria that = (FeedbackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(name, that.name) &&
            Objects.equals(email, that.email) &&
            Objects.equals(feedback, that.feedback)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, name, email, feedback);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (feedback != null ? "feedback=" + feedback + ", " : "") +
            "}";
    }
}
