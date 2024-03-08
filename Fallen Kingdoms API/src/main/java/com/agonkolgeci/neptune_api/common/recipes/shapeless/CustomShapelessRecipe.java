package com.agonkolgeci.neptune_api.common.recipes.shapeless;

import com.agonkolgeci.neptune_api.common.recipes.CustomRecipe;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomShapelessRecipe extends CustomRecipe<ShapelessRecipe> {

    @Getter @NotNull private final List<Material> ingredients;

    public CustomShapelessRecipe(@NotNull ItemStack result, @NotNull List<Material> ingredients) {
        super(result);

        this.ingredients = ingredients;
    }

    @Override
    @NotNull
    public ShapelessRecipe toRegistrableRecipe() {
        @NotNull final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(result);

        ingredients.forEach(shapelessRecipe::addIngredient);

        return shapelessRecipe;
    }
}
