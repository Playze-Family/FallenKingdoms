package com.agonkolgeci.neptune_api.plugin;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class PluginAddon<M extends PluginModule<?>> {

    @NotNull protected final M module;

    public PluginAddon(@NotNull M module) {
        this.module = module;
    }
}
