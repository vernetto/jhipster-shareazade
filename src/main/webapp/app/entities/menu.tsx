import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/share-ride">
        <Translate contentKey="global.menu.entities.shareRide" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/share-city">
        <Translate contentKey="global.menu.entities.shareCity" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
