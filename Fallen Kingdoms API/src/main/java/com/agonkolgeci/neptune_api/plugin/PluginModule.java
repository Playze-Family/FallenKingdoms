package com.agonkolgeci.neptune_api.plugin;

import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@Getter
public abstract class PluginModule<P extends JavaPlugin> {

    @NotNull protected final P instance;

    @NotNull protected final Logger logger;

    @NotNull protected final Server server;
    @NotNull protected final PluginManager pluginManager;
    @NotNull protected final BukkitScheduler bukkitScheduler;

    public PluginModule(@NotNull P instance) {
        this.instance = instance;

        this.logger = instance.getLogger();

        this.server = instance.getServer();
        this.pluginManager = server.getPluginManager();
        this.bukkitScheduler = server.getScheduler();
    }
}
