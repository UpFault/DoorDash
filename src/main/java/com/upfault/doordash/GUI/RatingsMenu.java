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

public class RatingsMenu {
	public static String title = ChatColor.GOLD + "Ratings";
	public static int size = 27;
	public static final Inventory inventory = createInventory(null, size, title);

	public RatingsMenu(Player player) {
		ItemStack pane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.displayName(Component.text(" "));
		pane.setItemMeta(paneMeta);

		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.displayName(Component.text(ChatColor.RED + "Back"));
		backMeta.lore(Arrays.asList(Component.text(ChatColor.GRAY + "Click to return to the Main Menu")));
		back.setItemMeta(backMeta);
		inventory.setItem(22, back);

		for (int i = 0; i < size; i++) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, pane);
			}
		}
	}

	public Inventory getInventory() {
		return inventory;
	}
}

