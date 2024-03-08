package com.agonkolgeci.neptune_api.common.player;

import com.agonkolgeci.neptune_api.plugin.PluginAddon;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class PlayerCacheModule<C extends PlayersController<?, C, P>, P extends PlayerCache<C, P>> extends PluginAddon<C> {

    @NotNull protected final P playerCache;

    public PlayerCacheModule(@NotNull C module, @NotNull P playerCache) {
        super(module);

        this.playerCache = playerCache;
    }
}
