package org.pierre.shareazade.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.pierre.shareazade.domain.enumeration.RideType;

/**
 * A ShareRide.
 */
@Entity
@Table(name = "share_ride")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareRide implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ride_date_time")
    private ZonedDateTime rideDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_type")
    private RideType rideType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ride_comments")
    private String rideComments;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ShareCity rideCityFrom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private ShareCity rideCityTo;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShareRide id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRideDateTime() {
        return this.rideDateTime;
    }

    public ShareRide rideDateTime(ZonedDateTime rideDateTime) {
        this.setRideDateTime(rideDateTime);
        return this;
    }

    public void setRideDateTime(ZonedDateTime rideDateTime) {
        this.rideDateTime = rideDateTime;
    }

    public RideType getRideType() {
        return this.rideType;
    }

    public ShareRide rideType(RideType rideType) {
        this.setRideType(rideType);
        return this;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public String getRideComments() {
        return this.rideComments;
    }

    public ShareRide rideComments(String rideComments) {
        this.setRideComments(rideComments);
        return this;
    }

    public void setRideComments(String rideComments) {
        this.rideComments = rideComments;
    }

    public ShareCity getRideCityFrom() {
        return this.rideCityFrom;
    }

    public void setRideCityFrom(ShareCity shareCity) {
        this.rideCityFrom = shareCity;
    }

    public ShareRide rideCityFrom(ShareCity shareCity) {
        this.setRideCityFrom(shareCity);
        return this;
    }

    public ShareCity getRideCityTo() {
        return this.rideCityTo;
    }

    public void setRideCityTo(ShareCity shareCity) {
        this.rideCityTo = shareCity;
    }

    public ShareRide rideCityTo(ShareCity shareCity) {
        this.setRideCityTo(shareCity);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShareRide user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareRide)) {
            return false;
        }
        return id != null && id.equals(((ShareRide) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareRide{" +
            "id=" + getId() +
            ", rideDateTime='" + getRideDateTime() + "'" +
            ", rideType='" + getRideType() + "'" +
            ", rideComments='" + getRideComments() + "'" +
            "}";
    }
}
