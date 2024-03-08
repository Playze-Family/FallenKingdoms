package com.agonkolgeci.fk_spawners;

import com.agonkolgeci.fk_spawners.items.ItemsSpawnersController;
import com.agonkolgeci.neptune_api.FkPlugin;
import com.agonkolgeci.neptune_api.common.config.ConfigController;
import lombok.Getter;

@Getter
public class FkSpawners extends FkPlugin {

    @Getter private static FkSpawners instance;

    private ConfigController configController;

    private ItemsSpawnersController itemsSpawnersController;

    @Override
    public void onInstance() throws Exception {
        instance = this;

        configController = new ConfigController(instance);

        itemsSpawnersController = new ItemsSpawnersController(instance).load();
    }

    @Override
    public void onDisable() {
        itemsSpawnersController.unload();

        super.onDisable();
    }
}