package com.agonkolgeci.neptune_api.common.entities.armor_stands;

import com.agonkolgeci.neptune_api.common.entities.EntitiesController;
import com.agonkolgeci.neptune_api.common.entities.armor_stands.holograms.Hologram;
import com.agonkolgeci.neptune_api.plugin.PluginAddon;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArmorStandsController extends PluginAddon<EntitiesController> implements PluginController<ArmorStandsController> {

    @Getter @NotNull private final List<Hologram> holograms;

    public ArmorStandsController(@NotNull EntitiesController module) {
        super(module);

        this.holograms = new ArrayList<>();
    }

    @Override
    public ArmorStandsController load() {
        return this;
    }

    @Override
    public void unload() {
        holograms.forEach(Hologram::unload);
    }

    @NotNull
    public Hologram registerHologram(@NotNull Hologram hologram) {
        holograms.add(hologram);
        return hologram;
    }

    @NotNull
    public Hologram createHologram(@NotNull Location location, @NotNull String title, @NotNull Hologram.Direction direction) {
        return registerHologram(new Hologram(module, location, title, direction));
    }

//    @NotNull
//    public Hologram createHologram(@NotNull Location location, @NotNull ItemStack itemStack, @NotNull Hologram.Direction direction) {
//        return registerHologram(new Hologram(module, location, itemStack, direction));
//    }

    public void deleteHologram(@NotNull Hologram hologram) {
        holograms.remove(hologram);
        hologram.unload();
    }
}
