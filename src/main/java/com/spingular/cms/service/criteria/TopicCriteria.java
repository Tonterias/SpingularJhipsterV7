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
 * Criteria class for the {@link com.spingular.cms.domain.Topic} entity. This class is used
 * in {@link com.spingular.cms.web.rest.TopicResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /topics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TopicCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter topicName;

    private LongFilter postId;

    public TopicCriteria() {}

    public TopicCriteria(TopicCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.topicName = other.topicName == null ? null : other.topicName.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
    }

    @Override
    public TopicCriteria copy() {
        return new TopicCriteria(this);
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

    public StringFilter getTopicName() {
        return topicName;
    }

    public StringFilter topicName() {
        if (topicName == null) {
            topicName = new StringFilter();
        }
        return topicName;
    }

    public void setTopicName(StringFilter topicName) {
        this.topicName = topicName;
    }

    public LongFilter getPostId() {
        return postId;
    }

    public LongFilter postId() {
        if (postId == null) {
            postId = new LongFilter();
        }
        return postId;
    }

    public void setPostId(LongFilter postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TopicCriteria that = (TopicCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(topicName, that.topicName) && Objects.equals(postId, that.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topicName, postId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TopicCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (topicName != null ? "topicName=" + topicName + ", " : "") +
            (postId != null ? "postId=" + postId + ", " : "") +
            "}";
    }
}
