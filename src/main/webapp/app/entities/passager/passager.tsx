import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './passager.reducer';
import { IPassager } from 'app/shared/model/passager.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPassagerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IPassagerState {
  search: string;
}

export class Passager extends React.Component<IPassagerProps, IPassagerState> {
  state: IPassagerState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { passagerList, match } = this.props;
    return (
      <div>
        <h2 id="passager-heading">
          <Translate contentKey="covoijhipsterApp.passager.home.title">Passagers</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="covoijhipsterApp.passager.home.createLabel">Create a new Passager</Translate>
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder={translate('covoijhipsterApp.passager.home.search')}
                  />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {passagerList && passagerList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="covoijhipsterApp.passager.fumeur">Fumeur</Translate>
                  </th>
                  <th>
                    <Translate contentKey="covoijhipsterApp.passager.acceptMusic">Accept Music</Translate>
                  </th>
                  <th>
                    <Translate contentKey="covoijhipsterApp.passager.acceptRadio">Accept Radio</Translate>
                  </th>
                  <th>
                    <Translate contentKey="covoijhipsterApp.passager.telephone">Telephone</Translate>
                  </th>
                  <th>
                    <Translate contentKey="covoijhipsterApp.passager.user">User</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {passagerList.map((passager, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${passager.id}`} color="link" size="sm">
                        {passager.id}
                      </Button>
                    </td>
                    <td>{passager.fumeur ? 'true' : 'false'}</td>
                    <td>{passager.acceptMusic ? 'true' : 'false'}</td>
                    <td>{passager.acceptRadio ? 'true' : 'false'}</td>
                    <td>{passager.telephone}</td>
                    <td>{passager.userEmail ? passager.userEmail : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${passager.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${passager.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${passager.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="covoijhipsterApp.passager.home.notFound">No Passagers found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ passager }: IRootState) => ({
  passagerList: passager.entities
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Passager);
