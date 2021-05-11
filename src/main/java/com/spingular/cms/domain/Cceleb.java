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
 * A Cceleb.
 */
@Entity
@Table(name = "cceleb")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cceleb implements Serializable {

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
        name = "rel_cceleb__community",
        joinColumns = @JoinColumn(name = "cceleb_id"),
        inverseJoinColumns = @JoinColumn(name = "community_id")
    )
    @JsonIgnoreProperties(
        value = {
            "blogs", "cfolloweds", "cfollowings", "cblockedusers", "cblockingusers", "appuser", "cinterests", "cactivities", "ccelebs",
        },
        allowSetters = true
    )
    private Set<Community> communities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cceleb id(Long id) {
        this.id = id;
        return this;
    }

    public String getCelebName() {
        return this.celebName;
    }

    public Cceleb celebName(String celebName) {
        this.celebName = celebName;
        return this;
    }

    public void setCelebName(String celebName) {
        this.celebName = celebName;
    }

    public Set<Community> getCommunities() {
        return this.communities;
    }

    public Cceleb communities(Set<Community> communities) {
        this.setCommunities(communities);
        return this;
    }

    public Cceleb addCommunity(Community community) {
        this.communities.add(community);
        community.getCcelebs().add(this);
        return this;
    }

    public Cceleb removeCommunity(Community community) {
        this.communities.remove(community);
        community.getCcelebs().remove(this);
        return this;
    }

    public void setCommunities(Set<Community> communities) {
        this.communities = communities;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cceleb)) {
            return false;
        }
        return id != null && id.equals(((Cceleb) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cceleb{" +
            "id=" + getId() +
            ", celebName='" + getCelebName() + "'" +
            "}";
    }
}
