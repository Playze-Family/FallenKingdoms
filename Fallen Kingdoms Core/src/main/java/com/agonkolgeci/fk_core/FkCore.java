package com.agonkolgeci.fk_core;

import com.agonkolgeci.fk_core.players.FkPlayersController;
import com.agonkolgeci.neptune_api.FkPlugin;
import com.agonkolgeci.neptune_api.common.config.ConfigController;
import lombok.Getter;

@Getter
public class FkCore extends FkPlugin {

    @Getter private static FkCore instance;

    private ConfigController configController;

    private FkPlayersController playersController;

    @Override
    public void onInstance() throws Exception {
        instance = this;

        configController = new ConfigController(instance);

        playersController = new FkPlayersController(instance, eventsController).load();
    }

    @Override
    public void onDisable() {
        playersController.unload();

        super.onDisable();
    }
}