package com.agonkolgeci.fk_restrictions.enchants;

import com.agonkolgeci.fk_restrictions.FkRestrictions;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.common.events.custom.inventory.AnvilCombineItemsEvent;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import com.agonkolgeci.neptune_api.utils.minecraft.inventory.ItemBuilder;
import com.agonkolgeci.neptune_api.utils.minecraft.messages.MessageUtils;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

@Getter
public class EnchantsRestrictionsController extends PluginModule<FkRestrictions> implements PluginController<EnchantsRestrictionsController>, ListenerAdapter {

    @NotNull private final ConfigSection configuration;

    @NotNull private final Map<Enchantment, EnchantRestriction> restrictions;

    public EnchantsRestrictionsController(@NotNull FkRestrictions instance) {
        super(instance);

        this.configuration = instance.getConfigController().of("enchantments");

        this.restrictions = retrieveRestrictions(configuration);
    }

    @Override
    public EnchantsRestrictionsController load() {
        instance.getEventsController().registerEventAdapter(this);

        return this;
    }

    @Override
    public void unload() {
        instance.getEventsController().unregisterEventAdapter(this);
    }

    @NotNull
    public static Map<Enchantment, EnchantRestriction> retrieveRestrictions(@NotNull ConfigSection configuration) {
        return configuration.keys(c -> Objects.requireNonNull(Enchantment.getByName(c.getName().toUpperCase()), String.format("Unknown Enchantment '%s'", c)), EnchantRestriction::new);
    }

    public boolean isRestricted(@NotNull ItemStack itemStack) {
        if(instance.getItemsRestrictionsController().isRestricted(itemStack)) {
            return true;
        }

        return restrictions.values().stream().anyMatch(restriction -> restriction.isRestricted(itemStack));
    }

    private static final String FORBIDDEN_ENCHANTMENTS_WARN_CREATIVE = "Attention, les enchantements que vous essayez d'appliquer sur votre objet ne sont pas autorisés en survie.";
    private static final String FORBIDDEN_ENCHANTMENTS_ERROR = "Les enchantements que vous essayez d'appliquer sur votre objet ne sont pas autorisés.";

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerEnchantItem(@NotNull EnchantItemEvent event) {
        @NotNull final Player player = event.getEnchanter();
        @NotNull final ItemStack itemStack = event.getItem();
        if(itemStack == null) return;

        try {
            @NotNull final ItemStack resultItem = new ItemBuilder<>(itemStack.clone()).addEnchantments(event.getEnchantsToAdd(), true).toItemStack();

            if(isRestricted(resultItem)) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    MessageUtils.warn(player, FORBIDDEN_ENCHANTMENTS_WARN_CREATIVE);

                    return;
                }

                throw new IllegalStateException(FORBIDDEN_ENCHANTMENTS_WARN_CREATIVE);
            }
        } catch (IllegalStateException exception) {
            event.setCancelled(true);

            MessageUtils.severe(player, exception);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAnvilCombineItem(@NotNull AnvilCombineItemsEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;

        @NotNull final Player player = (Player) event.getWhoClicked();

        try {
            @NotNull final ItemStack resultItem = event.getResultItem();

            if(isRestricted(resultItem)) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    MessageUtils.warn(player, FORBIDDEN_ENCHANTMENTS_WARN_CREATIVE);

                    return;
                }

                throw new IllegalStateException(FORBIDDEN_ENCHANTMENTS_WARN_CREATIVE);
            }
        } catch (IllegalStateException exception) {
            event.setCancelled(true);

            player.closeInventory();

            MessageUtils.severe(player, exception);
        }
    }

}
