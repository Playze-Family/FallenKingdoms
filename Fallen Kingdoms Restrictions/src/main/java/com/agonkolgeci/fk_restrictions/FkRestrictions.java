package com.agonkolgeci.fk_restrictions;

import com.agonkolgeci.fk_restrictions.enchants.EnchantsRestrictionsController;
import com.agonkolgeci.fk_restrictions.items.ItemsRestrictionsController;
import com.agonkolgeci.neptune_api.FkPlugin;
import com.agonkolgeci.neptune_api.common.config.ConfigController;
import lombok.Getter;

@Getter
public class FkRestrictions extends FkPlugin {

    @Getter private static FkRestrictions instance;

    private ConfigController configController;

    private ItemsRestrictionsController itemsRestrictionsController;
    private EnchantsRestrictionsController enchantsRestrictionsController;

    @Override
    public void onInstance() throws Exception {
        instance = this;

        configController = new ConfigController(instance);

        itemsRestrictionsController = new ItemsRestrictionsController(instance).load();
        enchantsRestrictionsController = new EnchantsRestrictionsController(instance).load();
    }

    @Override
    public void onDisable() {
        itemsRestrictionsController.unload();
        enchantsRestrictionsController.unload();

        super.onDisable();
    }
}