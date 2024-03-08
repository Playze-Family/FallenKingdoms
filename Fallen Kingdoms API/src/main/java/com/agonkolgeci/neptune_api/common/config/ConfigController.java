package com.agonkolgeci.neptune_api.common.config;

import com.agonkolgeci.neptune_api.plugin.PluginModule;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigController extends PluginModule<JavaPlugin> {

    @Getter @NotNull private final FileConfiguration configuration;

    public ConfigController(@NotNull JavaPlugin instance) {
        super(instance);

        this.instance.saveDefaultConfig();
        this.configuration = instance.getConfig();
    }

    public void saveChanges() {
        instance.saveConfig();
    }

    public ConfigSection of() {
        return new ConfigSection(this, configuration);
    }

    @NotNull
    public ConfigSection of(@NotNull String... path) {
        return new ConfigSection(this, requireConfigurationSection(configuration, path));
    }

    @NotNull
    protected String translatePath(@NotNull String ... path) {
        return String.join(".", path);
    }

    @Nullable
    public ConfigurationSection retrieveConfigurationSection(@NotNull ConfigurationSection parent, @NotNull String... path) {
        return parent.getConfigurationSection(translatePath(path));
    }

    @NotNull
    public ConfigurationSection requireConfigurationSection(@NotNull ConfigurationSection parent, @NotNull String... path) {
        return ObjectUtils.requireNonNullElseGet(retrieveConfigurationSection(parent, translatePath(path)), () -> parent.createSection(translatePath(path)));
    }

}
