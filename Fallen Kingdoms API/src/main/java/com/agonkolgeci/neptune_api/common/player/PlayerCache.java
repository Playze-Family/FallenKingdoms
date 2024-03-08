package com.agonkolgeci.neptune_api.common.player;

import com.agonkolgeci.neptune_api.plugin.PluginAddon;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
public abstract class PlayerCache<C extends PlayersController<?, C, P>, P extends PlayerCache<C, P>> extends PluginAddon<C> {

    @NotNull protected final UUID uniqueId;

    @Nullable protected Player player;

    public PlayerCache(@NotNull C module, @NotNull UUID uniqueId) {
        super(module);

        this.uniqueId = uniqueId;
    }

    protected void onLogin(@NotNull Player player) {
        this.player = player;
    }

    protected void onLogout(@NotNull Player player) {
        this.player = null;
    }

    public boolean isOnline() {
        return player != null && player.isOnline();
    }

}
