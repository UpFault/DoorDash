package com.upfault.doordash.GUI;

import com.upfault.doordash.DoorDash;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.bukkit.Bukkit.createInventory;

public class SettingsMenu {
	public static String title = ChatColor.RED + "Settings";
	public static int size = 27;
	public static final Inventory inventory = createInventory(null, size, title);

	public SettingsMenu(Player player) {
		ItemStack pane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.displayName(Component.text(" "));
		pane.setItemMeta(paneMeta);

		ItemStack back = new ItemStack(Material.ARROW);
		ItemMeta backMeta = back.getItemMeta();
		backMeta.displayName(Component.text(ChatColor.RED + "Back"));
		backMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to return to the Account Menu")));
		back.setItemMeta(backMeta);
		inventory.setItem(22, back);

		boolean codEnabled = false;
		try {
			PreparedStatement statement = DoorDash.getConnection().prepareStatement("SELECT cod FROM playerData WHERE uuid = ?");
			statement.setString(1, player.getUniqueId().toString());
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				codEnabled = rs.getBoolean("cod");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ItemStack cod = new ItemStack(codEnabled ? Material.LIME_DYE : Material.RED_DYE);
		ItemMeta codMeta = cod.getItemMeta();
		codMeta.displayName(Component.text(codEnabled ? ChatColor.GREEN + "Cash on Delivery | Enabled" : ChatColor.RED + "Cash on Delivery | Disabled"));
		codMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Select to accept cash on delivery!")));
		cod.setItemMeta(codMeta);
		inventory.setItem(10, cod);


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

