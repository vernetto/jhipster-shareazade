package org.pierre.shareazade.service.dto;

import java.io.Serializable;
import java.util.Objects;
import org.pierre.shareazade.domain.enumeration.ShareCountry;

/**
 * A DTO for the {@link org.pierre.shareazade.domain.ShareCity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareCityDTO implements Serializable {

    private Long id;

    private String cityName;

    private ShareCountry cityCountry;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ShareCountry getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(ShareCountry cityCountry) {
        this.cityCountry = cityCountry;
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
        if (!(o instanceof ShareCityDTO)) {
            return false;
        }

        ShareCityDTO shareCityDTO = (ShareCityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shareCityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareCityDTO{" +
            "id=" + getId() +
            ", cityName='" + getCityName() + "'" +
            ", cityCountry='" + getCityCountry() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
