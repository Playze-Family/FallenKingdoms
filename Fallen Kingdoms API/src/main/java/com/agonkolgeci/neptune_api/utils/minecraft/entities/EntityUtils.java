package com.agonkolgeci.neptune_api.utils.minecraft.entities;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {

    public static <T extends Entity> T createEntity(@NotNull Location location, @NotNull Class<T> clazz) {
        @NotNull final World world = location.getWorld();
        @NotNull final Chunk chunk = location.getChunk();

        @NotNull final T entity = world.spawn(location, clazz);

        return entity;
    }

    @NotNull
    public static List<Entity> getNearByEntities(@NotNull Location location, int distance) {
        return location.getWorld().getEntities().stream().filter(entity -> entity.getLocation().distance(location) <= distance).collect(Collectors.toList());
    }

}
