package com.agonkolgeci.fk_core.players;

import com.agonkolgeci.fk_core.FkCore;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.events.EventsController;
import com.agonkolgeci.neptune_api.common.player.PlayersController;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class FkPlayersController extends PlayersController<FkCore, FkPlayersController, FkPlayerCache> {

    @NotNull private final ConfigSection configuration;

    public FkPlayersController(@NotNull FkCore instance, @NotNull EventsController eventsController) {
        super(instance, eventsController);

        this.configuration = instance.getConfigController().of("players");
    }

    @Override
    protected @NotNull FkPlayerCache retrievePlayerCache(@NotNull UUID uuid) {
        return ObjectUtils.retrieveObjectOrElseGet(players, uuid, () -> new FkPlayerCache(this, uuid));
    }
}
