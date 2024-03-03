package com.agonkolgeci.fk_loots;

import com.agonkolgeci.fk_loots.loots.LootsController;
import com.agonkolgeci.neptune_api.NeptunePlugin;
import com.agonkolgeci.neptune_api.common.config.ConfigController;
import lombok.Getter;

@Getter
public class FkLoots extends NeptunePlugin {

    @Getter private static FkLoots instance;

    private ConfigController configController;

    private LootsController lootsController;

    @Override
    public void onInstance() throws Exception {
        instance = this;

        configController = new ConfigController(instance);

        lootsController = new LootsController(instance).load();
    }

    @Override
    public void onDisable() {
        lootsController.unload();

        super.onDisable();
    }
}