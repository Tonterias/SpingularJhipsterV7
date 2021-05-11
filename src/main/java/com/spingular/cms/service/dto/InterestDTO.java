package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Interest} entity.
 */
public class InterestDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    private String interestName;

    private Set<AppuserDTO> appusers = new HashSet<>();

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
        if (!(o instanceof InterestDTO)) {
            return false;
        }

        InterestDTO interestDTO = (InterestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterestDTO{" +
            "id=" + getId() +
            ", interestName='" + getInterestName() + "'" +
            ", appusers=" + getAppusers() +
            "}";
    }
}
