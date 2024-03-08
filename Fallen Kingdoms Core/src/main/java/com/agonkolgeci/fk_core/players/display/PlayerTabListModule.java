package com.agonkolgeci.fk_core.players.display;

import com.agonkolgeci.fk_core.players.FkPlayerCache;
import com.agonkolgeci.fk_core.players.FkPlayersController;
import com.agonkolgeci.neptune_api.FkAPI;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.player.PlayerCacheModule;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerTabListModule extends PlayerCacheModule<FkPlayersController, FkPlayerCache> {

    @NotNull private final ConfigSection configuration;

    @NotNull private final String header;
    @NotNull private final String footer;

    public PlayerTabListModule(@NotNull FkPlayersController module, @NotNull FkPlayerCache playerCache) {
        super(module, playerCache);

        this.configuration = module.getConfiguration().of("tab_list");

        this.header = configuration.get("header", "");
        this.footer = configuration.get("footer", "");
    }

    public void showTab(@NotNull Player player) {
        @NotNull final PacketContainer headerFooterPacket = new PacketContainer(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        headerFooterPacket.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', header)));
        headerFooterPacket.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', footer)));

        FkAPI.getProtocolManager().sendServerPacket(player, headerFooterPacket);
    }

}
