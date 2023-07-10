import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IShareCity } from 'app/shared/model/share-city.model';
import { ShareCountry } from 'app/shared/model/enumerations/share-country.model';
import { getEntity, updateEntity, createEntity, reset } from './share-city.reducer';

export const ShareCityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const shareCityEntity = useAppSelector(state => state.shareCity.entity);
  const loading = useAppSelector(state => state.shareCity.loading);
  const updating = useAppSelector(state => state.shareCity.updating);
  const updateSuccess = useAppSelector(state => state.shareCity.updateSuccess);
  const shareCountryValues = Object.keys(ShareCountry);

  const handleClose = () => {
    navigate('/share-city' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...shareCityEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          cityCountry: 'CH',
          ...shareCityEntity,
          user: shareCityEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shareazadeApp.shareCity.home.createOrEditLabel" data-cy="ShareCityCreateUpdateHeading">
            <Translate contentKey="shareazadeApp.shareCity.home.createOrEditLabel">Create or edit a ShareCity</Translate>
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
                  id="share-city-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('shareazadeApp.shareCity.cityName')}
                id="share-city-cityName"
                name="cityName"
                data-cy="cityName"
                type="text"
              />
              <ValidatedField
                label={translate('shareazadeApp.shareCity.cityCountry')}
                id="share-city-cityCountry"
                name="cityCountry"
                data-cy="cityCountry"
                type="select"
              >
                {shareCountryValues.map(shareCountry => (
                  <option value={shareCountry} key={shareCountry}>
                    {translate('shareazadeApp.ShareCountry.' + shareCountry)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="share-city-user"
                name="user"
                data-cy="user"
                label={translate('shareazadeApp.shareCity.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/share-city" replace color="info">
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

export default ShareCityUpdate;
