package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant creationDate;

    @NotNull
    @Size(min = 2, max = 65000)
    private String commentText;

    private Boolean isOffensive;

    private AppuserDTO appuser;

    private PostDTO post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Boolean getIsOffensive() {
        return isOffensive;
    }

    public void setIsOffensive(Boolean isOffensive) {
        this.isOffensive = isOffensive;
    }

    public AppuserDTO getAppuser() {
        return appuser;
    }

    public void setAppuser(AppuserDTO appuser) {
        this.appuser = appuser;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", commentText='" + getCommentText() + "'" +
            ", isOffensive='" + getIsOffensive() + "'" +
            ", appuser=" + getAppuser() +
            ", post=" + getPost() +
            "}";
    }
}
