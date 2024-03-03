package com.agonkolgeci.fk_restrictions.items;

import com.agonkolgeci.fk_restrictions.enchants.EnchantRestriction;
import com.agonkolgeci.fk_restrictions.enchants.EnchantsRestrictionsController;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public class ItemRestriction {

    @NotNull private final ConfigSection configuration;

    @NotNull private final ItemStack restrictedItemStack;

    @NotNull private final Map<Enchantment, EnchantRestriction> enchantmentsRestriction;

    public ItemRestriction(@NotNull ConfigSection configuration) {
        this.configuration = configuration;

        this.restrictedItemStack = configuration.require("item", "Incorrect Item");

        this.enchantmentsRestriction = EnchantsRestrictionsController.retrieveRestrictions(configuration.of("enchantments"));
    }

    public boolean isRestricted(@NotNull ItemStack itemStack) {
        if(restrictedItemStack.getType() != itemStack.getType()) return false;

        if(!enchantmentsRestriction.isEmpty()) {
            return enchantmentsRestriction.values().stream().anyMatch(restriction -> restriction.isRestricted(itemStack));
        }

        return restrictedItemStack.isSimilar(itemStack);
    }

}
