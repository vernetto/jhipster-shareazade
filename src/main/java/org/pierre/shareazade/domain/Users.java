package org.pierre.shareazade.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.pierre.shareazade.domain.enumeration.UserRole;
import org.pierre.shareazade.domain.enumeration.UserStatus;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "user_phone")
    private String userPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public Users userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public Users userEmail(String userEmail) {
        this.setUserEmail(userEmail);
        return this;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserRole getUserRole() {
        return this.userRole;
    }

    public Users userRole(UserRole userRole) {
        this.setUserRole(userRole);
        return this;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUserPhone() {
        return this.userPhone;
    }

    public Users userPhone(String userPhone) {
        this.setUserPhone(userPhone);
        return this;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public UserStatus getUserStatus() {
        return this.userStatus;
    }

    public Users userStatus(UserStatus userStatus) {
        this.setUserStatus(userStatus);
        return this;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return id != null && id.equals(((Users) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", userRole='" + getUserRole() + "'" +
            ", userPhone='" + getUserPhone() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            "}";
    }
}
