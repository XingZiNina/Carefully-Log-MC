package com.fluxcraft.XXRZ.events;

import com.fluxcraft.XXRZ.XXRZ;
import com.fluxcraft.XXRZ.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandLogger implements Listener {
    private final LogManager logManager = XXRZ.getInstance().getLogManager();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();

        logManager.logAsync(LogManager.LogType.COMMAND, player, player.getLocation(),
                "执行了指令: " + command);
    }
}