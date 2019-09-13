import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './annonce.reducer';
import { IAnnonce } from 'app/shared/model/annonce.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAnnonceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AnnonceDetail extends React.Component<IAnnonceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { annonceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="covoijhipsterApp.annonce.detail.title">Annonce</Translate> [<b>{annonceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="nom">
                <Translate contentKey="covoijhipsterApp.annonce.nom">Nom</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.nom}</dd>
            <dt>
              <span id="lieuDepart">
                <Translate contentKey="covoijhipsterApp.annonce.lieuDepart">Lieu Depart</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.lieuDepart}</dd>
            <dt>
              <span id="lieuArrivee">
                <Translate contentKey="covoijhipsterApp.annonce.lieuArrivee">Lieu Arrivee</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.lieuArrivee}</dd>
            <dt>
              <span id="dateVoyage">
                <Translate contentKey="covoijhipsterApp.annonce.dateVoyage">Date Voyage</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={annonceEntity.dateVoyage} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="detail">
                <Translate contentKey="covoijhipsterApp.annonce.detail">Detail</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.detail}</dd>
            <dt>
              <span id="music">
                <Translate contentKey="covoijhipsterApp.annonce.music">Music</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.music ? 'true' : 'false'}</dd>
            <dt>
              <span id="fumer">
                <Translate contentKey="covoijhipsterApp.annonce.fumer">Fumer</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.fumer ? 'true' : 'false'}</dd>
            <dt>
              <span id="radio">
                <Translate contentKey="covoijhipsterApp.annonce.radio">Radio</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.radio ? 'true' : 'false'}</dd>
            <dt>
              <span id="dateCreation">
                <Translate contentKey="covoijhipsterApp.annonce.dateCreation">Date Creation</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={annonceEntity.dateCreation} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="etatAnnonce">
                <Translate contentKey="covoijhipsterApp.annonce.etatAnnonce">Etat Annonce</Translate>
              </span>
            </dt>
            <dd>{annonceEntity.etatAnnonce}</dd>
            <dt>
              <Translate contentKey="covoijhipsterApp.annonce.chauffeur">Chauffeur</Translate>
            </dt>
            <dd>{annonceEntity.chauffeurId ? annonceEntity.chauffeurId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/annonce" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/annonce/${annonceEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ annonce }: IRootState) => ({
  annonceEntity: annonce.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AnnonceDetail);
