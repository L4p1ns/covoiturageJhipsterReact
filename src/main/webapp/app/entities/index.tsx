import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Chauffeur from './chauffeur';
import Passager from './passager';
import Reservation from './reservation';
import Annonce from './annonce';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/chauffeur`} component={Chauffeur} />
      <ErrorBoundaryRoute path={`${match.url}/passager`} component={Passager} />
      <ErrorBoundaryRoute path={`${match.url}/reservation`} component={Reservation} />
      <ErrorBoundaryRoute path={`${match.url}/annonce`} component={Annonce} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
