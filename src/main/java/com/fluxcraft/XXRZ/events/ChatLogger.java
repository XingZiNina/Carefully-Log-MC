package com.fluxcraft.XXRZ.events;

import com.fluxcraft.XXRZ.XXRZ;
import com.fluxcraft.XXRZ.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatLogger implements Listener {
    private final LogManager logManager = XXRZ.getInstance().getLogManager();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        logManager.logAsync(LogManager.LogType.CHAT, player, player.getLocation(),
                "说: " + message);
    }
}