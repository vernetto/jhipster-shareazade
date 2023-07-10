import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './share-city.reducer';

export const ShareCityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const shareCityEntity = useAppSelector(state => state.shareCity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shareCityDetailsHeading">
          <Translate contentKey="shareazadeApp.shareCity.detail.title">ShareCity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shareCityEntity.id}</dd>
          <dt>
            <span id="cityName">
              <Translate contentKey="shareazadeApp.shareCity.cityName">City Name</Translate>
            </span>
          </dt>
          <dd>{shareCityEntity.cityName}</dd>
          <dt>
            <span id="cityCountry">
              <Translate contentKey="shareazadeApp.shareCity.cityCountry">City Country</Translate>
            </span>
          </dt>
          <dd>{shareCityEntity.cityCountry}</dd>
          <dt>
            <Translate contentKey="shareazadeApp.shareCity.user">User</Translate>
          </dt>
          <dd>{shareCityEntity.user ? shareCityEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/share-city" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/share-city/${shareCityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShareCityDetail;
