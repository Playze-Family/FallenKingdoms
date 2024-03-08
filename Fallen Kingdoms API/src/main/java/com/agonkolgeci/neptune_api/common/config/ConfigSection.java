package com.agonkolgeci.neptune_api.common.config;

import com.agonkolgeci.neptune_api.common.config.callback.any.ConfigurationObjectCallback;
import com.agonkolgeci.neptune_api.common.config.callback.type.ConfigurationTypeCallback;
import com.agonkolgeci.neptune_api.common.config.callback.type.ConfigurationTypeObjectCallback;
import com.agonkolgeci.neptune_api.plugin.PluginAddon;
import com.agonkolgeci.neptune_api.common.database.DatabaseCredentials;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

@Getter
@SuppressWarnings("unchecked")
public class ConfigSection extends PluginAddon<ConfigController> {

    @NotNull private final ConfigurationSection configuration;
    @Nullable private final ConfigSection parent;

    public ConfigSection(@NotNull ConfigController controller, @NotNull ConfigurationSection configuration) {
        super(controller);

        this.configuration = configuration;
        this.parent = (configuration.getParent() != null ? new ConfigSection(controller, configuration.getParent()) : null);
    }

    @NotNull
    public String getName() {
        return configuration.getName();
    }

    @NotNull
    public String getPath() {
        return configuration.getCurrentPath();
    }

    @NotNull
    public ConfigSection of(@NotNull String... path) {
        return new ConfigSection(module, module.requireConfigurationSection(configuration, path));
    }

    @NotNull
    public Set<String> keys(boolean deep) {
        return configuration.getKeys(deep);
    }

    @NotNull
    public <T, O> Map<T, O> keys(@NotNull ConfigurationTypeCallback<T> typeCallback, @NotNull ConfigurationTypeObjectCallback<T, O> objectCallback) {
        @NotNull final Map<T, O> objects = new HashMap<>();

        for(@NotNull final String key : keys(false)) {
            @NotNull final ConfigSection configuration = of(key);

            @NotNull final T type = typeCallback.retrieveType(configuration);
            if(objects.containsKey(type)) throw new IllegalStateException("Duplicated type key in keys !");

            @NotNull final O object = objectCallback.retrieveObject(type, configuration);

            objects.put(type, object);
        }

        return objects;
    }

    @NotNull
    public <O> List<O> keys(@NotNull ConfigurationObjectCallback<O> objectCallback) {
        @NotNull final List<O> objects = new ArrayList<>();

        for(@NotNull final String key : keys(false)) {
            @NotNull final ConfigSection configuration = of(key);
            @NotNull final O object = objectCallback.retrieveObject(configuration);

            objects.add(object);
        }

        return objects;
    }

    public boolean has(@NotNull String key) {
        return get(key) != null;
    }

    @NotNull
    public <T> T set(@NotNull String key, @NotNull T object) {
        configuration.set(key, object);
        module.saveChanges();

        return object;
    }

    @NotNull
    public <T> T update(@NotNull T object) {
        ObjectUtils.requireNonNullElse(configuration.getRoot(), configuration).set(configuration.getName(), object);
        module.saveChanges();

        return object;
    }

    @Nullable
    public <T> T get(@NotNull String key) {
        return (T) configuration.get(key);
    }

    @NotNull
    public <T> T get(@NotNull String key, @NotNull T defaultValue) {
        return ObjectUtils.requireNonNullElse(get(key), defaultValue);
    }

    @NotNull
    public <T> T get(@NotNull String key, @NotNull Supplier<T> defaultValue) {
        return ObjectUtils.requireNonNullElseGet(get(key), defaultValue);
    }

    @NotNull
    public <T> T require(@NotNull String key) {
        return Objects.requireNonNull(get(key), String.format("An unknown error occurred while retrieving the '%s' key associated with the '%s' parent in the configuration.", key, configuration.getParent()));
    }

    @NotNull
    public <T> T require(@NotNull String key, @NotNull String message) {
        return Objects.requireNonNull(get(key), String.format("An unknown error occurred while retrieving the '%s' key associated with the '%s' parent in the configuration: '%s'", key, configuration.getParent(), message));
    }

    @Nullable
    public DatabaseCredentials toDatabaseCredentials() {
        @Nullable final String host = this.get("host");
        @Nullable final String username = this.get("username");
        @Nullable final String password = this.get("password");
        @Nullable final String name = this.get("name");
        if(host == null || username == null || password == null || name == null) return null;

        final int maxPoolSize = this.get("maxPoolSize", 10);
        final int port = this.get("port", 3306);

        return new DatabaseCredentials(host, username, password, name, maxPoolSize, port);
    }

}
