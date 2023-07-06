package org.pierre.shareazade.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pierre.shareazade.domain.enumeration.ShareCountry;

/**
 * A ShareCity.
 */
@Entity
@Table(name = "share_city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareCity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "city_name")
    private String cityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "city_country")
    private ShareCountry cityCountry;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShareCity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return this.cityName;
    }

    public ShareCity cityName(String cityName) {
        this.setCityName(cityName);
        return this;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public ShareCountry getCityCountry() {
        return this.cityCountry;
    }

    public ShareCity cityCountry(ShareCountry cityCountry) {
        this.setCityCountry(cityCountry);
        return this;
    }

    public void setCityCountry(ShareCountry cityCountry) {
        this.cityCountry = cityCountry;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShareCity)) {
            return false;
        }
        return id != null && id.equals(((ShareCity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareCity{" +
            "id=" + getId() +
            ", cityName='" + getCityName() + "'" +
            ", cityCountry='" + getCityCountry() + "'" +
            "}";
    }
}
