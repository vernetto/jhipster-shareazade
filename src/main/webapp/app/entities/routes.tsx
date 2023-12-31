import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ShareRide from './share-ride';
import ShareCity from './share-city';
import ShareUser from './share-user';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="share-ride/*" element={<ShareRide />} />
        <Route path="share-city/*" element={<ShareCity />} />
        <Route path="share-user/*" element={<ShareUser />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
