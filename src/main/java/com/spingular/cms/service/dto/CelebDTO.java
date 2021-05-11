package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Celeb} entity.
 */
public class CelebDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    private String celebName;

    private Set<AppuserDTO> appusers = new HashSet<>();

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

    public Set<AppuserDTO> getAppusers() {
        return appusers;
    }

    public void setAppusers(Set<AppuserDTO> appusers) {
        this.appusers = appusers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CelebDTO)) {
            return false;
        }

        CelebDTO celebDTO = (CelebDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, celebDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CelebDTO{" +
            "id=" + getId() +
            ", celebName='" + getCelebName() + "'" +
            ", appusers=" + getAppusers() +
            "}";
    }
}
