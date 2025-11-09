package com.fluxcraft.XXRZ.events;

import com.fluxcraft.XXRZ.XXRZ;
import com.fluxcraft.XXRZ.LogManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ContainerLogger implements Listener {
    private final LogManager logManager = XXRZ.getInstance().getLogManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        InventoryType type = inventory.getType();

        // 只记录特定容器
        if (type != InventoryType.CHEST &&
                type != InventoryType.BARREL &&
                type != InventoryType.SHULKER_BOX &&
                type != InventoryType.ENDER_CHEST) {
            return;
        }

        Location location = null;
        if (inventory.getLocation() != null) {
            location = inventory.getLocation();
        } else if (type == InventoryType.ENDER_CHEST) {
            location = player.getLocation(); // 末影箱没有具体位置，使用玩家位置
        }

        if (location == null) return;

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        String action = "";
        if (event.getRawSlot() < inventory.getSize()) {
            // 点击在容器内
            if (currentItem != null && !currentItem.getType().isAir()) {
                action = "从容器中拿走了 " + currentItem.getType().name() + " x" + currentItem.getAmount();
            } else if (cursorItem != null && !cursorItem.getType().isAir()) {
                action = "向容器放置了 " + cursorItem.getType().name() + " x" + cursorItem.getAmount();
            }
        }

        if (!action.isEmpty()) {
            logManager.logAsync(LogManager.LogType.CONTAINER, player, location, action);
        }
    }
}