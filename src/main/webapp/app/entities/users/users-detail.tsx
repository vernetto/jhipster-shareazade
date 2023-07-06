import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './users.reducer';

export const UsersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const usersEntity = useAppSelector(state => state.users.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usersDetailsHeading">
          <Translate contentKey="shareazadeApp.users.detail.title">Users</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{usersEntity.id}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="shareazadeApp.users.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{usersEntity.userName}</dd>
          <dt>
            <span id="userEmail">
              <Translate contentKey="shareazadeApp.users.userEmail">User Email</Translate>
            </span>
          </dt>
          <dd>{usersEntity.userEmail}</dd>
          <dt>
            <span id="userRole">
              <Translate contentKey="shareazadeApp.users.userRole">User Role</Translate>
            </span>
          </dt>
          <dd>{usersEntity.userRole}</dd>
          <dt>
            <span id="userPhone">
              <Translate contentKey="shareazadeApp.users.userPhone">User Phone</Translate>
            </span>
          </dt>
          <dd>{usersEntity.userPhone}</dd>
          <dt>
            <span id="userStatus">
              <Translate contentKey="shareazadeApp.users.userStatus">User Status</Translate>
            </span>
          </dt>
          <dd>{usersEntity.userStatus}</dd>
        </dl>
        <Button tag={Link} to="/users" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/users/${usersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsersDetail;
