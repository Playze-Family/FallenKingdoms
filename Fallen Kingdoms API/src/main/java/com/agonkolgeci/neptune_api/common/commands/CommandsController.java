package com.agonkolgeci.neptune_api.common.commands;

import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public class CommandsController extends PluginModule<JavaPlugin> implements PluginController<CommandsController> {

    public CommandsController(@NotNull JavaPlugin instance) {
        super(instance);
    }

    @Override
    public CommandsController load() {
        return this;
    }

    @Override
    public void unload() {

    }

    @NotNull
    public <C extends CommandAdapter> C registerCommandAdapter(@NotNull String commandName, @NotNull CommandAdapter commandAdapter) {
        @Nullable final PluginCommand pluginCommand = instance.getCommand(commandName);
        if(pluginCommand == null) throw new IllegalStateException(String.format("Cannot retrieve command: %s", commandName));

        pluginCommand.setExecutor(commandAdapter);
        pluginCommand.setTabCompleter(commandAdapter);

        return (C) commandAdapter;
    }

}
