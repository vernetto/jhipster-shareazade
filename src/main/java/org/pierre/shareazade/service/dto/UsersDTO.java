package org.pierre.shareazade.service.dto;

import java.io.Serializable;
import java.util.Objects;
import org.pierre.shareazade.domain.enumeration.UserRole;
import org.pierre.shareazade.domain.enumeration.UserStatus;

/**
 * A DTO for the {@link org.pierre.shareazade.domain.Users} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsersDTO implements Serializable {

    private Long id;

    private String userName;

    private String userEmail;

    private UserRole userRole;

    private String userPhone;

    private UserStatus userStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsersDTO)) {
            return false;
        }

        UsersDTO usersDTO = (UsersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsersDTO{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", userEmail='" + getUserEmail() + "'" +
            ", userRole='" + getUserRole() + "'" +
            ", userPhone='" + getUserPhone() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            "}";
    }
}
