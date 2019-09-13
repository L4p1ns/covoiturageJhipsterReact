import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAnnonce } from 'app/shared/model/annonce.model';
import { getEntities as getAnnonces } from 'app/entities/annonce/annonce.reducer';
import { IPassager } from 'app/shared/model/passager.model';
import { getEntities as getPassagers } from 'app/entities/passager/passager.reducer';
import { getEntity, updateEntity, createEntity, reset } from './reservation.reducer';
import { IReservation } from 'app/shared/model/reservation.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IReservationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IReservationUpdateState {
  isNew: boolean;
  annonceId: string;
  passagerId: string;
}

export class ReservationUpdate extends React.Component<IReservationUpdateProps, IReservationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      annonceId: '0',
      passagerId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getAnnonces();
    this.props.getPassagers();
  }

  saveEntity = (event, errors, values) => {
    values.dateReservation = convertDateTimeToServer(values.dateReservation);

    if (errors.length === 0) {
      const { reservationEntity } = this.props;
      const entity = {
        ...reservationEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/reservation');
  };

  render() {
    const { reservationEntity, annonces, passagers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="covoijhipsterApp.reservation.home.createOrEditLabel">
              <Translate contentKey="covoijhipsterApp.reservation.home.createOrEditLabel">Create or edit a Reservation</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : reservationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="reservation-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="reservation-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateReservationLabel" for="reservation-dateReservation">
                    <Translate contentKey="covoijhipsterApp.reservation.dateReservation">Date Reservation</Translate>
                  </Label>
                  <AvInput
                    id="reservation-dateReservation"
                    type="datetime-local"
                    className="form-control"
                    name="dateReservation"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.reservationEntity.dateReservation)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nombreDePlaceLabel" for="reservation-nombreDePlace">
                    <Translate contentKey="covoijhipsterApp.reservation.nombreDePlace">Nombre De Place</Translate>
                  </Label>
                  <AvField
                    id="reservation-nombreDePlace"
                    type="string"
                    className="form-control"
                    name="nombreDePlace"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="reservation-status">
                    <Translate contentKey="covoijhipsterApp.reservation.status">Status</Translate>
                  </Label>
                  <AvInput
                    id="reservation-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && reservationEntity.status) || 'ANVOYER'}
                  >
                    <option value="ANVOYER">{translate('covoijhipsterApp.StatusReservation.ANVOYER')}</option>
                    <option value="ACCEPTER">{translate('covoijhipsterApp.StatusReservation.ACCEPTER')}</option>
                    <option value="CONFIRMER">{translate('covoijhipsterApp.StatusReservation.CONFIRMER')}</option>
                    <option value="REFUSER">{translate('covoijhipsterApp.StatusReservation.REFUSER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="reservation-annonce">
                    <Translate contentKey="covoijhipsterApp.reservation.annonce">Annonce</Translate>
                  </Label>
                  <AvInput id="reservation-annonce" type="select" className="form-control" name="annonceId">
                    <option value="" key="0" />
                    {annonces
                      ? annonces.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.nom}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="reservation-passager">
                    <Translate contentKey="covoijhipsterApp.reservation.passager">Passager</Translate>
                  </Label>
                  <AvInput id="reservation-passager" type="select" className="form-control" name="passagerId">
                    <option value="" key="0" />
                    {passagers
                      ? passagers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/reservation" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  annonces: storeState.annonce.entities,
  passagers: storeState.passager.entities,
  reservationEntity: storeState.reservation.entity,
  loading: storeState.reservation.loading,
  updating: storeState.reservation.updating,
  updateSuccess: storeState.reservation.updateSuccess
});

const mapDispatchToProps = {
  getAnnonces,
  getPassagers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ReservationUpdate);
