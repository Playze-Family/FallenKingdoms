package com.agonkolgeci.fk_loots.loots;

import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class LootableBlock {

    @NotNull private final ConfigSection configuration;

    @NotNull private final Material type;
    private final double probability;

    @NotNull private final List<LootableItem> items;

    public LootableBlock(@NotNull ConfigSection configuration) {
        this.configuration = configuration;

        this.type = ObjectUtils.fetchObject(Material.class, configuration.require("type"), "Incorrect Material");
        this.probability = configuration.require("probability", "Incorrect Probability");

        this.items = retrieveItems(configuration.of("items"));
    }

    @NotNull
    public static List<LootableItem> retrieveItems(@NotNull ConfigSection configuration) {
        return configuration.keys(LootableItem::new);
    }

}
