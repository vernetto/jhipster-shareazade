import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUsers } from 'app/shared/model/users.model';
import { getEntities as getUsers } from 'app/entities/users/users.reducer';
import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IRide } from 'app/shared/model/ride.model';
import { RideType } from 'app/shared/model/enumerations/ride-type.model';
import { getEntity, updateEntity, createEntity, reset } from './ride.reducer';

export const RideUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.users.entities);
  const cities = useAppSelector(state => state.city.entities);
  const rideEntity = useAppSelector(state => state.ride.entity);
  const loading = useAppSelector(state => state.ride.loading);
  const updating = useAppSelector(state => state.ride.updating);
  const updateSuccess = useAppSelector(state => state.ride.updateSuccess);
  const rideTypeValues = Object.keys(RideType);

  const handleClose = () => {
    navigate('/ride' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.rideDateTime = convertDateTimeToServer(values.rideDateTime);

    const entity = {
      ...rideEntity,
      ...values,
      rideUser: users.find(it => it.id.toString() === values.rideUser.toString()),
      rideCityFrom: cities.find(it => it.id.toString() === values.rideCityFrom.toString()),
      rideCityTo: cities.find(it => it.id.toString() === values.rideCityTo.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          rideDateTime: displayDefaultDateTime(),
        }
      : {
          rideType: 'OFFER',
          ...rideEntity,
          rideDateTime: convertDateTimeFromServer(rideEntity.rideDateTime),
          rideUser: rideEntity?.rideUser?.id,
          rideCityFrom: rideEntity?.rideCityFrom?.id,
          rideCityTo: rideEntity?.rideCityTo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shareazadeApp.ride.home.createOrEditLabel" data-cy="RideCreateUpdateHeading">
            <Translate contentKey="shareazadeApp.ride.home.createOrEditLabel">Create or edit a Ride</Translate>
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
                  id="ride-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('shareazadeApp.ride.rideDateTime')}
                id="ride-rideDateTime"
                name="rideDateTime"
                data-cy="rideDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('shareazadeApp.ride.rideCityFrom')}
                id="ride-rideCityFrom"
                name="rideCityFrom"
                data-cy="rideCityFrom"
                type="text"
              />
              <ValidatedField
                label={translate('shareazadeApp.ride.rideCityTo')}
                id="ride-rideCityTo"
                name="rideCityTo"
                data-cy="rideCityTo"
                type="text"
              />
              <ValidatedField
                label={translate('shareazadeApp.ride.rideType')}
                id="ride-rideType"
                name="rideType"
                data-cy="rideType"
                type="select"
              >
                {rideTypeValues.map(rideType => (
                  <option value={rideType} key={rideType}>
                    {translate('shareazadeApp.RideType.' + rideType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('shareazadeApp.ride.rideComments')}
                id="ride-rideComments"
                name="rideComments"
                data-cy="rideComments"
                type="textarea"
              />
              <ValidatedField
                id="ride-rideUser"
                name="rideUser"
                data-cy="rideUser"
                label={translate('shareazadeApp.ride.rideUser')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.userName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ride-rideCityFrom"
                name="rideCityFrom"
                data-cy="rideCityFrom"
                label={translate('shareazadeApp.ride.rideCityFrom')}
                type="select"
              >
                <option value="" key="0" />
                {cities
                  ? cities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cityName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="ride-rideCityTo"
                name="rideCityTo"
                data-cy="rideCityTo"
                label={translate('shareazadeApp.ride.rideCityTo')}
                type="select"
              >
                <option value="" key="0" />
                {cities
                  ? cities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cityName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ride" replace color="info">
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

export default RideUpdate;
