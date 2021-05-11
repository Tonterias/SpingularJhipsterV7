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
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "publication_date")
    private Instant publicationDate;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "headline", length = 100, nullable = false)
    private String headline;

    @Size(min = 2, max = 1000)
    @Column(name = "leadtext", length = 1000)
    private String leadtext;

    @NotNull
    @Size(min = 2, max = 65000)
    @Column(name = "bodytext", length = 65000, nullable = false)
    private String bodytext;

    @Size(min = 2, max = 1000)
    @Column(name = "quote", length = 1000)
    private String quote;

    @Size(min = 2, max = 2000)
    @Column(name = "conclusion", length = 2000)
    private String conclusion;

    @Size(min = 2, max = 1000)
    @Column(name = "link_text", length = 1000)
    private String linkText;

    @Size(min = 2, max = 1000)
    @Column(name = "link_url", length = 1000)
    private String linkURL;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appuser", "post" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "user",
            "blogs",
            "communities",
            "notifications",
            "comments",
            "posts",
            "followeds",
            "followings",
            "blockedusers",
            "blockingusers",
            "appphoto",
            "interests",
            "activities",
            "celebs",
        },
        allowSetters = true
    )
    private Appuser appuser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "posts", "appuser", "community" }, allowSetters = true)
    private Blog blog;

    @ManyToMany(mappedBy = "posts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posts" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "posts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "posts" }, allowSetters = true)
    private Set<Topic> topics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Post creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getPublicationDate() {
        return this.publicationDate;
    }

    public Post publicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(Instant publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getHeadline() {
        return this.headline;
    }

    public Post headline(String headline) {
        this.headline = headline;
        return this;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getLeadtext() {
        return this.leadtext;
    }

    public Post leadtext(String leadtext) {
        this.leadtext = leadtext;
        return this;
    }

    public void setLeadtext(String leadtext) {
        this.leadtext = leadtext;
    }

    public String getBodytext() {
        return this.bodytext;
    }

    public Post bodytext(String bodytext) {
        this.bodytext = bodytext;
        return this;
    }

    public void setBodytext(String bodytext) {
        this.bodytext = bodytext;
    }

    public String getQuote() {
        return this.quote;
    }

    public Post quote(String quote) {
        this.quote = quote;
        return this;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getConclusion() {
        return this.conclusion;
    }

    public Post conclusion(String conclusion) {
        this.conclusion = conclusion;
        return this;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getLinkText() {
        return this.linkText;
    }

    public Post linkText(String linkText) {
        this.linkText = linkText;
        return this;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkURL() {
        return this.linkURL;
    }

    public Post linkURL(String linkURL) {
        this.linkURL = linkURL;
        return this;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Post image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Post imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public Post comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Post addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPost(this);
        return this;
    }

    public Post removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setPost(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setPost(this));
        }
        this.comments = comments;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public Post appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    public Blog getBlog() {
        return this.blog;
    }

    public Post blog(Blog blog) {
        this.setBlog(blog);
        return this;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public Post tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Post addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPosts().add(this);
        return this;
    }

    public Post removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPosts().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.removePost(this));
        }
        if (tags != null) {
            tags.forEach(i -> i.addPost(this));
        }
        this.tags = tags;
    }

    public Set<Topic> getTopics() {
        return this.topics;
    }

    public Post topics(Set<Topic> topics) {
        this.setTopics(topics);
        return this;
    }

    public Post addTopic(Topic topic) {
        this.topics.add(topic);
        topic.getPosts().add(this);
        return this;
    }

    public Post removeTopic(Topic topic) {
        this.topics.remove(topic);
        topic.getPosts().remove(this);
        return this;
    }

    public void setTopics(Set<Topic> topics) {
        if (this.topics != null) {
            this.topics.forEach(i -> i.removePost(this));
        }
        if (topics != null) {
            topics.forEach(i -> i.addPost(this));
        }
        this.topics = topics;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", headline='" + getHeadline() + "'" +
            ", leadtext='" + getLeadtext() + "'" +
            ", bodytext='" + getBodytext() + "'" +
            ", quote='" + getQuote() + "'" +
            ", conclusion='" + getConclusion() + "'" +
            ", linkText='" + getLinkText() + "'" +
            ", linkURL='" + getLinkURL() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
