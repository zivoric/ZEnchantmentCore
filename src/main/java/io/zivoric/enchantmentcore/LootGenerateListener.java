package io.zivoric.enchantmentcore;

import io.zivoric.enchantmentcore.utils.EnchantmentInformation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Event listener responsible for generating loot tables for versions 1.15 and above.
 */
public class LootGenerateListener implements Listener {
    private final EnchantmentGenerator generator;

    public LootGenerateListener(EnchantmentGenerator generator) {
        this.generator = generator;
    }

    static void addEnchantToItem(EnchantmentGenerator generator, LootTable table, ItemStack i) {
        ItemMeta iMeta = i.getItemMeta();
        if (!(iMeta instanceof EnchantmentStorageMeta) && iMeta.hasEnchants()) {
            Map<Enchantment, Integer> enchMap = iMeta.getEnchants();
            for (Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
                iMeta.removeEnchant(entry.getKey());
            }
            EnchantmentInformation enchInfo = generator.getLootOffer(i, table, enchMap.size(), true);
            for (Entry<Enchantment, Integer> ench : enchInfo.toMap().entrySet()) {
                iMeta.addEnchant(ench.getKey(), ench.getValue(), false);
            }
            i.setItemMeta(iMeta);
        } else if (iMeta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) iMeta).hasStoredEnchants()) {
            EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) iMeta;

            Map<Enchantment, Integer> enchMap = storageMeta.getStoredEnchants();
            for (Entry<Enchantment, Integer> entry : enchMap.entrySet()) {
                storageMeta.removeStoredEnchant(entry.getKey());
            }
            EnchantmentInformation enchInfo = generator.getLootOffer(i, table, enchMap.size(), true);
            for (Entry<Enchantment, Integer> ench : enchInfo.toMap().entrySet()) {
                storageMeta.addStoredEnchant(ench.getKey(), ench.getValue(), false);
            }
            i.setItemMeta(storageMeta);
        }
    }

    @EventHandler
    public void onGenerate(LootGenerateEvent e) {
        List<ItemStack> loot = e.getLoot();
        for (ItemStack i : loot) {
            addEnchantToItem(generator, e.getLootTable(), i);
        }
    }
}
