package com.spingular.cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appuser.
 */
@Entity
@Table(name = "appuser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Appuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Size(max = 7500)
    @Column(name = "bio", length = 7500)
    private String bio;

    @Size(max = 50)
    @Column(name = "facebook", length = 50)
    private String facebook;

    @Size(max = 50)
    @Column(name = "twitter", length = 50)
    private String twitter;

    @Size(max = 50)
    @Column(name = "linkedin", length = 50)
    private String linkedin;

    @Size(max = 50)
    @Column(name = "instagram", length = 50)
    private String instagram;

    @Column(name = "birthdate")
    private Instant birthdate;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posts", "appuser", "community" }, allowSetters = true)
    private Set<Blog> blogs = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "blogs", "cfolloweds", "cfollowings", "cblockedusers", "cblockingusers", "appuser", "cinterests", "cactivities", "ccelebs",
        },
        allowSetters = true
    )
    private Set<Community> communities = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "post" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "appuser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comments", "appuser", "blog", "tags", "topics" }, allowSetters = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "followed")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "followed", "following", "cfollowed", "cfollowing" }, allowSetters = true)
    private Set<Follow> followeds = new HashSet<>();

    @OneToMany(mappedBy = "following")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "followed", "following", "cfollowed", "cfollowing" }, allowSetters = true)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "blockeduser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "blockeduser", "blockinguser", "cblockeduser", "cblockinguser" }, allowSetters = true)
    private Set<Blockuser> blockedusers = new HashSet<>();

    @OneToMany(mappedBy = "blockinguser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "blockeduser", "blockinguser", "cblockeduser", "cblockinguser" }, allowSetters = true)
    private Set<Blockuser> blockingusers = new HashSet<>();

    @JsonIgnoreProperties(value = { "appuser" }, allowSetters = true)
    @OneToOne(mappedBy = "appuser")
    private Appphoto appphoto;

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appusers" }, allowSetters = true)
    private Set<Interest> interests = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appusers" }, allowSetters = true)
    private Set<Activity> activities = new HashSet<>();

    @ManyToMany(mappedBy = "appusers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appusers" }, allowSetters = true)
    private Set<Celeb> celebs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appuser id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Appuser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getBio() {
        return this.bio;
    }

    public Appuser bio(String bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public Appuser facebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public Appuser twitter(String twitter) {
        this.twitter = twitter;
        return this;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return this.linkedin;
    }

    public Appuser linkedin(String linkedin) {
        this.linkedin = linkedin;
        return this;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public Appuser instagram(String instagram) {
        this.instagram = instagram;
        return this;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public Instant getBirthdate() {
        return this.birthdate;
    }

    public Appuser birthdate(Instant birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public void setBirthdate(Instant birthdate) {
        this.birthdate = birthdate;
    }

    public User getUser() {
        return this.user;
    }

    public Appuser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Blog> getBlogs() {
        return this.blogs;
    }

    public Appuser blogs(Set<Blog> blogs) {
        this.setBlogs(blogs);
        return this;
    }

    public Appuser addBlog(Blog blog) {
        this.blogs.add(blog);
        blog.setAppuser(this);
        return this;
    }

    public Appuser removeBlog(Blog blog) {
        this.blogs.remove(blog);
        blog.setAppuser(null);
        return this;
    }

    public void setBlogs(Set<Blog> blogs) {
        if (this.blogs != null) {
            this.blogs.forEach(i -> i.setAppuser(null));
        }
        if (blogs != null) {
            blogs.forEach(i -> i.setAppuser(this));
        }
        this.blogs = blogs;
    }

    public Set<Community> getCommunities() {
        return this.communities;
    }

    public Appuser communities(Set<Community> communities) {
        this.setCommunities(communities);
        return this;
    }

    public Appuser addCommunity(Community community) {
        this.communities.add(community);
        community.setAppuser(this);
        return this;
    }

    public Appuser removeCommunity(Community community) {
        this.communities.remove(community);
        community.setAppuser(null);
        return this;
    }

    public void setCommunities(Set<Community> communities) {
        if (this.communities != null) {
            this.communities.forEach(i -> i.setAppuser(null));
        }
        if (communities != null) {
            communities.forEach(i -> i.setAppuser(this));
        }
        this.communities = communities;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public Appuser notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public Appuser addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setAppuser(this);
        return this;
    }

    public Appuser removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setAppuser(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setAppuser(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setAppuser(this));
        }
        this.notifications = notifications;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public Appuser comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Appuser addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAppuser(this);
        return this;
    }

    public Appuser removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setAppuser(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setAppuser(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setAppuser(this));
        }
        this.comments = comments;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public Appuser posts(Set<Post> posts) {
        this.setPosts(posts);
        return this;
    }

    public Appuser addPost(Post post) {
        this.posts.add(post);
        post.setAppuser(this);
        return this;
    }

    public Appuser removePost(Post post) {
        this.posts.remove(post);
        post.setAppuser(null);
        return this;
    }

    public void setPosts(Set<Post> posts) {
        if (this.posts != null) {
            this.posts.forEach(i -> i.setAppuser(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setAppuser(this));
        }
        this.posts = posts;
    }

    public Set<Follow> getFolloweds() {
        return this.followeds;
    }

    public Appuser followeds(Set<Follow> follows) {
        this.setFolloweds(follows);
        return this;
    }

    public Appuser addFollowed(Follow follow) {
        this.followeds.add(follow);
        follow.setFollowed(this);
        return this;
    }

    public Appuser removeFollowed(Follow follow) {
        this.followeds.remove(follow);
        follow.setFollowed(null);
        return this;
    }

    public void setFolloweds(Set<Follow> follows) {
        if (this.followeds != null) {
            this.followeds.forEach(i -> i.setFollowed(null));
        }
        if (follows != null) {
            follows.forEach(i -> i.setFollowed(this));
        }
        this.followeds = follows;
    }

    public Set<Follow> getFollowings() {
        return this.followings;
    }

    public Appuser followings(Set<Follow> follows) {
        this.setFollowings(follows);
        return this;
    }

    public Appuser addFollowing(Follow follow) {
        this.followings.add(follow);
        follow.setFollowing(this);
        return this;
    }

    public Appuser removeFollowing(Follow follow) {
        this.followings.remove(follow);
        follow.setFollowing(null);
        return this;
    }

    public void setFollowings(Set<Follow> follows) {
        if (this.followings != null) {
            this.followings.forEach(i -> i.setFollowing(null));
        }
        if (follows != null) {
            follows.forEach(i -> i.setFollowing(this));
        }
        this.followings = follows;
    }

    public Set<Blockuser> getBlockedusers() {
        return this.blockedusers;
    }

    public Appuser blockedusers(Set<Blockuser> blockusers) {
        this.setBlockedusers(blockusers);
        return this;
    }

    public Appuser addBlockeduser(Blockuser blockuser) {
        this.blockedusers.add(blockuser);
        blockuser.setBlockeduser(this);
        return this;
    }

    public Appuser removeBlockeduser(Blockuser blockuser) {
        this.blockedusers.remove(blockuser);
        blockuser.setBlockeduser(null);
        return this;
    }

    public void setBlockedusers(Set<Blockuser> blockusers) {
        if (this.blockedusers != null) {
            this.blockedusers.forEach(i -> i.setBlockeduser(null));
        }
        if (blockusers != null) {
            blockusers.forEach(i -> i.setBlockeduser(this));
        }
        this.blockedusers = blockusers;
    }

    public Set<Blockuser> getBlockingusers() {
        return this.blockingusers;
    }

    public Appuser blockingusers(Set<Blockuser> blockusers) {
        this.setBlockingusers(blockusers);
        return this;
    }

    public Appuser addBlockinguser(Blockuser blockuser) {
        this.blockingusers.add(blockuser);
        blockuser.setBlockinguser(this);
        return this;
    }

    public Appuser removeBlockinguser(Blockuser blockuser) {
        this.blockingusers.remove(blockuser);
        blockuser.setBlockinguser(null);
        return this;
    }

    public void setBlockingusers(Set<Blockuser> blockusers) {
        if (this.blockingusers != null) {
            this.blockingusers.forEach(i -> i.setBlockinguser(null));
        }
        if (blockusers != null) {
            blockusers.forEach(i -> i.setBlockinguser(this));
        }
        this.blockingusers = blockusers;
    }

    public Appphoto getAppphoto() {
        return this.appphoto;
    }

    public Appuser appphoto(Appphoto appphoto) {
        this.setAppphoto(appphoto);
        return this;
    }

    public void setAppphoto(Appphoto appphoto) {
        if (this.appphoto != null) {
            this.appphoto.setAppuser(null);
        }
        if (appphoto != null) {
            appphoto.setAppuser(this);
        }
        this.appphoto = appphoto;
    }

    public Set<Interest> getInterests() {
        return this.interests;
    }

    public Appuser interests(Set<Interest> interests) {
        this.setInterests(interests);
        return this;
    }

    public Appuser addInterest(Interest interest) {
        this.interests.add(interest);
        interest.getAppusers().add(this);
        return this;
    }

    public Appuser removeInterest(Interest interest) {
        this.interests.remove(interest);
        interest.getAppusers().remove(this);
        return this;
    }

    public void setInterests(Set<Interest> interests) {
        if (this.interests != null) {
            this.interests.forEach(i -> i.removeAppuser(this));
        }
        if (interests != null) {
            interests.forEach(i -> i.addAppuser(this));
        }
        this.interests = interests;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public Appuser activities(Set<Activity> activities) {
        this.setActivities(activities);
        return this;
    }

    public Appuser addActivity(Activity activity) {
        this.activities.add(activity);
        activity.getAppusers().add(this);
        return this;
    }

    public Appuser removeActivity(Activity activity) {
        this.activities.remove(activity);
        activity.getAppusers().remove(this);
        return this;
    }

    public void setActivities(Set<Activity> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.removeAppuser(this));
        }
        if (activities != null) {
            activities.forEach(i -> i.addAppuser(this));
        }
        this.activities = activities;
    }

    public Set<Celeb> getCelebs() {
        return this.celebs;
    }

    public Appuser celebs(Set<Celeb> celebs) {
        this.setCelebs(celebs);
        return this;
    }

    public Appuser addCeleb(Celeb celeb) {
        this.celebs.add(celeb);
        celeb.getAppusers().add(this);
        return this;
    }

    public Appuser removeCeleb(Celeb celeb) {
        this.celebs.remove(celeb);
        celeb.getAppusers().remove(this);
        return this;
    }

    public void setCelebs(Set<Celeb> celebs) {
        if (this.celebs != null) {
            this.celebs.forEach(i -> i.removeAppuser(this));
        }
        if (celebs != null) {
            celebs.forEach(i -> i.addAppuser(this));
        }
        this.celebs = celebs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appuser)) {
            return false;
        }
        return id != null && id.equals(((Appuser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appuser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", bio='" + getBio() + "'" +
            ", facebook='" + getFacebook() + "'" +
            ", twitter='" + getTwitter() + "'" +
            ", linkedin='" + getLinkedin() + "'" +
            ", instagram='" + getInstagram() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            "}";
    }
}
