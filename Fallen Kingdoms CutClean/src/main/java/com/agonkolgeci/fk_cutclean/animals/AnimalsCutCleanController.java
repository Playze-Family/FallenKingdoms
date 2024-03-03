package com.agonkolgeci.fk_cutclean.animals;

import com.agonkolgeci.fk_cutclean.FkCutClean;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class AnimalsCutCleanController extends PluginModule<FkCutClean> implements PluginController<AnimalsCutCleanController>, ListenerAdapter {

    private final boolean active;

    public AnimalsCutCleanController(@NotNull FkCutClean instance) {
        super(instance);

        this.active = instance.getConfigController().of().get("animals", false);
    }

    @Override
    public AnimalsCutCleanController load() {
        instance.getEventsController().registerEventAdapter(this);

        return this;
    }

    @Override
    public void unload() {
        instance.getEventsController().unregisterEventAdapter(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(@Nonnull EntityDeathEvent event) {
        if(!active) return;

        for(@NotNull final ItemStack itemStack : event.getDrops()) {
            @Nullable final Material burnedMaterial = burnMaterial(itemStack.getType());

            if(burnedMaterial != null) {
                event.getDrops().removeIf(is -> is.isSimilar(itemStack));
                event.getDrops().add(new ItemStack(burnedMaterial, itemStack.getAmount()));
            }
        }
    }

    @Nullable
    public static Material burnMaterial(@NotNull Material material) {
        switch (material) {
            case PORK: return Material.GRILLED_PORK;
            case RAW_FISH: return Material.RAW_FISH;
            case RAW_BEEF: return Material.COOKED_BEEF;
            case RAW_CHICKEN: return Material.COOKED_CHICKEN;
            case RABBIT: return Material.COOKED_RABBIT;
            case MUTTON: return Material.COOKED_MUTTON;
        }

        return null;
    }

}
