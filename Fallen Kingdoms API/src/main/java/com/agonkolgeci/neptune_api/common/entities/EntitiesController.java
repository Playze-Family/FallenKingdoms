package com.agonkolgeci.neptune_api.common.entities;

import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import com.agonkolgeci.neptune_api.common.events.EventsController;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class EntitiesController extends PluginModule<JavaPlugin> implements PluginController<EntitiesController> {

    @Getter @NotNull private final EventsController eventsController;

    public EntitiesController(@NotNull JavaPlugin instance, @NotNull EventsController eventsController) {
        super(instance);

        this.eventsController = eventsController;
    }

    @NotNull
    @Override
    public EntitiesController load() {
        return this;
    }

    @Override
    public void unload() {
    }

    @Nullable
    public Entity retrieveEntity(@NotNull UUID uuid) {
        return server.getWorlds().stream().map(World::getEntities).flatMap(List::stream).filter(entity -> entity.getUniqueId().equals(uuid)).findAny().orElse(null);
    }

    public void removeEntity(@NotNull UUID uuid) {
        @Nullable final Entity entity = retrieveEntity(uuid);

        if(entity != null) {
            entity.remove();
        }
    }

}
