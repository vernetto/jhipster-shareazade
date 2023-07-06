import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ShareCity from './share-city';
import ShareCityDetail from './share-city-detail';
import ShareCityUpdate from './share-city-update';
import ShareCityDeleteDialog from './share-city-delete-dialog';

const ShareCityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ShareCity />} />
    <Route path="new" element={<ShareCityUpdate />} />
    <Route path=":id">
      <Route index element={<ShareCityDetail />} />
      <Route path="edit" element={<ShareCityUpdate />} />
      <Route path="delete" element={<ShareCityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShareCityRoutes;
