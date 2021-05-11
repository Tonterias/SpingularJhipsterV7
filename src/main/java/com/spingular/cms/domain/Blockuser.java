package com.spingular.cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Blockuser.
 */
@Entity
@Table(name = "blockuser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Blockuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "creation_date")
    private Instant creationDate;

    @ManyToOne
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
    private Appuser blockeduser;

    @ManyToOne
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
    private Appuser blockinguser;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "blogs", "cfolloweds", "cfollowings", "cblockedusers", "cblockingusers", "appuser", "cinterests", "cactivities", "ccelebs",
        },
        allowSetters = true
    )
    private Community cblockeduser;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "blogs", "cfolloweds", "cfollowings", "cblockedusers", "cblockingusers", "appuser", "cinterests", "cactivities", "ccelebs",
        },
        allowSetters = true
    )
    private Community cblockinguser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blockuser id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Blockuser creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Appuser getBlockeduser() {
        return this.blockeduser;
    }

    public Blockuser blockeduser(Appuser appuser) {
        this.setBlockeduser(appuser);
        return this;
    }

    public void setBlockeduser(Appuser appuser) {
        this.blockeduser = appuser;
    }

    public Appuser getBlockinguser() {
        return this.blockinguser;
    }

    public Blockuser blockinguser(Appuser appuser) {
        this.setBlockinguser(appuser);
        return this;
    }

    public void setBlockinguser(Appuser appuser) {
        this.blockinguser = appuser;
    }

    public Community getCblockeduser() {
        return this.cblockeduser;
    }

    public Blockuser cblockeduser(Community community) {
        this.setCblockeduser(community);
        return this;
    }

    public void setCblockeduser(Community community) {
        this.cblockeduser = community;
    }

    public Community getCblockinguser() {
        return this.cblockinguser;
    }

    public Blockuser cblockinguser(Community community) {
        this.setCblockinguser(community);
        return this;
    }

    public void setCblockinguser(Community community) {
        this.cblockinguser = community;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blockuser)) {
            return false;
        }
        return id != null && id.equals(((Blockuser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Blockuser{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
