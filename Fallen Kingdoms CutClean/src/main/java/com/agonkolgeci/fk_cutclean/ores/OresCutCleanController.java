package com.agonkolgeci.fk_cutclean.ores;

import com.agonkolgeci.fk_cutclean.FkCutClean;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class OresCutCleanController extends PluginModule<FkCutClean> implements PluginController<OresCutCleanController>, ListenerAdapter {

    private final boolean active;

    public OresCutCleanController(@NotNull FkCutClean instance) {
        super(instance);

        this.active = instance.getConfigController().of().get("ores", false);
    }

    @Override
    public OresCutCleanController load() {
        instance.getEventsController().registerEventAdapter(this);

        return this;
    }

    @Override
    public void unload() {
        instance.getEventsController().unregisterEventAdapter(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemSpawn(@Nonnull ItemSpawnEvent event) {
        if(!active) return;

        @NotNull final ItemStack itemStack = event.getEntity().getItemStack();
        @Nullable final Material blastedMaterial = blastMaterial(itemStack.getType());

        if(blastedMaterial != null) {
            itemStack.setType(blastedMaterial);
        }
    }

    @Nullable
    public static Material blastMaterial(@NotNull Material material) {
        switch (material) {
            case IRON_ORE: return Material.IRON_INGOT;
            case GOLD_ORE: return Material.GOLD_INGOT;
        }

        return null;
    }

}
