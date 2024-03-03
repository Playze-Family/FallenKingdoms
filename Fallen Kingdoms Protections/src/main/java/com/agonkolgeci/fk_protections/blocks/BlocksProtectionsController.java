package com.agonkolgeci.fk_protections.blocks;

import com.agonkolgeci.fk_protections.FkProtections;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import com.agonkolgeci.neptune_api.utils.minecraft.messages.MessageUtils;
import com.agonkolgeci.neptune_api.utils.objects.ObjectUtils;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BlocksProtectionsController extends PluginModule<FkProtections> implements PluginController<BlocksProtectionsController>, ListenerAdapter {

    @NotNull private final ConfigSection configuration;

    @NotNull private final List<Material> unbreakable;
    @NotNull private final List<Material> indestructible;

    public BlocksProtectionsController(@NotNull FkProtections instance) {
        super(instance);

        this.configuration = instance.getConfigController().of("blocks");

        this.unbreakable = retrieveMaterials(configuration, "unbreakable");
        this.indestructible = retrieveMaterials(configuration, "indestructible");
    }

    private static final String UNBREAKABLE_BLOCK_WARN_CREATIVE = "Attention, ce bloc n'est pas cassable en survie.";
    private static final String UNBREAKABLE_BLOCK_ERROR = "Ce bloc n'est pas cassable.";


    @NotNull
    public List<Material> retrieveMaterials(@NotNull ConfigSection configuration, @NotNull String key) {
        return configuration.get(key, new ArrayList<>()).stream().map(m -> ObjectUtils.fetchObject(Material.class, (String) m, "Unknown Material: '%s'")).collect(Collectors.toList());
    }

    @Override
    public BlocksProtectionsController load() {
        instance.getEventsController().registerEventAdapter(this);

        return this;
    }

    @Override
    public void unload() {
        instance.getEventsController().unregisterEventAdapter(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(@Nonnull BlockBreakEvent event) {
        @NotNull final Player player = event.getPlayer();
        @NotNull final Block block = event.getBlock();

        try {
            if(isUnbreakableBlock(block)) {
                if(player.getGameMode() == GameMode.CREATIVE) {
                    MessageUtils.warn(player, UNBREAKABLE_BLOCK_WARN_CREATIVE);

                    return;
                }

                throw new IllegalStateException(UNBREAKABLE_BLOCK_ERROR);
            }
        } catch (IllegalStateException exception) {
            event.setCancelled(true);

            MessageUtils.severe(player, exception);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
        event.blockList().removeIf(this::isIndestructibleBlock);
    }

    public boolean isUnbreakableBlock(@NotNull Material material) {
        return unbreakable.contains(material);
    }

    public boolean isUnbreakableBlock(@NotNull Block block) {
        return isUnbreakableBlock(block.getType());
    }

    public boolean isIndestructibleBlock(@NotNull Material material) {
        return indestructible.contains(material);
    }

    public boolean isIndestructibleBlock(@NotNull Block block) {
        return isIndestructibleBlock(block.getType());
    }


}
