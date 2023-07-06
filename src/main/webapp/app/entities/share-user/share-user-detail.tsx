import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './share-user.reducer';

export const ShareUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const shareUserEntity = useAppSelector(state => state.shareUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="shareUserDetailsHeading">
          <Translate contentKey="shareazadeApp.shareUser.detail.title">ShareUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{shareUserEntity.id}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="shareazadeApp.shareUser.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{shareUserEntity.userName}</dd>
          <dt>
            <span id="userEmail">
              <Translate contentKey="shareazadeApp.shareUser.userEmail">User Email</Translate>
            </span>
          </dt>
          <dd>{shareUserEntity.userEmail}</dd>
          <dt>
            <span id="userRole">
              <Translate contentKey="shareazadeApp.shareUser.userRole">User Role</Translate>
            </span>
          </dt>
          <dd>{shareUserEntity.userRole}</dd>
          <dt>
            <span id="userPhone">
              <Translate contentKey="shareazadeApp.shareUser.userPhone">User Phone</Translate>
            </span>
          </dt>
          <dd>{shareUserEntity.userPhone}</dd>
          <dt>
            <span id="userStatus">
              <Translate contentKey="shareazadeApp.shareUser.userStatus">User Status</Translate>
            </span>
          </dt>
          <dd>{shareUserEntity.userStatus}</dd>
        </dl>
        <Button tag={Link} to="/share-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/share-user/${shareUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShareUserDetail;
