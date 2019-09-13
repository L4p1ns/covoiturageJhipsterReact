import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Passager from './passager';
import PassagerDetail from './passager-detail';
import PassagerUpdate from './passager-update';
import PassagerDeleteDialog from './passager-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PassagerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PassagerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PassagerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Passager} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PassagerDeleteDialog} />
  </>
);

export default Routes;
