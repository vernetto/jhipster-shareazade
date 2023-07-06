package org.pierre.shareazade.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import org.pierre.shareazade.domain.enumeration.RideType;

/**
 * A DTO for the {@link org.pierre.shareazade.domain.Ride} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RideDTO implements Serializable {

    private Long id;

    private ZonedDateTime rideDateTime;

    private String rideCityFrom;

    private String rideCityTo;

    private RideType rideType;

    @Lob
    private String rideComments;

    private UsersDTO rideUser;

    private CityDTO rideCityFrom;

    private CityDTO rideCityTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRideDateTime() {
        return rideDateTime;
    }

    public void setRideDateTime(ZonedDateTime rideDateTime) {
        this.rideDateTime = rideDateTime;
    }

    public String getRideCityFrom() {
        return rideCityFrom;
    }

    public void setRideCityFrom(String rideCityFrom) {
        this.rideCityFrom = rideCityFrom;
    }

    public String getRideCityTo() {
        return rideCityTo;
    }

    public void setRideCityTo(String rideCityTo) {
        this.rideCityTo = rideCityTo;
    }

    public RideType getRideType() {
        return rideType;
    }

    public void setRideType(RideType rideType) {
        this.rideType = rideType;
    }

    public String getRideComments() {
        return rideComments;
    }

    public void setRideComments(String rideComments) {
        this.rideComments = rideComments;
    }

    public UsersDTO getRideUser() {
        return rideUser;
    }

    public void setRideUser(UsersDTO rideUser) {
        this.rideUser = rideUser;
    }

    public CityDTO getRideCityFrom() {
        return rideCityFrom;
    }

    public void setRideCityFrom(CityDTO rideCityFrom) {
        this.rideCityFrom = rideCityFrom;
    }

    public CityDTO getRideCityTo() {
        return rideCityTo;
    }

    public void setRideCityTo(CityDTO rideCityTo) {
        this.rideCityTo = rideCityTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RideDTO)) {
            return false;
        }

        RideDTO rideDTO = (RideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RideDTO{" +
            "id=" + getId() +
            ", rideDateTime='" + getRideDateTime() + "'" +
            ", rideCityFrom='" + getRideCityFrom() + "'" +
            ", rideCityTo='" + getRideCityTo() + "'" +
            ", rideType='" + getRideType() + "'" +
            ", rideComments='" + getRideComments() + "'" +
            ", rideUser=" + getRideUser() +
            ", rideCityFrom=" + getRideCityFrom() +
            ", rideCityTo=" + getRideCityTo() +
            "}";
    }
}
