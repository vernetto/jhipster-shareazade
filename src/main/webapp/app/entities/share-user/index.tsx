import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ShareUser from './share-user';
import ShareUserDetail from './share-user-detail';
import ShareUserUpdate from './share-user-update';
import ShareUserDeleteDialog from './share-user-delete-dialog';

const ShareUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ShareUser />} />
    <Route path="new" element={<ShareUserUpdate />} />
    <Route path=":id">
      <Route index element={<ShareUserDetail />} />
      <Route path="edit" element={<ShareUserUpdate />} />
      <Route path="delete" element={<ShareUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShareUserRoutes;
