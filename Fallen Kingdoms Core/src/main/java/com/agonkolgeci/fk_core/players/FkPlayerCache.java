package com.agonkolgeci.fk_core.players;

import com.agonkolgeci.fk_core.players.display.PlayerTabListModule;
import com.agonkolgeci.fk_core.players.death.PlayerRespawnModule;
import com.agonkolgeci.neptune_api.common.player.PlayerCache;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class FkPlayerCache extends PlayerCache<FkPlayersController, FkPlayerCache> {

    @NotNull private final PlayerTabListModule tabListController;

    @NotNull private final PlayerRespawnModule playerRespawnModule;

    public FkPlayerCache(@NotNull FkPlayersController module, @NotNull UUID uniqueId) {
        super(module, uniqueId);

        this.tabListController = new PlayerTabListModule(module, this);

        this.playerRespawnModule = new PlayerRespawnModule(module, this);
    }

    @Override
    protected void onLogin(@NotNull Player player) {
        super.onLogin(player);

        tabListController.showTab(player);

        module.getEventsController().registerEventAdapter(playerRespawnModule);
    }

    @Override
    protected void onLogout(@NotNull Player player) {
        super.onLogout(player);

        module.getEventsController().unregisterEventAdapter(playerRespawnModule);
    }
}
