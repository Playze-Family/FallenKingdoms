package com.agonkolgeci.fk_spawners.items;

import com.agonkolgeci.fk_spawners.FkSpawners;
import com.agonkolgeci.neptune_api.NeptuneAPI;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.utils.minecraft.entities.EntityUtils;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import com.agonkolgeci.neptune_api.utils.objects.list.CircularQueue;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ItemsSpawner {

    @NotNull private final List<Location> locations;
    @NotNull private final CircularQueue<ItemStack> items;

    @NotNull private final Type type;
    @NotNull private final EnumWrappers.Particle particle;

    private final int distance;
    private final int interval;

    @Nullable private BukkitTask spawnerTask;

    public ItemsSpawner(@NotNull ConfigSection configuration) {
        this.locations = configuration.of("locations").keys(c -> c.require("location", "Incorrect Location"));
        this.items = new CircularQueue<>(configuration.of("items").keys(c -> c.require("item", "Incorrect Item")));

        this.type = ObjectUtils.fetchObject(Type.class, configuration.get("type"), "Unknown Type: '%s'");
        this.particle = ObjectUtils.fetchObject(EnumWrappers.Particle.class, configuration.get("particle"), "Unknown Particle: '%s'");

        this.distance = configuration.require("distance", "Incorrect Distance");
        this.interval = configuration.require("interval", "Incorrect Interval");
    }

    public void enable() {
        if(spawnerTask == null) {
            spawnerTask = Bukkit.getScheduler().runTaskTimer(FkSpawners.getInstance(), this::spawnItems, 0, ObjectUtils.retrieveTicks(interval));
        }
    }

    public void disable() {
        if(spawnerTask != null) {
            spawnerTask.cancel();
        }
    }

    public void spawnItems() {
        for(@NotNull Location location : locations) {
            @NotNull final List<Player> players = EntityUtils.getNearByEntities(location, distance).stream().filter(entity -> entity instanceof Player).map(entity -> (Player) entity).collect(Collectors.toList());
            if(players.isEmpty()) {
                continue;
            }

            @NotNull final World world = location.getWorld();
            switch (type) {
                case ALL: {
                    for(@NotNull ItemStack itemStack : items) {
                        world.dropItem(location, itemStack);
                    }

                    break;
                }

                case CIRCULAR: {
                    world.dropItem(location, items.next());

                    break;
                }

                case RANDOM: {
                    world.dropItem(location, ObjectUtils.retrieveRandomObject(items));

                    break;
                }
            }

            players.forEach(player -> spawnParticle(player, location));
        }
    }

    protected void spawnParticle(@NotNull Player player, @NotNull Location location) {
        @NotNull final PacketContainer particlePacket = new PacketContainer(PacketType.Play.Server.WORLD_PARTICLES);

        particlePacket.getParticles().write(0, particle);
        particlePacket.getFloat().write(0, (float) location.getX()).write(1, (float) location.getY()).write(2, (float) location.getZ());
        particlePacket.getFloat().write(3, 1F).write(4, 1F).write(5, 1F);
        particlePacket.getIntegers().write(0, distance);

        NeptuneAPI.getProtocolManager().sendServerPacket(player, particlePacket);
    }

    public enum Type {

        ALL,
        CIRCULAR,
        RANDOM

    }

}
