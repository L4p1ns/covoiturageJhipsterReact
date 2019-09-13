import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './reservation.reducer';
import { IReservation } from 'app/shared/model/reservation.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReservationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ReservationDetail extends React.Component<IReservationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { reservationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="covoijhipsterApp.reservation.detail.title">Reservation</Translate> [<b>{reservationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dateReservation">
                <Translate contentKey="covoijhipsterApp.reservation.dateReservation">Date Reservation</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={reservationEntity.dateReservation} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="nombreDePlace">
                <Translate contentKey="covoijhipsterApp.reservation.nombreDePlace">Nombre De Place</Translate>
              </span>
            </dt>
            <dd>{reservationEntity.nombreDePlace}</dd>
            <dt>
              <span id="status">
                <Translate contentKey="covoijhipsterApp.reservation.status">Status</Translate>
              </span>
            </dt>
            <dd>{reservationEntity.status}</dd>
            <dt>
              <Translate contentKey="covoijhipsterApp.reservation.annonce">Annonce</Translate>
            </dt>
            <dd>{reservationEntity.annonceNom ? reservationEntity.annonceNom : ''}</dd>
            <dt>
              <Translate contentKey="covoijhipsterApp.reservation.passager">Passager</Translate>
            </dt>
            <dd>{reservationEntity.passagerId ? reservationEntity.passagerId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/reservation" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/reservation/${reservationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ reservation }: IRootState) => ({
  reservationEntity: reservation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReservationDetail);
