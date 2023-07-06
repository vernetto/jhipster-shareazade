package org.pierre.shareazade.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.pierre.shareazade.domain.enumeration.UserRole;
import org.pierre.shareazade.domain.enumeration.UserStatus;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.pierre.shareazade.domain.ShareUser} entity. This class is used
 * in {@link org.pierre.shareazade.web.rest.ShareUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /share-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShareUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering UserRole
     */
    public static class UserRoleFilter extends Filter<UserRole> {

        public UserRoleFilter() {}

        public UserRoleFilter(UserRoleFilter filter) {
            super(filter);
        }

        @Override
        public UserRoleFilter copy() {
            return new UserRoleFilter(this);
        }
    }

    /**
     * Class for filtering UserStatus
     */
    public static class UserStatusFilter extends Filter<UserStatus> {

        public UserStatusFilter() {}

        public UserStatusFilter(UserStatusFilter filter) {
            super(filter);
        }

        @Override
        public UserStatusFilter copy() {
            return new UserStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userName;

    private StringFilter userEmail;

    private UserRoleFilter userRole;

    private StringFilter userPhone;

    private UserStatusFilter userStatus;

    private Boolean distinct;

    public ShareUserCriteria() {}

    public ShareUserCriteria(ShareUserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.userEmail = other.userEmail == null ? null : other.userEmail.copy();
        this.userRole = other.userRole == null ? null : other.userRole.copy();
        this.userPhone = other.userPhone == null ? null : other.userPhone.copy();
        this.userStatus = other.userStatus == null ? null : other.userStatus.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShareUserCriteria copy() {
        return new ShareUserCriteria(this);
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

    public StringFilter getUserName() {
        return userName;
    }

    public StringFilter userName() {
        if (userName == null) {
            userName = new StringFilter();
        }
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public StringFilter getUserEmail() {
        return userEmail;
    }

    public StringFilter userEmail() {
        if (userEmail == null) {
            userEmail = new StringFilter();
        }
        return userEmail;
    }

    public void setUserEmail(StringFilter userEmail) {
        this.userEmail = userEmail;
    }

    public UserRoleFilter getUserRole() {
        return userRole;
    }

    public UserRoleFilter userRole() {
        if (userRole == null) {
            userRole = new UserRoleFilter();
        }
        return userRole;
    }

    public void setUserRole(UserRoleFilter userRole) {
        this.userRole = userRole;
    }

    public StringFilter getUserPhone() {
        return userPhone;
    }

    public StringFilter userPhone() {
        if (userPhone == null) {
            userPhone = new StringFilter();
        }
        return userPhone;
    }

    public void setUserPhone(StringFilter userPhone) {
        this.userPhone = userPhone;
    }

    public UserStatusFilter getUserStatus() {
        return userStatus;
    }

    public UserStatusFilter userStatus() {
        if (userStatus == null) {
            userStatus = new UserStatusFilter();
        }
        return userStatus;
    }

    public void setUserStatus(UserStatusFilter userStatus) {
        this.userStatus = userStatus;
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
        final ShareUserCriteria that = (ShareUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(userEmail, that.userEmail) &&
            Objects.equals(userRole, that.userRole) &&
            Objects.equals(userPhone, that.userPhone) &&
            Objects.equals(userStatus, that.userStatus) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, userEmail, userRole, userPhone, userStatus, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShareUserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userName != null ? "userName=" + userName + ", " : "") +
            (userEmail != null ? "userEmail=" + userEmail + ", " : "") +
            (userRole != null ? "userRole=" + userRole + ", " : "") +
            (userPhone != null ? "userPhone=" + userPhone + ", " : "") +
            (userStatus != null ? "userStatus=" + userStatus + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
