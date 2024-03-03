package com.agonkolgeci.fk_protections;

import com.agonkolgeci.fk_protections.blocks.BlocksProtectionsController;
import com.agonkolgeci.neptune_api.NeptunePlugin;
import com.agonkolgeci.neptune_api.common.config.ConfigController;
import lombok.Getter;

@Getter
public class FkProtections extends NeptunePlugin {

    @Getter private static FkProtections instance;

    private ConfigController configController;

    private BlocksProtectionsController blocksProtectionsController;

    @Override
    public void onInstance() throws Exception {
        instance = this;

        configController = new ConfigController(instance);

        blocksProtectionsController = new BlocksProtectionsController(instance).load();
    }

    @Override
    public void onDisable() {
        blocksProtectionsController.unload();

        super.onDisable();
    }
}