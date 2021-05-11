package com.spingular.cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Celeb.
 */
@Entity
@Table(name = "celeb")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Celeb implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "celeb_name", length = 40, nullable = false)
    private String celebName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_celeb__appuser",
        joinColumns = @JoinColumn(name = "celeb_id"),
        inverseJoinColumns = @JoinColumn(name = "appuser_id")
    )
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
    private Set<Appuser> appusers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Celeb id(Long id) {
        this.id = id;
        return this;
    }

    public String getCelebName() {
        return this.celebName;
    }

    public Celeb celebName(String celebName) {
        this.celebName = celebName;
        return this;
    }

    public void setCelebName(String celebName) {
        this.celebName = celebName;
    }

    public Set<Appuser> getAppusers() {
        return this.appusers;
    }

    public Celeb appusers(Set<Appuser> appusers) {
        this.setAppusers(appusers);
        return this;
    }

    public Celeb addAppuser(Appuser appuser) {
        this.appusers.add(appuser);
        appuser.getCelebs().add(this);
        return this;
    }

    public Celeb removeAppuser(Appuser appuser) {
        this.appusers.remove(appuser);
        appuser.getCelebs().remove(this);
        return this;
    }

    public void setAppusers(Set<Appuser> appusers) {
        this.appusers = appusers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Celeb)) {
            return false;
        }
        return id != null && id.equals(((Celeb) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Celeb{" +
            "id=" + getId() +
            ", celebName='" + getCelebName() + "'" +
            "}";
    }
}
