package com.agonkolgeci.fk_spawners.items;

import com.agonkolgeci.fk_spawners.FkSpawners;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class ItemsSpawnersController extends PluginModule<FkSpawners> implements PluginController<ItemsSpawnersController> {

    @NotNull private final ConfigSection configuration;

    @NotNull private final List<ItemsSpawner> spawners;

    public ItemsSpawnersController(@NotNull FkSpawners instance) {
        super(instance);

        this.configuration = instance.getConfigController().of("items");

        this.spawners = retrieveSpawners(configuration);
    }

    @NotNull
    public static List<ItemsSpawner> retrieveSpawners(@NotNull ConfigSection configuration) {
        return configuration.keys(ItemsSpawner::new);
    }

    @Override
    public ItemsSpawnersController load() {
        spawners.forEach(ItemsSpawner::enable);

        return this;
    }

    @Override
    public void unload() {
        spawners.forEach(ItemsSpawner::disable);
    }
}
