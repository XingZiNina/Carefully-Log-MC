package com.fluxcraft.XXRZ;

import com.fluxcraft.XXRZ.events.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class XXRZ extends JavaPlugin {
    private static XXRZ instance;
    private LogManager logManager;
    private TPSMonitor tpsMonitor;

    @Override
    public void onEnable() {
        instance = this;

        // 先初始化日志管理器
        this.logManager = new LogManager();

        // 检查Geyser和Floodgate
        if (Bukkit.getPluginManager().getPlugin("Geyser-Spigot") != null &&
                Bukkit.getPluginManager().getPlugin("floodgate") != null) {
            getLogger().info("Geyser和Floodgate检测到，已启用基岩版玩家支持");
        }

        // 注册事件监听器
        registerEvents();

        // 启动TPS监控
        this.tpsMonitor = new TPSMonitor();
        startTPSMonitor();

        getLogger().info("XXRZ日志记录插件已启用!");
    }

    @Override
    public void onDisable() {
        if (tpsMonitor != null) {
            tpsMonitor.cancel();
        }

        if (logManager != null) {
            logManager.shutdown();
        }

        getLogger().info("XXRZ日志记录插件已禁用!");
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new BlockLogger(), this);
        pm.registerEvents(new ContainerLogger(), this);
        pm.registerEvents(new CommandLogger(), this);
        pm.registerEvents(new ChatLogger(), this);
        pm.registerEvents(new ItemLogger(), this);
        pm.registerEvents(new JoinQuitLogger(), this);
        pm.registerEvents(new EntityLogger(), this);
        pm.registerEvents(new WorldSwitchLogger(), this); // 新增：注册世界切换监听器
    }

    private void startTPSMonitor() {
        tpsMonitor.runTaskTimer(this, 100L, 100L); // 每5秒检查一次TPS
    }

    public static XXRZ getInstance() {
        return instance;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}