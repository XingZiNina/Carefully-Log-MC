package com.fluxcraft.XXRZ.events;

import com.fluxcraft.XXRZ.XXRZ;
import com.fluxcraft.XXRZ.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TPSMonitor extends BukkitRunnable {
    private LogManager logManager;
    private boolean alreadyLogged = false;

    @Override
    public void run() {
        if (logManager == null) {
            logManager = XXRZ.getInstance().getLogManager();
            if (logManager == null) {
                return;
            }
        }

        double currentTPS = Bukkit.getTPS()[0];

        if (currentTPS <= 10.0) {
            if (!alreadyLogged) {
                logManager.logAsync(LogManager.LogType.TPS,
                        "服务器TPS下降至: " + String.format("%.2f", currentTPS));
                alreadyLogged = true;
            }
        } else {
            alreadyLogged = false;
        }
    }
}