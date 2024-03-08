package com.agonkolgeci.neptune_api.common.recipes;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

public abstract class CustomRecipe<T extends Recipe> {

    @Getter @NotNull protected final ItemStack result;

    public CustomRecipe(@NotNull ItemStack result) {
        this.result = result;
    }

    @NotNull
    public abstract T toRegistrableRecipe();

}
