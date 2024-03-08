package com.agonkolgeci.neptune_api;

import com.agonkolgeci.neptune_api.common.commands.CommandsController;
import com.agonkolgeci.neptune_api.common.events.EventsController;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class FkPlugin extends JavaPlugin {

    public static final String BLANK_FIELD = " \n";

    @Getter protected static FkPlugin instance;

    protected static Logger logger;

    protected static PluginLoader pluginLoader;
    protected static PluginDescriptionFile pluginInfos;

    protected static Server server;

    @Getter protected CommandsController commandsController;
    @Getter protected EventsController eventsController;

    @Override
    public void onEnable() {
        try {
            instance = this;

            logger = getLogger();

            pluginLoader = getPluginLoader();
            pluginInfos = getDescription();

            server = getServer();

            commandsController = new CommandsController(instance).load();
            eventsController = new EventsController(instance).load();

            this.onInstance();
        } catch (Exception exception) {
            this.shutdownPlugin(exception);
        }
    }

    protected abstract void onInstance() throws Exception;

    @Override
    public void onDisable() {
        commandsController.unload();
        eventsController.unload();

        super.onDisable();
    }

    public void shutdownPlugin(@NotNull Exception exception) {
        logger.log(Level.SEVERE, String.format("An critical error occurred while loading %s, you can find the complete stack trace below.", pluginInfos.getName()), exception);
        pluginLoader.disablePlugin(this);
    }

}