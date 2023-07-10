package org.pierre.shareazade.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.pierre.shareazade.domain.enumeration.RideType;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.pierre.shareazade.domain.ShareRide} entity. This class is used
 * in {@link org.pierre.shareazade.web.rest.ShareRideResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /share-rides?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareRideCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RideType
     */
    public static class RideTypeFilter extends Filter<RideType> {

        public RideTypeFilter() {}

        public RideTypeFilter(RideTypeFilter filter) {
            super(filter);
        }

        @Override
        public RideTypeFilter copy() {
            return new RideTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter rideDateTime;

    private RideTypeFilter rideType;

    private LongFilter rideCityFromId;

    private LongFilter rideCityToId;

    private LongFilter userId;

    private Boolean distinct;

    public ShareRideCriteria() {}

    public ShareRideCriteria(ShareRideCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rideDateTime = other.rideDateTime == null ? null : other.rideDateTime.copy();
        this.rideType = other.rideType == null ? null : other.rideType.copy();
        this.rideCityFromId = other.rideCityFromId == null ? null : other.rideCityFromId.copy();
        this.rideCityToId = other.rideCityToId == null ? null : other.rideCityToId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShareRideCriteria copy() {
        return new ShareRideCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getRideDateTime() {
        return rideDateTime;
    }

    public ZonedDateTimeFilter rideDateTime() {
        if (rideDateTime == null) {
            rideDateTime = new ZonedDateTimeFilter();
        }
        return rideDateTime;
    }

    public void setRideDateTime(ZonedDateTimeFilter rideDateTime) {
        this.rideDateTime = rideDateTime;
    }

    public RideTypeFilter getRideType() {
        return rideType;
    }

    public RideTypeFilter rideType() {
        if (rideType == null) {
            rideType = new RideTypeFilter();
        }
        return rideType;
    }

    public void setRideType(RideTypeFilter rideType) {
        this.rideType = rideType;
    }

    public LongFilter getRideCityFromId() {
        return rideCityFromId;
    }

    public LongFilter rideCityFromId() {
        if (rideCityFromId == null) {
            rideCityFromId = new LongFilter();
        }
        return rideCityFromId;
    }

    public void setRideCityFromId(LongFilter rideCityFromId) {
        this.rideCityFromId = rideCityFromId;
    }

    public LongFilter getRideCityToId() {
        return rideCityToId;
    }

    public LongFilter rideCityToId() {
        if (rideCityToId == null) {
            rideCityToId = new LongFilter();
        }
        return rideCityToId;
    }

    public void setRideCityToId(LongFilter rideCityToId) {
        this.rideCityToId = rideCityToId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShareRideCriteria that = (ShareRideCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rideDateTime, that.rideDateTime) &&
            Objects.equals(rideType, that.rideType) &&
            Objects.equals(rideCityFromId, that.rideCityFromId) &&
            Objects.equals(rideCityToId, that.rideCityToId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rideDateTime, rideType, rideCityFromId, rideCityToId, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareRideCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rideDateTime != null ? "rideDateTime=" + rideDateTime + ", " : "") +
            (rideType != null ? "rideType=" + rideType + ", " : "") +
            (rideCityFromId != null ? "rideCityFromId=" + rideCityFromId + ", " : "") +
            (rideCityToId != null ? "rideCityToId=" + rideCityToId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
