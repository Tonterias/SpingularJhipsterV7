package com.spingular.cms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spingular.cms.domain.enumeration.NotificationReason;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "notification_date")
    private Instant notificationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_reason", nullable = false)
    private NotificationReason notificationReason;

    @Size(min = 2, max = 100)
    @Column(name = "notification_text", length = 100)
    private String notificationText;

    @Column(name = "is_delivered")
    private Boolean isDelivered;

    @ManyToOne(optional = false)
    @NotNull
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
    private Appuser appuser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Notification id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Notification creationDate(Instant creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getNotificationDate() {
        return this.notificationDate;
    }

    public Notification notificationDate(Instant notificationDate) {
        this.notificationDate = notificationDate;
        return this;
    }

    public void setNotificationDate(Instant notificationDate) {
        this.notificationDate = notificationDate;
    }

    public NotificationReason getNotificationReason() {
        return this.notificationReason;
    }

    public Notification notificationReason(NotificationReason notificationReason) {
        this.notificationReason = notificationReason;
        return this;
    }

    public void setNotificationReason(NotificationReason notificationReason) {
        this.notificationReason = notificationReason;
    }

    public String getNotificationText() {
        return this.notificationText;
    }

    public Notification notificationText(String notificationText) {
        this.notificationText = notificationText;
        return this;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public Boolean getIsDelivered() {
        return this.isDelivered;
    }

    public Notification isDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
        return this;
    }

    public void setIsDelivered(Boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public Appuser getAppuser() {
        return this.appuser;
    }

    public Notification appuser(Appuser appuser) {
        this.setAppuser(appuser);
        return this;
    }

    public void setAppuser(Appuser appuser) {
        this.appuser = appuser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", notificationDate='" + getNotificationDate() + "'" +
            ", notificationReason='" + getNotificationReason() + "'" +
            ", notificationText='" + getNotificationText() + "'" +
            ", isDelivered='" + getIsDelivered() + "'" +
            "}";
    }
}
