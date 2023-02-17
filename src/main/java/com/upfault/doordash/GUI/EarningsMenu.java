package com.upfault.doordash.GUI;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;

import static org.bukkit.Bukkit.createInventory;

public class EarningsMenu {
	public static String title = ChatColor.GREEN + "Earnings";
	public static int size = 27;
	public static final Inventory inventory = createInventory(null, size, title);

	public EarningsMenu(Player player) {
		ItemStack pane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.displayName(Component.text(" "));
		pane.setItemMeta(paneMeta);

		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.displayName(Component.text(ChatColor.RED + "Back"));
		backMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to return to the Main Menu")));
		back.setItemMeta(backMeta);
		inventory.setItem(22, back);

		for (int i = 0; i < size; i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, pane);
			}
		}
	}

	private static ItemStack getPlayerHeadItemStack(Player player) {
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwningPlayer(player);
		itemStack.setItemMeta(skullMeta);
		return itemStack;
	}

	public Inventory getInventory() {
		return inventory;
	}
}

