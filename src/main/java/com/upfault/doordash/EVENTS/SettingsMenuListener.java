package com.upfault.doordash.EVENTS;

import com.upfault.doordash.DoorDash;
import com.upfault.doordash.GUI.AccountInformationMenu;
import com.upfault.doordash.GUI.EarningsMenu;
import com.upfault.doordash.GUI.MainMenu;
import com.upfault.doordash.GUI.SettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SettingsMenuListener implements Listener {
	Inventory settings = SettingsMenu.inventory;
	Inventory account = AccountInformationMenu.inventory;

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory clickedInventory = event.getClickedInventory();
		if (clickedInventory == null) {
			return;
		}

		if (clickedInventory.equals(settings)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);

			if (clickedInventory.equals(settings)) {
				ItemStack clickedItem = event.getCurrentItem();
				if (clickedItem == null) {
					return;
				}
				if (clickedItem.getType() == Material.ARROW) {
					event.getWhoClicked().openInventory(account);
				}
				if (clickedItem.getType() == Material.RED_DYE) {
					try {
						PreparedStatement statement = DoorDash.getConnection().prepareStatement("UPDATE playerData SET cod = ? WHERE uuid = ?");
						statement.setBoolean(1, true);
						statement.setString(2, event.getWhoClicked().getUniqueId().toString());
						statement.executeUpdate();

						event.getWhoClicked().sendMessage(ChatColor.GREEN + "You're now accepting cash on delivery!");
						Bukkit.getScheduler().runTask(DoorDash.getInstance(), () -> event.getWhoClicked().openInventory(new SettingsMenu((Player) event.getWhoClicked()).getInventory()));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (clickedItem.getType() == Material.LIME_DYE) {
					try {
						PreparedStatement statement = DoorDash.getConnection().prepareStatement("UPDATE playerData SET cod = ? WHERE uuid = ?");
						statement.setBoolean(1, false);
						statement.setString(2, event.getWhoClicked().getUniqueId().toString());
						statement.executeUpdate();

						event.getWhoClicked().sendMessage(ChatColor.GREEN + "You're no longer accepting cash on delivery!");
						Bukkit.getScheduler().runTask(DoorDash.getInstance(), () -> event.getWhoClicked().openInventory(new SettingsMenu((Player) event.getWhoClicked()).getInventory()));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event) {
		Inventory draggedInventory = event.getInventory();
		if (draggedInventory.equals(settings)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory destination = event.getDestination();
		Inventory source = event.getSource();

		if (destination.equals(settings) || source.equals(settings)) {
			event.setCancelled(true);
			event.setItem(null);
		}
	}
}
