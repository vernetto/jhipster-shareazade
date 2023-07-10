import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './share-ride.reducer';

export const ShareRideDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const shareRideEntity = useAppSelector(state => state.shareRide.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shareRideDetailsHeading">
          <Translate contentKey="shareazadeApp.shareRide.detail.title">ShareRide</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shareRideEntity.id}</dd>
          <dt>
            <span id="rideDateTime">
              <Translate contentKey="shareazadeApp.shareRide.rideDateTime">Ride Date Time</Translate>
            </span>
          </dt>
          <dd>
            {shareRideEntity.rideDateTime ? <TextFormat value={shareRideEntity.rideDateTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="rideType">
              <Translate contentKey="shareazadeApp.shareRide.rideType">Ride Type</Translate>
            </span>
          </dt>
          <dd>{shareRideEntity.rideType}</dd>
          <dt>
            <span id="rideComments">
              <Translate contentKey="shareazadeApp.shareRide.rideComments">Ride Comments</Translate>
            </span>
          </dt>
          <dd>{shareRideEntity.rideComments}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.shareRide.rideCityFrom">Ride City From</Translate>
          </dt>
          <dd>{shareRideEntity.rideCityFrom ? shareRideEntity.rideCityFrom.cityName : ''}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.shareRide.rideCityTo">Ride City To</Translate>
          </dt>
          <dd>{shareRideEntity.rideCityTo ? shareRideEntity.rideCityTo.cityName : ''}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.shareRide.user">User</Translate>
          </dt>
          <dd>{shareRideEntity.user ? shareRideEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/share-ride" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/share-ride/${shareRideEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShareRideDetail;
