package com.agonkolgeci.fk_restrictions.items;

import com.agonkolgeci.fk_restrictions.FkRestrictions;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import com.agonkolgeci.neptune_api.utils.minecraft.messages.MessageUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemsRestrictionsController extends PluginModule<FkRestrictions> implements PluginController<ItemsRestrictionsController>, ListenerAdapter {

    @NotNull private final ConfigSection configuration;

    @NotNull private final List<ItemRestriction> restrictions;

    public ItemsRestrictionsController(@NotNull FkRestrictions instance) {
        super(instance);

        this.configuration = instance.getConfigController().of("items");

        this.restrictions = retrieveRestrictions(configuration);
    }

    private static final String FORBIDDEN_ITEM_WARN_CREATIVE = "Attention, cet objet n'est pas autorisé en survie.";
    private static final String FORBIDDEN_ITEM_ERROR = "Cet objet n'est pas autorisé.";

    @Override
    public ItemsRestrictionsController load() {
        instance.getEventsController().registerEventAdapter(this);

        return this;
    }

    @Override
    public void unload() {
        instance.getEventsController().unregisterEventAdapter(this);
    }

    @NotNull
    public static List<ItemRestriction> retrieveRestrictions(@NotNull ConfigSection configuration) {
        return configuration.keys(ItemRestriction::new);
    }

    public boolean isRestricted(@NotNull ItemStack itemStack) {
        return restrictions.stream().anyMatch(restriction -> restriction.isRestricted(itemStack));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCraftItem(@NotNull CraftItemEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;

        @NotNull final Player player = (Player) event.getWhoClicked();

        try {
            @NotNull final ItemStack itemStack = event.getRecipe().getResult();

            if(isRestricted(itemStack)) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    MessageUtils.warn(player, FORBIDDEN_ITEM_WARN_CREATIVE);

                    return;
                }

                throw new IllegalStateException(FORBIDDEN_ITEM_ERROR);
            }
        } catch (IllegalStateException exception) {
            event.setCancelled(true);

            player.closeInventory();

            MessageUtils.severe(player, exception);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        @NotNull final Player player = event.getPlayer();
        @NotNull final ItemStack itemStack = event.getItem();
        if(itemStack == null) return;

        try {
            if(isRestricted(itemStack)) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    MessageUtils.warn(player, FORBIDDEN_ITEM_WARN_CREATIVE);

                    return;
                }

                throw new IllegalStateException(FORBIDDEN_ITEM_ERROR);
            }
        } catch (IllegalStateException exception) {
            event.setCancelled(true);

            player.getInventory().remove(itemStack);

            MessageUtils.severe(player, exception);
        }
    }

}
