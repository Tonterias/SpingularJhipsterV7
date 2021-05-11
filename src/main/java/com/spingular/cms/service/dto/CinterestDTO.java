package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Cinterest} entity.
 */
public class CinterestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    private String interestName;

    private Set<CommunityDTO> communities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public Set<CommunityDTO> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<CommunityDTO> communities) {
        this.communities = communities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CinterestDTO)) {
            return false;
        }

        CinterestDTO cinterestDTO = (CinterestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cinterestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CinterestDTO{" +
            "id=" + getId() +
            ", interestName='" + getInterestName() + "'" +
            ", communities=" + getCommunities() +
            "}";
    }
}
