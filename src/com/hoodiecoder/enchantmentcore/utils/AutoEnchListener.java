package com.hoodiecoder.enchantmentcore.utils;

import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
//import org.bukkit.event.inventory.InventoryEvent;
//import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
//import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.hoodiecoder.enchantmentcore.CoreEnch;
import com.hoodiecoder.enchantmentcore.CustomEnch;
import com.hoodiecoder.enchantmentcore.EnchantmentCore;

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
	private void addAllLore(Inventory inv) {
		for (ItemStack i : inv.getContents()) {
			if (i == null) continue;
			ItemMeta meta = i.getItemMeta();
			Map<Enchantment,Integer> enchs;
			if (meta instanceof EnchantmentStorageMeta) {
				enchs = ((EnchantmentStorageMeta)meta).getStoredEnchants();
			} else {
				enchs = meta.getEnchants();
			}
			List<String> currentLore = meta.getLore();
			Map<Enchantment, Integer> parsedLore = EnchantmentUtils.parseLore(currentLore);
			boolean foundEnch = false;
			for (CustomEnch cew : core.getEnchList()) {
				CoreEnch ne = cew.getCoreEnch();
				if (parsedLore == null || (enchs.containsKey(ne.getCraftEnchant()) && !parsedLore.containsKey(ne.getCraftEnchant()))) {
					foundEnch = true;
				}
			}
			if (!foundEnch) continue;
			List<String> createdLore = EnchantmentUtils.createLore(enchs, currentLore);
			if (currentLore!=null) createdLore.addAll(currentLore);
			meta.setLore(createdLore);
			i.setItemMeta(meta);
		}
	}
}
