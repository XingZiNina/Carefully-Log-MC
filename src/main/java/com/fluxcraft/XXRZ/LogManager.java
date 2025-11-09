package com.fluxcraft.XXRZ;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogManager {
    private final File logFolder;
    private final ExecutorService executor;

    // 日志类型枚举
    public enum LogType {
        BLOCK("block"),
        CONTAINER("container"),
        COMMAND("command"),
        CHAT("chat"),
        ITEM("item"),
        JOIN_QUIT("join_quit"),
        ENTITY("entity"),
        TNT("tnt"),
        TPS("tps");

        private final String folderName;

        LogType(String folderName) {
            this.folderName = folderName;
        }

        public String getFolderName() {
            return folderName;
        }
    }

    public LogManager() {
        // 使用Bukkit的getDataFolder()获取插件数据文件夹
        this.logFolder = new File(Bukkit.getServer().getPluginsFolder(), "XXRZ");

        // 确保主文件夹存在
        if (!logFolder.exists()) {
            boolean created = logFolder.mkdirs();
            if (created) {
                Bukkit.getLogger().info("XXRZ主文件夹已创建: " + logFolder.getAbsolutePath());
            } else {
                Bukkit.getLogger().warning("无法创建XXRZ主文件夹: " + logFolder.getAbsolutePath());
            }
        }

        // 创建所有子文件夹
        for (LogType type : LogType.values()) {
            File subFolder = new File(logFolder, type.getFolderName());
            if (!subFolder.exists()) {
                boolean created = subFolder.mkdirs();
                if (created) {
                    Bukkit.getLogger().info("XXRZ子文件夹已创建: " + subFolder.getAbsolutePath());
                } else {
                    Bukkit.getLogger().warning("无法创建XXRZ子文件夹: " + subFolder.getAbsolutePath());
                }
            }
        }

        // 创建线程池处理异步日志写入
        this.executor = Executors.newFixedThreadPool(2);
    }

    public void logAsync(LogType type, Player player, Location location, String action) {
        executor.execute(() -> log(type, player, location, action));
    }

    public void logAsync(LogType type, String message) {
        executor.execute(() -> log(type, message));
    }

    private void log(LogType type, Player player, Location location, String action) {
        String worldName = location.getWorld() != null ? location.getWorld().getName() : "unknown";
        String playerName = player.getName();
        String geyserPrefix = "";

        // 检查是否为Geyser玩家
        if (isGeyserPlayer(player)) {
            geyserPrefix = "[Geyser] ";
            playerName = getGeyserPlayerName(player);
        }

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        String logMessage = String.format("%s %s%s %d %d %d %s",
                worldName, geyserPrefix, playerName, x, y, z, action);

        log(type, logMessage);
    }

    private void log(LogType type, String message) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        File logFile = new File(logFolder + File.separator + type.getFolderName(), date + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write("[" + time + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            Bukkit.getLogger().warning("写入日志文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isGeyserPlayer(Player player) {
        try {
            Class<?> floodgateApi = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object instance = floodgateApi.getMethod("getInstance").invoke(null);
            return (boolean) floodgateApi.getMethod("isFloodgatePlayer", java.util.UUID.class)
                    .invoke(instance, player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }

    private String getGeyserPlayerName(Player player) {
        try {
            Class<?> floodgateApi = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object instance = floodgateApi.getMethod("getInstance").invoke(null);
            Object floodgatePlayer = floodgateApi.getMethod("getPlayer", java.util.UUID.class)
                    .invoke(instance, player.getUniqueId());

            return (String) floodgatePlayer.getClass().getMethod("getUsername").invoke(floodgatePlayer);
        } catch (Exception e) {
            return player.getName() + " [Bedrock]";
        }
    }

    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}