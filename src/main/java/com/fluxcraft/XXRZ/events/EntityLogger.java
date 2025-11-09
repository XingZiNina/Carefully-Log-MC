package com.fluxcraft.XXRZ.events;

import com.fluxcraft.XXRZ.XXRZ;
import com.fluxcraft.XXRZ.LogManager;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityLogger implements Listener {
    private final LogManager logManager = XXRZ.getInstance().getLogManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {
            logManager.logAsync(LogManager.LogType.ENTITY, killer, killer.getLocation(),
                    "杀死了玩家 " + player.getName());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer != null) {
            logManager.logAsync(LogManager.LogType.ENTITY, killer, killer.getLocation(),
                    "杀死了 " + entity.getType().name());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTNTIgnite(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) entity;
            Entity source = tnt.getSource();

            if (source instanceof Player) {
                Player player = (Player) source;
                logManager.logAsync(LogManager.LogType.TNT, player, player.getLocation(),
                        "点燃了TNT");
            }
        }
    }
}