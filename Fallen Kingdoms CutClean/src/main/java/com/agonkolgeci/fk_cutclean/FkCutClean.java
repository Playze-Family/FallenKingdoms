package com.agonkolgeci.fk_cutclean;

import com.agonkolgeci.fk_cutclean.animals.AnimalsCutCleanController;
import com.agonkolgeci.fk_cutclean.ores.OresCutCleanController;
import com.agonkolgeci.neptune_api.FkPlugin;
import com.agonkolgeci.neptune_api.common.config.ConfigController;
import lombok.Getter;

@Getter
public class FkCutClean extends FkPlugin {

    @Getter private static FkCutClean instance;

    private ConfigController configController;

    private AnimalsCutCleanController animalsCutCleanController;
    private OresCutCleanController oresCutCleanController;

    @Override
    public void onInstance() throws Exception {
        instance = this;

        configController = new ConfigController(instance);

        animalsCutCleanController = new AnimalsCutCleanController(instance).load();
        oresCutCleanController = new OresCutCleanController(instance).load();
    }

    @Override
    public void onDisable() {
        animalsCutCleanController.unload();
        oresCutCleanController.unload();

        super.onDisable();
    }
}