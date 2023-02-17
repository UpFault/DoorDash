package com.upfault.doordash.GUI;

import com.upfault.doordash.DoorDash;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.Collections;

import static org.bukkit.Bukkit.createInventory;

public class AccountInformationMenu {
	public static String title = ChatColor.YELLOW + "Your Account: ";
	public static int size = 27;
	public static Inventory inventory = createInventory(null, size, title);;

	public AccountInformationMenu(Player player) {

		ItemStack pane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.displayName(Component.text(" "));
		pane.setItemMeta(paneMeta);

		ItemStack name = new ItemStack(Material.NAME_TAG);
		ItemMeta nameMeta = name.getItemMeta();
		nameMeta.displayName(Component.text(ChatColor.GREEN + "Username"));
		nameMeta.lore(Arrays.asList(Component.text(ChatColor.GRAY + "Current Username: " + ChatColor.YELLOW + getNameFromDatabase(player)),
				Component.text(" "), Component.text(ChatColor.GRAY + "Click to edit your dasher name")));
		name.setItemMeta(nameMeta);
		inventory.setItem(10, name);

		ItemStack settings = new ItemStack(Material.REDSTONE);
		ItemMeta settingsMeta = settings.getItemMeta();
		settingsMeta.displayName(Component.text(ChatColor.RED + "Settings"));
		settings.setItemMeta(settingsMeta);
		inventory.setItem(12, settings);

		ItemStack soon = new ItemStack(Material.RED_WOOL);
		ItemMeta soonMeta = soon.getItemMeta();
		soonMeta.displayName(Component.text(ChatColor.RED + "Coming Soon..."));
		soon.setItemMeta(soonMeta);
		inventory.setItem(14, soon);

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

	public Inventory getInventory() {
		return inventory;
	}

	private static String getNameFromDatabase(Player player) {

		try {
			PreparedStatement statement = DoorDash.getConnection().prepareStatement("SELECT username FROM playerData WHERE uuid = ?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return result.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

