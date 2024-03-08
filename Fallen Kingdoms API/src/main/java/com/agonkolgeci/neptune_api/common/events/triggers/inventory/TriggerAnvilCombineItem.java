package com.agonkolgeci.neptune_api.common.events.triggers.inventory;

import com.agonkolgeci.neptune_api.common.events.EventsController;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.common.events.custom.inventory.AnvilCombineItemsEvent;
import com.agonkolgeci.neptune_api.plugin.PluginAddon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerAnvilCombineItem extends PluginAddon<EventsController> implements ListenerAdapter {

    public TriggerAnvilCombineItem(@NotNull EventsController module) {
        super(module);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        @NotNull final Inventory inventory = event.getInventory();

        if(!(inventory instanceof AnvilInventory)) return;
        if(event.getRawSlot() != 2) return;

        @Nullable final ItemStack firstItem = inventory.getItem(0);
        @Nullable final ItemStack secondItem = inventory.getItem(1);
        @Nullable final ItemStack resultItem = inventory.getItem(2);
        if(firstItem == null || secondItem == null || resultItem == null) return;

        @NotNull final AnvilCombineItemsEvent anvilCombineItemsEvent = new AnvilCombineItemsEvent(event, firstItem, secondItem, resultItem);

        module.callEvent(anvilCombineItemsEvent);

        if(anvilCombineItemsEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

}
