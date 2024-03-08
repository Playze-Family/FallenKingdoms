package com.agonkolgeci.neptune_api.utils.minecraft.messages;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageUtils {

    public static final Sound SOUND_INFO = Sound.NOTE_PLING;

    public static final Sound SOUND_SEVERE = Sound.VILLAGER_NO;

    public static void log(@NotNull CommandSender sender, @NotNull String message, @NotNull Sound sound, float volume, float pitch) {
        sender.sendMessage(message);

        if(sender instanceof Player) {
            @NotNull final Player player = (Player) sender;
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void success(@NotNull CommandSender sender, @NotNull String message) {
        log(sender, ChatColor.GREEN + message, SOUND_INFO, 1F, 2F);
    }

    public static void warn(@NotNull CommandSender sender, @NotNull String message) {
        log(sender, ChatColor.GRAY + message, SOUND_INFO, 1F, 2F);
    }

    public static void severe(@NotNull CommandSender sender, @NotNull Exception exception) {
        log(sender, ChatColor.RED + exception.getMessage(), SOUND_SEVERE, 1F, 1F);
    }

}