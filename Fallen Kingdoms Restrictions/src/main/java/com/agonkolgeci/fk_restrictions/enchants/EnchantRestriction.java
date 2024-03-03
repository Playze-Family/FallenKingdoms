package com.agonkolgeci.fk_restrictions.enchants;

import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class EnchantRestriction {

    @NotNull private final Enchantment enchantment;
    @NotNull private final ConfigSection configuration;

    private final int levelMax;

    public EnchantRestriction(@NotNull Enchantment enchantment, @NotNull ConfigSection configuration) {
        this.enchantment = enchantment;
        this.configuration = configuration;

        this.levelMax = configuration.get("level_max", Integer.MAX_VALUE);
    }

    public boolean isRestricted(@NotNull ItemStack itemStack) {
        return itemStack.getEnchantments().getOrDefault(enchantment, 0) > levelMax;
    }

}
