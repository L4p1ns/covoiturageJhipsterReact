import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './passager.reducer';
import { IPassager } from 'app/shared/model/passager.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPassagerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PassagerDetail extends React.Component<IPassagerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { passagerEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="covoijhipsterApp.passager.detail.title">Passager</Translate> [<b>{passagerEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="fumeur">
                <Translate contentKey="covoijhipsterApp.passager.fumeur">Fumeur</Translate>
              </span>
            </dt>
            <dd>{passagerEntity.fumeur ? 'true' : 'false'}</dd>
            <dt>
              <span id="acceptMusic">
                <Translate contentKey="covoijhipsterApp.passager.acceptMusic">Accept Music</Translate>
              </span>
            </dt>
            <dd>{passagerEntity.acceptMusic ? 'true' : 'false'}</dd>
            <dt>
              <span id="acceptRadio">
                <Translate contentKey="covoijhipsterApp.passager.acceptRadio">Accept Radio</Translate>
              </span>
            </dt>
            <dd>{passagerEntity.acceptRadio ? 'true' : 'false'}</dd>
            <dt>
              <span id="telephone">
                <Translate contentKey="covoijhipsterApp.passager.telephone">Telephone</Translate>
              </span>
            </dt>
            <dd>{passagerEntity.telephone}</dd>
            <dt>
              <Translate contentKey="covoijhipsterApp.passager.user">User</Translate>
            </dt>
            <dd>{passagerEntity.userEmail ? passagerEntity.userEmail : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/passager" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/passager/${passagerEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ passager }: IRootState) => ({
  passagerEntity: passager.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PassagerDetail);
