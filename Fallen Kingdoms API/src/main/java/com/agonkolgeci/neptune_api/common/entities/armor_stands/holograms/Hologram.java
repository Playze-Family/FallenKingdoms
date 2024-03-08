package com.agonkolgeci.neptune_api.common.entities.armor_stands.holograms;

import com.agonkolgeci.neptune_api.common.entities.EntitiesController;
import com.agonkolgeci.neptune_api.plugin.PluginAddon;
import com.agonkolgeci.neptune_api.utils.minecraft.entities.EntityUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Hologram extends PluginAddon<EntitiesController> {

    public static final double COMPONENT_LINE_SPACING = 0.25;
    public static final double ITEM_STACK_LINE_SPACING = 0.6;

    @Getter @NotNull private final World world;

    @Getter @NotNull private final Location location;
    @Getter @NotNull private final Location nextLocation;

    @Getter @NotNull private final Direction direction;

    @Getter @NotNull private final List<UUID> lines;

    public Hologram(@NotNull EntitiesController module, @NotNull Location location, @NotNull Direction direction) {
        super(module);

        this.world = location.getWorld();

        this.location = location;
        this.nextLocation = location;
        this.direction = direction;

        this.lines = new ArrayList<>();
    }

    public Hologram(@NotNull EntitiesController module, @NotNull Location location, @NotNull String title, @NotNull Direction direction) {
        this(module, location, direction);

        addLine(title);
    }

//    public Hologram(@NotNull EntitiesController module, @NotNull Location location, @NotNull ItemStack itemStack, @NotNull Direction direction) {
//        this(module, location, direction);
//
//        addLine(itemStack);
//    }

    @NotNull
    public List<Entity> retrieveEntities() {
        return lines.stream().map(module::retrieveEntity).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void unload() {
        lines.forEach(module::removeEntity);
    }

    public void teleport(double x, double z) {
        for (@NotNull final Entity entity : retrieveEntities()) {
            @NotNull final Location newLocation = entity.getLocation().clone();

            newLocation.setX(x);
            newLocation.setZ(z);

            entity.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    @NotNull
    private Hologram registerLine(@NotNull Entity entity) {
        lines.add(entity.getUniqueId());

        return this;
    }

    @NotNull
    public Hologram addLine(@NotNull String text) {
        if(!text.isEmpty()) {
            @NotNull final ArmorStand armorStand = EntityUtils.createEntity(nextLocation.clone().subtract(0, 1, 0), ArmorStand.class);

            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(text);

            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setBasePlate(false);
            armorStand.setSmall(true);

            registerLine(armorStand);
        }

        direction.translateLocation(nextLocation, COMPONENT_LINE_SPACING);

        return this;
    }

//    @NotNull
//    public Hologram addLine(@NotNull ItemStack itemStack) {
//        @NotNull final Item item = EntityUtils.createEntity(nextLocation.clone(), Item.class);
//
//        item.setGravity(false);
//        item.setVelocity(new Vector(0, 0, 0));
//        item.setCanMobPickup(false);
//        item.setCanPlayerPickup(false);
//        item.setUnlimitedLifetime(true);
//        item.setWillAge(false);
//
//        item.setItemStack(itemStack);
//
//        direction.translateLocation(nextLocation, ITEM_STACK_LINE_SPACING);
//
//        return registerLine(item);
//    }

    public enum Direction {

        UP {
            @Override
            public @NotNull Location translateLocation(@NotNull Location location, double value) {
                return location.add(0, value, 0);
            }
        },

        DOWN {
            @Override
            public @NotNull Location translateLocation(@NotNull Location location, double value) {
                return location.subtract(0, value, 0);
            }
        }

        ;

        @NotNull public abstract Location translateLocation(@NotNull Location location, double value);

    }
}
