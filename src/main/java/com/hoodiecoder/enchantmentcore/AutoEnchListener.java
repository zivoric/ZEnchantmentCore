package com.hoodiecoder.enchantmentcore;

import com.hoodiecoder.enchantmentcore.utils.EnchantmentUtils;
import com.hoodiecoder.enchantmentcore.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

/**
 * Event listener responsible for automatically applying enchantment lore.
 */
public class AutoEnchListener implements Listener {
    private final EnchantmentCore core;

    public AutoEnchListener(EnchantmentCore c) {
        core = c;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getWhoClicked().getInventory();
        addAllLore(inv);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        Inventory playerInventory = player.getInventory();
        Inventory eventInventory = e.getInventory();
        addAllLore(playerInventory);
        addAllLore(eventInventory);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        addAllLore(e.getInventory());
    }

    private void addAllLore(Inventory inv) {
        addLoreLoop(inv.getContents());
        if (VersionUtils.SERVER_VERSION >= 14 && inv.getType() == InventoryType.GRINDSTONE) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(core, () -> {
                GrindstoneInventory gInv = (GrindstoneInventory) inv;
                ItemStack[] grindstoneItems = new ItemStack[]{gInv.getItem(2)};
                addLoreLoop(grindstoneItems);
                gInv.setItem(2, grindstoneItems[0]);
            }); // schedule needed or Minecraft will override the set item
        }
    }

    private void addLoreLoop(ItemStack[] items) {
        for (ItemStack i : items) {
            if (i == null) continue;
            ItemMeta meta = i.getItemMeta();
            Map<Enchantment, Integer> enchs;
            if (meta instanceof EnchantmentStorageMeta) {
                enchs = ((EnchantmentStorageMeta) meta).getStoredEnchants();
            } else if (meta != null) {
                enchs = meta.getEnchants();
            } else {
                return;
            }
            List<String> currentLore = meta.getLore();
            List<String> createdLore = EnchantmentUtils.createLore(enchs, currentLore);
            if (!createdLore.equals(currentLore)) {
                meta.setLore(createdLore);
                i.setItemMeta(meta);
            }
        }
    }
}
