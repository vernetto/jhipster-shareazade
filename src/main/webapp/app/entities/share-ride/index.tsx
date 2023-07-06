import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ShareRide from './share-ride';
import ShareRideDetail from './share-ride-detail';
import ShareRideUpdate from './share-ride-update';
import ShareRideDeleteDialog from './share-ride-delete-dialog';

const ShareRideRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ShareRide />} />
    <Route path="new" element={<ShareRideUpdate />} />
    <Route path=":id">
      <Route index element={<ShareRideDetail />} />
      <Route path="edit" element={<ShareRideUpdate />} />
      <Route path="delete" element={<ShareRideDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShareRideRoutes;
