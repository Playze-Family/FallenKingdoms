package com.agonkolgeci.fk_core.players.death;

import com.agonkolgeci.fk_core.players.FkPlayerCache;
import com.agonkolgeci.fk_core.players.FkPlayersController;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.common.player.PlayerCacheModule;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerRespawnModule extends PlayerCacheModule<FkPlayersController, FkPlayerCache> implements ListenerAdapter {

    @NotNull private final ConfigSection configuration;

    private final boolean autoRespawn;
    private final int protectionTime;

    public PlayerRespawnModule(@NotNull FkPlayersController module, @NotNull FkPlayerCache playerCache) {
        super(module, playerCache);

        this.configuration = module.getConfiguration().of("respawn");

        this.autoRespawn = configuration.get("auto_respawn", false);
        this.protectionTime = configuration.get("protection_time", 0);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(@NotNull PlayerRespawnEvent event) {
        @NotNull final Player player = event.getPlayer();
        if(playerCache.getPlayer() != player) return;

        final int ticks = ((int) ObjectUtils.retrieveTicks(protectionTime)) + player.getMaximumNoDamageTicks();

        event.getPlayer().setNoDamageTicks(ticks);
    }

}
