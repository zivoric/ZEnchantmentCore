package io.zivoric.enchantmentcore.enchant;

import io.zivoric.enchantmentcore.CustomEnch;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An extension to {@link CustomEnch} to deal with block event
 */
public interface BlockHandler {
    /**
     * <p>Event method automatically called when a player using this enchantment places a block.</p>
     *
     * @param player The player involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the player is involved with
     */
    default void onPlaceBlock(Player player, List<Integer> levels, List<ItemStack> items, BlockPlaceEvent event) {
        // EMPTY
    }

    /**
     * <p>Event method automatically called when a player using this enchantment breaks a block.</p>
     *
     * @param player The player involved
     * @param levels The levels of each item currently in use with this enchantment
     * @param items  Each item currently in use with this enchantment
     * @param event  The event that the player is involved with
     */
    default void onBreakBlock(Player player, List<Integer> levels, List<ItemStack> items, BlockBreakEvent event) {
        // EMPTY
    }
}
