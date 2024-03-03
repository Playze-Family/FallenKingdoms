package com.agonkolgeci.fk_loots.loots;

import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
public class LootableItem {

    @NotNull private final ConfigSection configuration;

    private final double probability;
    @NotNull private final ItemStack itemStack;

    public LootableItem(@NotNull ConfigSection configuration) {
        this.configuration = configuration;

        this.probability = configuration.require("probability", "Incorrect Probability");
        this.itemStack = configuration.require("item", "Incorrect Item");
    }


}
