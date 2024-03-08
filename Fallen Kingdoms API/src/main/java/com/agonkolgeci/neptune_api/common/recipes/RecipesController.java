package com.agonkolgeci.neptune_api.common.recipes;

import com.agonkolgeci.neptune_api.plugin.PluginModule;
import lombok.Getter;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RecipesController extends PluginModule<JavaPlugin> {

    @Getter @NotNull private final Map<CustomRecipe<?>, Recipe> registeredRecipes;

    public RecipesController(@NotNull JavaPlugin instance) {
        super(instance);

        this.registeredRecipes = new HashMap<>();
    }

    @NotNull
    public <R extends Recipe> R registerCustomRecipe(@NotNull CustomRecipe<R> customRecipe) {
        @NotNull final R registrableRecipe = customRecipe.toRegistrableRecipe();

        server.addRecipe(registrableRecipe);
        registeredRecipes.put(customRecipe, registrableRecipe);

        return registrableRecipe;
    }
}
