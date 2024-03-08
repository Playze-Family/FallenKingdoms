package com.agonkolgeci.neptune_api.common.events;

import com.agonkolgeci.neptune_api.common.events.triggers.inventory.TriggerAnvilCombineItem;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class EventsController extends PluginModule<JavaPlugin> implements PluginController<EventsController> {

    @Getter @NotNull private final List<ListenerAdapter> registeredListeners;

    public EventsController(@NotNull JavaPlugin instance) {
        super(instance);

        this.registeredListeners = new ArrayList<>();
    }

    @Override
    public EventsController load() {
        registerEventAdapter(new TriggerAnvilCombineItem(this));

        return this;
    }

    @Override
    public void unload() {
        registeredListeners.forEach(this::unregisterEventAdapter);
    }

    @NotNull
    public <L extends Listener> L registerEventAdapter(@NotNull ListenerAdapter listener) {
        pluginManager.registerEvents(listener, instance);
        registeredListeners.add(listener);

        return (L) listener;
    }

    public void unregisterEventAdapter(@NotNull ListenerAdapter listener) {
        HandlerList.unregisterAll(listener);
    }

    @NotNull
    public <E extends Event> E callEvent(@NotNull E event) {
        pluginManager.callEvent(event);
        
        return event;
    }

}
