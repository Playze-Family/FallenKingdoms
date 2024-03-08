package com.agonkolgeci.neptune_api.common.player;

import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import com.agonkolgeci.neptune_api.common.events.EventsController;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@SuppressWarnings("unchecked")
public abstract class PlayersController<I extends JavaPlugin, C extends PlayersController<I, C, P>, P extends PlayerCache<C, P>> extends PluginModule<I> implements PluginController<PlayersController<I, C, P>>, ListenerAdapter {

    @NotNull protected final EventsController eventsController;

    @NotNull protected final Map<UUID, P> players;

    public PlayersController(@NotNull I instance, @NotNull EventsController eventsController) {
        super(instance);

        this.eventsController = eventsController;

        this.players = new HashMap<>();
    }

    @Override
    public C load() {
        eventsController.registerEventAdapter(this);

        this.loadPlayers();

        return (C) this;
    }

    @Override
    public void unload() {
        this.unloadPlayers();
    }

    protected void loadPlayers() {
        server.getOnlinePlayers().forEach(player -> retrievePlayerCache(player).onLogin(player));
    }

    protected void unloadPlayers() {
        server.getOnlinePlayers().forEach(player -> retrievePlayerCache(player).onLogout(player));
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        @NotNull final Player player = event.getPlayer();
        @NotNull final P playerCache = retrievePlayerCache(player);

        playerCache.onLogin(player);
    }

    @EventHandler
    public void onPlayerLogout(@NotNull PlayerQuitEvent event) {
        @NotNull final Player player = event.getPlayer();
        @NotNull final P playerCache = retrievePlayerCache(player);

        playerCache.onLogout(player);
    }

    @NotNull
    protected abstract P retrievePlayerCache(@NotNull UUID uuid);

    @NotNull
    protected P retrievePlayerCache(@NotNull OfflinePlayer offlinePlayer) {
        return retrievePlayerCache(offlinePlayer.getUniqueId());
    }


}
