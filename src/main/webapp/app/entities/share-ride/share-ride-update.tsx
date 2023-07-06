import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IShareCity } from 'app/shared/model/share-city.model';
import { getEntities as getShareCities } from 'app/entities/share-city/share-city.reducer';
import { IShareUser } from 'app/shared/model/share-user.model';
import { getEntities as getShareUsers } from 'app/entities/share-user/share-user.reducer';
import { IShareRide } from 'app/shared/model/share-ride.model';
import { RideType } from 'app/shared/model/enumerations/ride-type.model';
import { getEntity, updateEntity, createEntity, reset } from './share-ride.reducer';

export const ShareRideUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const shareCities = useAppSelector(state => state.shareCity.entities);
  const shareUsers = useAppSelector(state => state.shareUser.entities);
  const shareRideEntity = useAppSelector(state => state.shareRide.entity);
  const loading = useAppSelector(state => state.shareRide.loading);
  const updating = useAppSelector(state => state.shareRide.updating);
  const updateSuccess = useAppSelector(state => state.shareRide.updateSuccess);
  const rideTypeValues = Object.keys(RideType);

  const handleClose = () => {
    navigate('/share-ride' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getShareCities({}));
    dispatch(getShareUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.rideDateTime = convertDateTimeToServer(values.rideDateTime);

    const entity = {
      ...shareRideEntity,
      ...values,
      rideCityFrom: shareCities.find(it => it.id.toString() === values.rideCityFrom.toString()),
      rideCityTo: shareCities.find(it => it.id.toString() === values.rideCityTo.toString()),
      rideUser: shareUsers.find(it => it.id.toString() === values.rideUser.toString()),
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
          ...shareRideEntity,
          rideDateTime: convertDateTimeFromServer(shareRideEntity.rideDateTime),
          rideCityFrom: shareRideEntity?.rideCityFrom?.id,
          rideCityTo: shareRideEntity?.rideCityTo?.id,
          rideUser: shareRideEntity?.rideUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shareazadeApp.shareRide.home.createOrEditLabel" data-cy="ShareRideCreateUpdateHeading">
            <Translate contentKey="shareazadeApp.shareRide.home.createOrEditLabel">Create or edit a ShareRide</Translate>
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
                  id="share-ride-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('shareazadeApp.shareRide.rideDateTime')}
                id="share-ride-rideDateTime"
                name="rideDateTime"
                data-cy="rideDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('shareazadeApp.shareRide.rideType')}
                id="share-ride-rideType"
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
                label={translate('shareazadeApp.shareRide.rideComments')}
                id="share-ride-rideComments"
                name="rideComments"
                data-cy="rideComments"
                type="textarea"
              />
              <ValidatedField
                id="share-ride-rideCityFrom"
                name="rideCityFrom"
                data-cy="rideCityFrom"
                label={translate('shareazadeApp.shareRide.rideCityFrom')}
                type="select"
              >
                <option value="" key="0" />
                {shareCities
                  ? shareCities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cityName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="share-ride-rideCityTo"
                name="rideCityTo"
                data-cy="rideCityTo"
                label={translate('shareazadeApp.shareRide.rideCityTo')}
                type="select"
              >
                <option value="" key="0" />
                {shareCities
                  ? shareCities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cityName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="share-ride-rideUser"
                name="rideUser"
                data-cy="rideUser"
                label={translate('shareazadeApp.shareRide.rideUser')}
                type="select"
              >
                <option value="" key="0" />
                {shareUsers
                  ? shareUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.userName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/share-ride" replace color="info">
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

export default ShareRideUpdate;
