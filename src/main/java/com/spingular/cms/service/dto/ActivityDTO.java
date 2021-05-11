package com.spingular.cms.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.spingular.cms.domain.Activity} entity.
 */
public class ActivityDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    private String activityName;

    private Set<AppuserDTO> appusers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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
        if (!(o instanceof ActivityDTO)) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityDTO{" +
            "id=" + getId() +
            ", activityName='" + getActivityName() + "'" +
            ", appusers=" + getAppusers() +
            "}";
    }
}
