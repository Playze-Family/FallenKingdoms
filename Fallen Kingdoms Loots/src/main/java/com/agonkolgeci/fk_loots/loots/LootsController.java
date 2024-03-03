package com.agonkolgeci.fk_loots.loots;

import com.agonkolgeci.fk_loots.FkLoots;
import com.agonkolgeci.neptune_api.common.config.ConfigSection;
import com.agonkolgeci.neptune_api.common.events.ListenerAdapter;
import com.agonkolgeci.neptune_api.plugin.PluginController;
import com.agonkolgeci.neptune_api.plugin.PluginModule;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class LootsController extends PluginModule<FkLoots> implements PluginController<LootsController>, ListenerAdapter {

    @NotNull private final ConfigSection configuration;

    @NotNull private final List<LootableBlock> blocks;

    public LootsController(@NotNull FkLoots instance) {
        super(instance);

        this.configuration = instance.getConfigController().of("blocks");

        this.blocks = retrieveBlocks(configuration);
    }

    @NotNull
    public static List<LootableBlock> retrieveBlocks(@NotNull ConfigSection configuration) {
        return configuration.keys(LootableBlock::new);
    }

    @Override
    public LootsController load() {
        instance.getEventsController().registerEventAdapter(this);

        return this;
    }

    @Override
    public void unload() {
        instance.getEventsController().unregisterEventAdapter(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        @NotNull final Player player = event.getPlayer();
        if(player.getGameMode() != GameMode.SURVIVAL) return;

        @NotNull final Block block = event.getBlock();
        @NotNull final Material material = block.getType();

        @NotNull final List<LootableBlock> lootableBlocks = blocks.stream().filter(lb -> lb.getType() == material).collect(Collectors.toList());
        for(@NotNull LootableBlock lootableBlock : lootableBlocks) {
            final double randomValue1 = Math.random();

            if(randomValue1 < lootableBlock.getProbability()) {
                final double totalProbability = lootableBlock.getItems().stream().mapToDouble(LootableItem::getProbability).sum();
                final double randomValue2 = Math.random() * totalProbability;
                double cumulativeProbability = 0;

                for(@NotNull LootableItem lootableItem : lootableBlock.getItems()) {
                    cumulativeProbability += lootableItem.getProbability();

                    if(randomValue2 < cumulativeProbability) {
                        block.getWorld().dropItemNaturally(block.getLocation().clone().add(.5, .5, .5), lootableItem.getItemStack().clone());

                        break;
                    }
                }
            }
        }
    }

}
