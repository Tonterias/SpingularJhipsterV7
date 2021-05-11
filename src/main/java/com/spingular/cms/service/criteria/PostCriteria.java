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
 * Criteria class for the {@link com.spingular.cms.domain.Post} entity. This class is used
 * in {@link com.spingular.cms.web.rest.PostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private InstantFilter publicationDate;

    private StringFilter headline;

    private StringFilter leadtext;

    private StringFilter bodytext;

    private StringFilter quote;

    private StringFilter conclusion;

    private StringFilter linkText;

    private StringFilter linkURL;

    private LongFilter commentId;

    private LongFilter appuserId;

    private LongFilter blogId;

    private LongFilter tagId;

    private LongFilter topicId;

    public PostCriteria() {}

    public PostCriteria(PostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.publicationDate = other.publicationDate == null ? null : other.publicationDate.copy();
        this.headline = other.headline == null ? null : other.headline.copy();
        this.leadtext = other.leadtext == null ? null : other.leadtext.copy();
        this.bodytext = other.bodytext == null ? null : other.bodytext.copy();
        this.quote = other.quote == null ? null : other.quote.copy();
        this.conclusion = other.conclusion == null ? null : other.conclusion.copy();
        this.linkText = other.linkText == null ? null : other.linkText.copy();
        this.linkURL = other.linkURL == null ? null : other.linkURL.copy();
        this.commentId = other.commentId == null ? null : other.commentId.copy();
        this.appuserId = other.appuserId == null ? null : other.appuserId.copy();
        this.blogId = other.blogId == null ? null : other.blogId.copy();
        this.tagId = other.tagId == null ? null : other.tagId.copy();
        this.topicId = other.topicId == null ? null : other.topicId.copy();
    }

    @Override
    public PostCriteria copy() {
        return new PostCriteria(this);
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

    public InstantFilter getPublicationDate() {
        return publicationDate;
    }

    public InstantFilter publicationDate() {
        if (publicationDate == null) {
            publicationDate = new InstantFilter();
        }
        return publicationDate;
    }

    public void setPublicationDate(InstantFilter publicationDate) {
        this.publicationDate = publicationDate;
    }

    public StringFilter getHeadline() {
        return headline;
    }

    public StringFilter headline() {
        if (headline == null) {
            headline = new StringFilter();
        }
        return headline;
    }

    public void setHeadline(StringFilter headline) {
        this.headline = headline;
    }

    public StringFilter getLeadtext() {
        return leadtext;
    }

    public StringFilter leadtext() {
        if (leadtext == null) {
            leadtext = new StringFilter();
        }
        return leadtext;
    }

    public void setLeadtext(StringFilter leadtext) {
        this.leadtext = leadtext;
    }

    public StringFilter getBodytext() {
        return bodytext;
    }

    public StringFilter bodytext() {
        if (bodytext == null) {
            bodytext = new StringFilter();
        }
        return bodytext;
    }

    public void setBodytext(StringFilter bodytext) {
        this.bodytext = bodytext;
    }

    public StringFilter getQuote() {
        return quote;
    }

    public StringFilter quote() {
        if (quote == null) {
            quote = new StringFilter();
        }
        return quote;
    }

    public void setQuote(StringFilter quote) {
        this.quote = quote;
    }

    public StringFilter getConclusion() {
        return conclusion;
    }

    public StringFilter conclusion() {
        if (conclusion == null) {
            conclusion = new StringFilter();
        }
        return conclusion;
    }

    public void setConclusion(StringFilter conclusion) {
        this.conclusion = conclusion;
    }

    public StringFilter getLinkText() {
        return linkText;
    }

    public StringFilter linkText() {
        if (linkText == null) {
            linkText = new StringFilter();
        }
        return linkText;
    }

    public void setLinkText(StringFilter linkText) {
        this.linkText = linkText;
    }

    public StringFilter getLinkURL() {
        return linkURL;
    }

    public StringFilter linkURL() {
        if (linkURL == null) {
            linkURL = new StringFilter();
        }
        return linkURL;
    }

    public void setLinkURL(StringFilter linkURL) {
        this.linkURL = linkURL;
    }

    public LongFilter getCommentId() {
        return commentId;
    }

    public LongFilter commentId() {
        if (commentId == null) {
            commentId = new LongFilter();
        }
        return commentId;
    }

    public void setCommentId(LongFilter commentId) {
        this.commentId = commentId;
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

    public LongFilter getBlogId() {
        return blogId;
    }

    public LongFilter blogId() {
        if (blogId == null) {
            blogId = new LongFilter();
        }
        return blogId;
    }

    public void setBlogId(LongFilter blogId) {
        this.blogId = blogId;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public LongFilter tagId() {
        if (tagId == null) {
            tagId = new LongFilter();
        }
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    public LongFilter getTopicId() {
        return topicId;
    }

    public LongFilter topicId() {
        if (topicId == null) {
            topicId = new LongFilter();
        }
        return topicId;
    }

    public void setTopicId(LongFilter topicId) {
        this.topicId = topicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PostCriteria that = (PostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(publicationDate, that.publicationDate) &&
            Objects.equals(headline, that.headline) &&
            Objects.equals(leadtext, that.leadtext) &&
            Objects.equals(bodytext, that.bodytext) &&
            Objects.equals(quote, that.quote) &&
            Objects.equals(conclusion, that.conclusion) &&
            Objects.equals(linkText, that.linkText) &&
            Objects.equals(linkURL, that.linkURL) &&
            Objects.equals(commentId, that.commentId) &&
            Objects.equals(appuserId, that.appuserId) &&
            Objects.equals(blogId, that.blogId) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(topicId, that.topicId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creationDate,
            publicationDate,
            headline,
            leadtext,
            bodytext,
            quote,
            conclusion,
            linkText,
            linkURL,
            commentId,
            appuserId,
            blogId,
            tagId,
            topicId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (publicationDate != null ? "publicationDate=" + publicationDate + ", " : "") +
            (headline != null ? "headline=" + headline + ", " : "") +
            (leadtext != null ? "leadtext=" + leadtext + ", " : "") +
            (bodytext != null ? "bodytext=" + bodytext + ", " : "") +
            (quote != null ? "quote=" + quote + ", " : "") +
            (conclusion != null ? "conclusion=" + conclusion + ", " : "") +
            (linkText != null ? "linkText=" + linkText + ", " : "") +
            (linkURL != null ? "linkURL=" + linkURL + ", " : "") +
            (commentId != null ? "commentId=" + commentId + ", " : "") +
            (appuserId != null ? "appuserId=" + appuserId + ", " : "") +
            (blogId != null ? "blogId=" + blogId + ", " : "") +
            (tagId != null ? "tagId=" + tagId + ", " : "") +
            (topicId != null ? "topicId=" + topicId + ", " : "") +
            "}";
    }
}
