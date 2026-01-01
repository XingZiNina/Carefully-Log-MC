package com.fluxcraft.XXRZ.events;

import com.fluxcraft.XXRZ.XXRZ;
import com.fluxcraft.XXRZ.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldSwitchLogger implements Listener {
    private final LogManager logManager = XXRZ.getInstance().getLogManager();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        // 获取来源世界和目标世界名称
        String fromWorld = event.getFrom().getName();
        String toWorld = player.getWorld().getName();

        // 记录日志：格式为 "从 [旧世界] 切换到了 [新世界]"
        logManager.logAsync(LogManager.LogType.WORLD_SWITCH, player, player.getLocation(),
                "从 " + fromWorld + " 切换到了 " + toWorld);
    }
}
