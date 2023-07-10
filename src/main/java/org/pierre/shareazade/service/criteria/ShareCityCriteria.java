package org.pierre.shareazade.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.pierre.shareazade.domain.enumeration.ShareCountry;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.pierre.shareazade.domain.ShareCity} entity. This class is used
 * in {@link org.pierre.shareazade.web.rest.ShareCityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /share-cities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareCityCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ShareCountry
     */
    public static class ShareCountryFilter extends Filter<ShareCountry> {

        public ShareCountryFilter() {}

        public ShareCountryFilter(ShareCountryFilter filter) {
            super(filter);
        }

        @Override
        public ShareCountryFilter copy() {
            return new ShareCountryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter cityName;

    private ShareCountryFilter cityCountry;

    private LongFilter userId;

    private Boolean distinct;

    public ShareCityCriteria() {}

    public ShareCityCriteria(ShareCityCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.cityName = other.cityName == null ? null : other.cityName.copy();
        this.cityCountry = other.cityCountry == null ? null : other.cityCountry.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShareCityCriteria copy() {
        return new ShareCityCriteria(this);
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

    public StringFilter getCityName() {
        return cityName;
    }

    public StringFilter cityName() {
        if (cityName == null) {
            cityName = new StringFilter();
        }
        return cityName;
    }

    public void setCityName(StringFilter cityName) {
        this.cityName = cityName;
    }

    public ShareCountryFilter getCityCountry() {
        return cityCountry;
    }

    public ShareCountryFilter cityCountry() {
        if (cityCountry == null) {
            cityCountry = new ShareCountryFilter();
        }
        return cityCountry;
    }

    public void setCityCountry(ShareCountryFilter cityCountry) {
        this.cityCountry = cityCountry;
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
        final ShareCityCriteria that = (ShareCityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(cityName, that.cityName) &&
            Objects.equals(cityCountry, that.cityCountry) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityName, cityCountry, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareCityCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (cityName != null ? "cityName=" + cityName + ", " : "") +
            (cityCountry != null ? "cityCountry=" + cityCountry + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
