package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Urllink} entity.
 */
public class UrllinkDTO implements Serializable {

    private Long id;

    @NotNull
    private String linkText;

    @NotNull
    private String linkURL;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UrllinkDTO)) {
            return false;
        }

        UrllinkDTO urllinkDTO = (UrllinkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, urllinkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UrllinkDTO{" +
            "id=" + getId() +
            ", linkText='" + getLinkText() + "'" +
            ", linkURL='" + getLinkURL() + "'" +
            "}";
    }
}
