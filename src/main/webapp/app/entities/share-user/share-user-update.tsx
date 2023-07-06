import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShareUser } from 'app/shared/model/share-user.model';
import { UserRole } from 'app/shared/model/enumerations/user-role.model';
import { UserStatus } from 'app/shared/model/enumerations/user-status.model';
import { getEntity, updateEntity, createEntity, reset } from './share-user.reducer';

export const ShareUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const shareUserEntity = useAppSelector(state => state.shareUser.entity);
  const loading = useAppSelector(state => state.shareUser.loading);
  const updating = useAppSelector(state => state.shareUser.updating);
  const updateSuccess = useAppSelector(state => state.shareUser.updateSuccess);
  const userRoleValues = Object.keys(UserRole);
  const userStatusValues = Object.keys(UserStatus);

  const handleClose = () => {
    navigate('/share-user' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...shareUserEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          userRole: 'ADMIN',
          userStatus: 'ACTIVE',
          ...shareUserEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shareazadeApp.shareUser.home.createOrEditLabel" data-cy="ShareUserCreateUpdateHeading">
            <Translate contentKey="shareazadeApp.shareUser.home.createOrEditLabel">Create or edit a ShareUser</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="share-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('shareazadeApp.shareUser.userName')}
                id="share-user-userName"
                name="userName"
                data-cy="userName"
                type="text"
              />
              <ValidatedField
                label={translate('shareazadeApp.shareUser.userEmail')}
                id="share-user-userEmail"
                name="userEmail"
                data-cy="userEmail"
                type="text"
              />
              <ValidatedField
                label={translate('shareazadeApp.shareUser.userRole')}
                id="share-user-userRole"
                name="userRole"
                data-cy="userRole"
                type="select"
              >
                {userRoleValues.map(userRole => (
                  <option value={userRole} key={userRole}>
                    {translate('shareazadeApp.UserRole.' + userRole)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('shareazadeApp.shareUser.userPhone')}
                id="share-user-userPhone"
                name="userPhone"
                data-cy="userPhone"
                type="text"
              />
              <ValidatedField
                label={translate('shareazadeApp.shareUser.userStatus')}
                id="share-user-userStatus"
                name="userStatus"
                data-cy="userStatus"
                type="select"
              >
                {userStatusValues.map(userStatus => (
                  <option value={userStatus} key={userStatus}>
                    {translate('shareazadeApp.UserStatus.' + userStatus)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/share-user" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ShareUserUpdate;
