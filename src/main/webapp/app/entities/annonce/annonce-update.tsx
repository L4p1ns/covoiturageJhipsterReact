import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IChauffeur } from 'app/shared/model/chauffeur.model';
import { getEntities as getChauffeurs } from 'app/entities/chauffeur/chauffeur.reducer';
import { getEntity, updateEntity, createEntity, reset } from './annonce.reducer';
import { IAnnonce } from 'app/shared/model/annonce.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAnnonceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAnnonceUpdateState {
  isNew: boolean;
  chauffeurId: string;
}

export class AnnonceUpdate extends React.Component<IAnnonceUpdateProps, IAnnonceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      chauffeurId: '0',
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

    this.props.getChauffeurs();
  }

  saveEntity = (event, errors, values) => {
    values.dateVoyage = convertDateTimeToServer(values.dateVoyage);
    values.dateCreation = convertDateTimeToServer(values.dateCreation);

    if (errors.length === 0) {
      const { annonceEntity } = this.props;
      const entity = {
        ...annonceEntity,
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
    this.props.history.push('/entity/annonce');
  };

  render() {
    const { annonceEntity, chauffeurs, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="covoijhipsterApp.annonce.home.createOrEditLabel">
              <Translate contentKey="covoijhipsterApp.annonce.home.createOrEditLabel">Create or edit a Annonce</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : annonceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="annonce-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="annonce-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nomLabel" for="annonce-nom">
                    <Translate contentKey="covoijhipsterApp.annonce.nom">Nom</Translate>
                  </Label>
                  <AvField
                    id="annonce-nom"
                    type="text"
                    name="nom"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 5, errorMessage: translate('entity.validation.minlength', { min: 5 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lieuDepartLabel" for="annonce-lieuDepart">
                    <Translate contentKey="covoijhipsterApp.annonce.lieuDepart">Lieu Depart</Translate>
                  </Label>
                  <AvField
                    id="annonce-lieuDepart"
                    type="text"
                    name="lieuDepart"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 5, errorMessage: translate('entity.validation.minlength', { min: 5 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="lieuArriveeLabel" for="annonce-lieuArrivee">
                    <Translate contentKey="covoijhipsterApp.annonce.lieuArrivee">Lieu Arrivee</Translate>
                  </Label>
                  <AvField
                    id="annonce-lieuArrivee"
                    type="text"
                    name="lieuArrivee"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 5, errorMessage: translate('entity.validation.minlength', { min: 5 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="dateVoyageLabel" for="annonce-dateVoyage">
                    <Translate contentKey="covoijhipsterApp.annonce.dateVoyage">Date Voyage</Translate>
                  </Label>
                  <AvInput
                    id="annonce-dateVoyage"
                    type="datetime-local"
                    className="form-control"
                    name="dateVoyage"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.annonceEntity.dateVoyage)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="detailLabel" for="annonce-detail">
                    <Translate contentKey="covoijhipsterApp.annonce.detail">Detail</Translate>
                  </Label>
                  <AvField id="annonce-detail" type="text" name="detail" />
                </AvGroup>
                <AvGroup>
                  <Label id="musicLabel" check>
                    <AvInput id="annonce-music" type="checkbox" className="form-control" name="music" />
                    <Translate contentKey="covoijhipsterApp.annonce.music">Music</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="fumerLabel" check>
                    <AvInput id="annonce-fumer" type="checkbox" className="form-control" name="fumer" />
                    <Translate contentKey="covoijhipsterApp.annonce.fumer">Fumer</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="radioLabel" check>
                    <AvInput id="annonce-radio" type="checkbox" className="form-control" name="radio" />
                    <Translate contentKey="covoijhipsterApp.annonce.radio">Radio</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="dateCreationLabel" for="annonce-dateCreation">
                    <Translate contentKey="covoijhipsterApp.annonce.dateCreation">Date Creation</Translate>
                  </Label>
                  <AvInput
                    id="annonce-dateCreation"
                    type="datetime-local"
                    className="form-control"
                    name="dateCreation"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.annonceEntity.dateCreation)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="etatAnnonceLabel" for="annonce-etatAnnonce">
                    <Translate contentKey="covoijhipsterApp.annonce.etatAnnonce">Etat Annonce</Translate>
                  </Label>
                  <AvInput
                    id="annonce-etatAnnonce"
                    type="select"
                    className="form-control"
                    name="etatAnnonce"
                    value={(!isNew && annonceEntity.etatAnnonce) || 'ACTIVE'}
                  >
                    <option value="ACTIVE">{translate('covoijhipsterApp.EtatAnnonce.ACTIVE')}</option>
                    <option value="ANNULER">{translate('covoijhipsterApp.EtatAnnonce.ANNULER')}</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="annonce-chauffeur">
                    <Translate contentKey="covoijhipsterApp.annonce.chauffeur">Chauffeur</Translate>
                  </Label>
                  <AvInput id="annonce-chauffeur" type="select" className="form-control" name="chauffeurId">
                    <option value="" key="0" />
                    {chauffeurs
                      ? chauffeurs.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/annonce" replace color="info">
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
  chauffeurs: storeState.chauffeur.entities,
  annonceEntity: storeState.annonce.entity,
  loading: storeState.annonce.loading,
  updating: storeState.annonce.updating,
  updateSuccess: storeState.annonce.updateSuccess
});

const mapDispatchToProps = {
  getChauffeurs,
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
)(AnnonceUpdate);
