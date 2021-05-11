package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Cceleb} entity.
 */
public class CcelebDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    private String celebName;

    private Set<CommunityDTO> communities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCelebName() {
        return celebName;
    }

    public void setCelebName(String celebName) {
        this.celebName = celebName;
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
        if (!(o instanceof CcelebDTO)) {
            return false;
        }

        CcelebDTO ccelebDTO = (CcelebDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ccelebDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CcelebDTO{" +
            "id=" + getId() +
            ", celebName='" + getCelebName() + "'" +
            ", communities=" + getCommunities() +
            "}";
    }
}
