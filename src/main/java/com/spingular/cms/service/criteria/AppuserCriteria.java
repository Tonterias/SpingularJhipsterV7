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
 * Criteria class for the {@link com.spingular.cms.domain.Appuser} entity. This class is used
 * in {@link com.spingular.cms.web.rest.AppuserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appusers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppuserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter creationDate;

    private StringFilter bio;

    private StringFilter facebook;

    private StringFilter twitter;

    private StringFilter linkedin;

    private StringFilter instagram;

    private InstantFilter birthdate;

    private LongFilter userId;

    private LongFilter blogId;

    private LongFilter communityId;

    private LongFilter notificationId;

    private LongFilter commentId;

    private LongFilter postId;

    private LongFilter followedId;

    private LongFilter followingId;

    private LongFilter blockeduserId;

    private LongFilter blockinguserId;

    private LongFilter appphotoId;

    private LongFilter interestId;

    private LongFilter activityId;

    private LongFilter celebId;

    public AppuserCriteria() {}

    public AppuserCriteria(AppuserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.bio = other.bio == null ? null : other.bio.copy();
        this.facebook = other.facebook == null ? null : other.facebook.copy();
        this.twitter = other.twitter == null ? null : other.twitter.copy();
        this.linkedin = other.linkedin == null ? null : other.linkedin.copy();
        this.instagram = other.instagram == null ? null : other.instagram.copy();
        this.birthdate = other.birthdate == null ? null : other.birthdate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.blogId = other.blogId == null ? null : other.blogId.copy();
        this.communityId = other.communityId == null ? null : other.communityId.copy();
        this.notificationId = other.notificationId == null ? null : other.notificationId.copy();
        this.commentId = other.commentId == null ? null : other.commentId.copy();
        this.postId = other.postId == null ? null : other.postId.copy();
        this.followedId = other.followedId == null ? null : other.followedId.copy();
        this.followingId = other.followingId == null ? null : other.followingId.copy();
        this.blockeduserId = other.blockeduserId == null ? null : other.blockeduserId.copy();
        this.blockinguserId = other.blockinguserId == null ? null : other.blockinguserId.copy();
        this.appphotoId = other.appphotoId == null ? null : other.appphotoId.copy();
        this.interestId = other.interestId == null ? null : other.interestId.copy();
        this.activityId = other.activityId == null ? null : other.activityId.copy();
        this.celebId = other.celebId == null ? null : other.celebId.copy();
    }

    @Override
    public AppuserCriteria copy() {
        return new AppuserCriteria(this);
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

    public StringFilter getBio() {
        return bio;
    }

    public StringFilter bio() {
        if (bio == null) {
            bio = new StringFilter();
        }
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
    }

    public StringFilter getFacebook() {
        return facebook;
    }

    public StringFilter facebook() {
        if (facebook == null) {
            facebook = new StringFilter();
        }
        return facebook;
    }

    public void setFacebook(StringFilter facebook) {
        this.facebook = facebook;
    }

    public StringFilter getTwitter() {
        return twitter;
    }

    public StringFilter twitter() {
        if (twitter == null) {
            twitter = new StringFilter();
        }
        return twitter;
    }

    public void setTwitter(StringFilter twitter) {
        this.twitter = twitter;
    }

    public StringFilter getLinkedin() {
        return linkedin;
    }

    public StringFilter linkedin() {
        if (linkedin == null) {
            linkedin = new StringFilter();
        }
        return linkedin;
    }

    public void setLinkedin(StringFilter linkedin) {
        this.linkedin = linkedin;
    }

    public StringFilter getInstagram() {
        return instagram;
    }

    public StringFilter instagram() {
        if (instagram == null) {
            instagram = new StringFilter();
        }
        return instagram;
    }

    public void setInstagram(StringFilter instagram) {
        this.instagram = instagram;
    }

    public InstantFilter getBirthdate() {
        return birthdate;
    }

    public InstantFilter birthdate() {
        if (birthdate == null) {
            birthdate = new InstantFilter();
        }
        return birthdate;
    }

    public void setBirthdate(InstantFilter birthdate) {
        this.birthdate = birthdate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public LongFilter getNotificationId() {
        return notificationId;
    }

    public LongFilter notificationId() {
        if (notificationId == null) {
            notificationId = new LongFilter();
        }
        return notificationId;
    }

    public void setNotificationId(LongFilter notificationId) {
        this.notificationId = notificationId;
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

    public LongFilter getBlockeduserId() {
        return blockeduserId;
    }

    public LongFilter blockeduserId() {
        if (blockeduserId == null) {
            blockeduserId = new LongFilter();
        }
        return blockeduserId;
    }

    public void setBlockeduserId(LongFilter blockeduserId) {
        this.blockeduserId = blockeduserId;
    }

    public LongFilter getBlockinguserId() {
        return blockinguserId;
    }

    public LongFilter blockinguserId() {
        if (blockinguserId == null) {
            blockinguserId = new LongFilter();
        }
        return blockinguserId;
    }

    public void setBlockinguserId(LongFilter blockinguserId) {
        this.blockinguserId = blockinguserId;
    }

    public LongFilter getAppphotoId() {
        return appphotoId;
    }

    public LongFilter appphotoId() {
        if (appphotoId == null) {
            appphotoId = new LongFilter();
        }
        return appphotoId;
    }

    public void setAppphotoId(LongFilter appphotoId) {
        this.appphotoId = appphotoId;
    }

    public LongFilter getInterestId() {
        return interestId;
    }

    public LongFilter interestId() {
        if (interestId == null) {
            interestId = new LongFilter();
        }
        return interestId;
    }

    public void setInterestId(LongFilter interestId) {
        this.interestId = interestId;
    }

    public LongFilter getActivityId() {
        return activityId;
    }

    public LongFilter activityId() {
        if (activityId == null) {
            activityId = new LongFilter();
        }
        return activityId;
    }

    public void setActivityId(LongFilter activityId) {
        this.activityId = activityId;
    }

    public LongFilter getCelebId() {
        return celebId;
    }

    public LongFilter celebId() {
        if (celebId == null) {
            celebId = new LongFilter();
        }
        return celebId;
    }

    public void setCelebId(LongFilter celebId) {
        this.celebId = celebId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppuserCriteria that = (AppuserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(facebook, that.facebook) &&
            Objects.equals(twitter, that.twitter) &&
            Objects.equals(linkedin, that.linkedin) &&
            Objects.equals(instagram, that.instagram) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(blogId, that.blogId) &&
            Objects.equals(communityId, that.communityId) &&
            Objects.equals(notificationId, that.notificationId) &&
            Objects.equals(commentId, that.commentId) &&
            Objects.equals(postId, that.postId) &&
            Objects.equals(followedId, that.followedId) &&
            Objects.equals(followingId, that.followingId) &&
            Objects.equals(blockeduserId, that.blockeduserId) &&
            Objects.equals(blockinguserId, that.blockinguserId) &&
            Objects.equals(appphotoId, that.appphotoId) &&
            Objects.equals(interestId, that.interestId) &&
            Objects.equals(activityId, that.activityId) &&
            Objects.equals(celebId, that.celebId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            creationDate,
            bio,
            facebook,
            twitter,
            linkedin,
            instagram,
            birthdate,
            userId,
            blogId,
            communityId,
            notificationId,
            commentId,
            postId,
            followedId,
            followingId,
            blockeduserId,
            blockinguserId,
            appphotoId,
            interestId,
            activityId,
            celebId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppuserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (creationDate != null ? "creationDate=" + creationDate + ", " : "") +
            (bio != null ? "bio=" + bio + ", " : "") +
            (facebook != null ? "facebook=" + facebook + ", " : "") +
            (twitter != null ? "twitter=" + twitter + ", " : "") +
            (linkedin != null ? "linkedin=" + linkedin + ", " : "") +
            (instagram != null ? "instagram=" + instagram + ", " : "") +
            (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (blogId != null ? "blogId=" + blogId + ", " : "") +
            (communityId != null ? "communityId=" + communityId + ", " : "") +
            (notificationId != null ? "notificationId=" + notificationId + ", " : "") +
            (commentId != null ? "commentId=" + commentId + ", " : "") +
            (postId != null ? "postId=" + postId + ", " : "") +
            (followedId != null ? "followedId=" + followedId + ", " : "") +
            (followingId != null ? "followingId=" + followingId + ", " : "") +
            (blockeduserId != null ? "blockeduserId=" + blockeduserId + ", " : "") +
            (blockinguserId != null ? "blockinguserId=" + blockinguserId + ", " : "") +
            (appphotoId != null ? "appphotoId=" + appphotoId + ", " : "") +
            (interestId != null ? "interestId=" + interestId + ", " : "") +
            (activityId != null ? "activityId=" + activityId + ", " : "") +
            (celebId != null ? "celebId=" + celebId + ", " : "") +
            "}";
    }
}
