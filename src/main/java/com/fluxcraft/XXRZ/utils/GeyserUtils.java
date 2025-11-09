package com.fluxcraft.XXRZ.utils;

import org.bukkit.entity.Player;

public class GeyserUtils {
    public static boolean isGeyserPlayer(Player player) {
        try {
            Class<?> floodgateApi = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Object instance = floodgateApi.getMethod("getInstance").invoke(null);
            return (boolean) floodgateApi.getMethod("isFloodgatePlayer", java.util.UUID.class)
                    .invoke(instance, player.getUniqueId());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getGeyserPlayerName(Player player) {
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
}