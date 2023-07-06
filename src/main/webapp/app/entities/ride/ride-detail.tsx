import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ride.reducer';

export const RideDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const rideEntity = useAppSelector(state => state.ride.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="rideDetailsHeading">
          <Translate contentKey="shareazadeApp.ride.detail.title">Ride</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{rideEntity.id}</dd>
          <dt>
            <span id="rideDateTime">
              <Translate contentKey="shareazadeApp.ride.rideDateTime">Ride Date Time</Translate>
            </span>
          </dt>
          <dd>{rideEntity.rideDateTime ? <TextFormat value={rideEntity.rideDateTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="rideCityFrom">
              <Translate contentKey="shareazadeApp.ride.rideCityFrom">Ride City From</Translate>
            </span>
          </dt>
          <dd>{rideEntity.rideCityFrom}</dd>
          <dt>
            <span id="rideCityTo">
              <Translate contentKey="shareazadeApp.ride.rideCityTo">Ride City To</Translate>
            </span>
          </dt>
          <dd>{rideEntity.rideCityTo}</dd>
          <dt>
            <span id="rideType">
              <Translate contentKey="shareazadeApp.ride.rideType">Ride Type</Translate>
            </span>
          </dt>
          <dd>{rideEntity.rideType}</dd>
          <dt>
            <span id="rideComments">
              <Translate contentKey="shareazadeApp.ride.rideComments">Ride Comments</Translate>
            </span>
          </dt>
          <dd>{rideEntity.rideComments}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.ride.rideUser">Ride User</Translate>
          </dt>
          <dd>{rideEntity.rideUser ? rideEntity.rideUser.userName : ''}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.ride.rideCityFrom">Ride City From</Translate>
          </dt>
          <dd>{rideEntity.rideCityFrom ? rideEntity.rideCityFrom.cityName : ''}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.ride.rideCityTo">Ride City To</Translate>
          </dt>
          <dd>{rideEntity.rideCityTo ? rideEntity.rideCityTo.cityName : ''}</dd>
        </dl>
        <Button tag={Link} to="/ride" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ride/${rideEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RideDetail;
