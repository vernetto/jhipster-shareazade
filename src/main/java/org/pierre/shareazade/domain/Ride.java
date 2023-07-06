package org.pierre.shareazade.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.pierre.shareazade.domain.enumeration.RideType;

/**
 * A Ride.
 */
@Entity
@Table(name = "ride")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ride_date_time")
    private ZonedDateTime rideDateTime;

    @Column(name = "ride_city_from")
    private String rideCityFrom;

    @Column(name = "ride_city_to")
    private String rideCityTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_type")
    private RideType rideType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ride_comments")
    private String rideComments;

    @OneToOne
    @JoinColumn(unique = true)
    private Users rideUser;

    @ManyToOne
    private City rideCityFrom;

    @ManyToOne
    private City rideCityTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ride id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRideDateTime() {
        return this.rideDateTime;
    }

    public Ride rideDateTime(ZonedDateTime rideDateTime) {
        this.setRideDateTime(rideDateTime);
        return this;
    }

    public void setRideDateTime(ZonedDateTime rideDateTime) {
        this.rideDateTime = rideDateTime;
    }

    public String getRideCityFrom() {
        return this.rideCityFrom;
    }

    public Ride rideCityFrom(String rideCityFrom) {
        this.setRideCityFrom(rideCityFrom);
        return this;
    }

    public void setRideCityFrom(String rideCityFrom) {
        this.rideCityFrom = rideCityFrom;
    }

    public String getRideCityTo() {
        return this.rideCityTo;
    }

    public Ride rideCityTo(String rideCityTo) {
        this.setRideCityTo(rideCityTo);
        return this;
    }

    public void setRideCityTo(String rideCityTo) {
        this.rideCityTo = rideCityTo;
    }

    public RideType getRideType() {
        return this.rideType;
    }

    public Ride rideType(RideType rideType) {
        this.setRideType(rideType);
        return this;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public String getRideComments() {
        return this.rideComments;
    }

    public Ride rideComments(String rideComments) {
        this.setRideComments(rideComments);
        return this;
    }

    public void setRideComments(String rideComments) {
        this.rideComments = rideComments;
    }

    public Users getRideUser() {
        return this.rideUser;
    }

    public void setRideUser(Users users) {
        this.rideUser = users;
    }

    public Ride rideUser(Users users) {
        this.setRideUser(users);
        return this;
    }

    public City getRideCityFrom() {
        return this.rideCityFrom;
    }

    public void setRideCityFrom(City city) {
        this.rideCityFrom = city;
    }

    public Ride rideCityFrom(City city) {
        this.setRideCityFrom(city);
        return this;
    }

    public City getRideCityTo() {
        return this.rideCityTo;
    }

    public void setRideCityTo(City city) {
        this.rideCityTo = city;
    }

    public Ride rideCityTo(City city) {
        this.setRideCityTo(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ride)) {
            return false;
        }
        return id != null && id.equals(((Ride) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ride{" +
            "id=" + getId() +
            ", rideDateTime='" + getRideDateTime() + "'" +
            ", rideCityFrom='" + getRideCityFrom() + "'" +
            ", rideCityTo='" + getRideCityTo() + "'" +
            ", rideType='" + getRideType() + "'" +
            ", rideComments='" + getRideComments() + "'" +
            "}";
    }
}
