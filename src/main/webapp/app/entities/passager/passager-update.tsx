import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './passager.reducer';
import { IPassager } from 'app/shared/model/passager.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPassagerUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPassagerUpdateState {
  isNew: boolean;
  userId: string;
}

export class PassagerUpdate extends React.Component<IPassagerUpdateProps, IPassagerUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
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

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { passagerEntity } = this.props;
      const entity = {
        ...passagerEntity,
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
    this.props.history.push('/entity/passager');
  };

  render() {
    const { passagerEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="covoijhipsterApp.passager.home.createOrEditLabel">
              <Translate contentKey="covoijhipsterApp.passager.home.createOrEditLabel">Create or edit a Passager</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : passagerEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="passager-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="passager-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="fumeurLabel" check>
                    <AvInput id="passager-fumeur" type="checkbox" className="form-control" name="fumeur" />
                    <Translate contentKey="covoijhipsterApp.passager.fumeur">Fumeur</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="acceptMusicLabel" check>
                    <AvInput id="passager-acceptMusic" type="checkbox" className="form-control" name="acceptMusic" />
                    <Translate contentKey="covoijhipsterApp.passager.acceptMusic">Accept Music</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="acceptRadioLabel" check>
                    <AvInput id="passager-acceptRadio" type="checkbox" className="form-control" name="acceptRadio" />
                    <Translate contentKey="covoijhipsterApp.passager.acceptRadio">Accept Radio</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="telephoneLabel" for="passager-telephone">
                    <Translate contentKey="covoijhipsterApp.passager.telephone">Telephone</Translate>
                  </Label>
                  <AvField
                    id="passager-telephone"
                    type="text"
                    name="telephone"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="passager-user">
                    <Translate contentKey="covoijhipsterApp.passager.user">User</Translate>
                  </Label>
                  <AvInput id="passager-user" type="select" className="form-control" name="userId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.email}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/passager" replace color="info">
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
  users: storeState.userManagement.users,
  passagerEntity: storeState.passager.entity,
  loading: storeState.passager.loading,
  updating: storeState.passager.updating,
  updateSuccess: storeState.passager.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
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
)(PassagerUpdate);
