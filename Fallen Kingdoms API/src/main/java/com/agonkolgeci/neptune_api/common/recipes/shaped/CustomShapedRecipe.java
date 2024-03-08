package com.agonkolgeci.neptune_api.common.recipes.shaped;

import com.agonkolgeci.neptune_api.common.recipes.CustomRecipe;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CustomShapedRecipe extends CustomRecipe<ShapedRecipe> {

    @Getter @NotNull private final String[] shape;
    @Getter @NotNull private final Map<Character, Material> ingredients;

    public CustomShapedRecipe(@NotNull ItemStack result, @NotNull String[] shape, @NotNull Map<Character, Material> ingredients) {
        super(result);

        this.shape = shape;
        this.ingredients = ingredients;
    }

    @Override
    @NotNull
    public ShapedRecipe toRegistrableRecipe() {
        @NotNull final ShapedRecipe shapedRecipe = new ShapedRecipe(result);

        shapedRecipe.shape(shape);
        ingredients.forEach(shapedRecipe::setIngredient);

        return shapedRecipe;
    }
}
