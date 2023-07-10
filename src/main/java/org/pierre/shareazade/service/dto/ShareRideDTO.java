package org.pierre.shareazade.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import org.pierre.shareazade.domain.enumeration.RideType;

/**
 * A DTO for the {@link org.pierre.shareazade.domain.ShareRide} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareRideDTO implements Serializable {

    private Long id;

    private ZonedDateTime rideDateTime;

    private RideType rideType;

    @Lob
    private String rideComments;

    private ShareCityDTO rideCityFrom;

    private ShareCityDTO rideCityTo;

    private UserDTO user;

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

    public ShareCityDTO getRideCityFrom() {
        return rideCityFrom;
    }

    public void setRideCityFrom(ShareCityDTO rideCityFrom) {
        this.rideCityFrom = rideCityFrom;
    }

    public ShareCityDTO getRideCityTo() {
        return rideCityTo;
    }

    public void setRideCityTo(ShareCityDTO rideCityTo) {
        this.rideCityTo = rideCityTo;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareRideDTO)) {
            return false;
        }

        ShareRideDTO shareRideDTO = (ShareRideDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shareRideDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareRideDTO{" +
            "id=" + getId() +
            ", rideDateTime='" + getRideDateTime() + "'" +
            ", rideType='" + getRideType() + "'" +
            ", rideComments='" + getRideComments() + "'" +
            ", rideCityFrom=" + getRideCityFrom() +
            ", rideCityTo=" + getRideCityTo() +
            ", user=" + getUser() +
            "}";
    }
}
