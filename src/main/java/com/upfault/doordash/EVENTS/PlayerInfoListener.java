package com.upfault.doordash.EVENTS;

import com.upfault.doordash.DoorDash;
import com.upfault.doordash.GUI.AccountInformationMenu;
import com.upfault.doordash.GUI.MainMenu;
import com.upfault.doordash.GUI.SettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerInfoListener implements Listener {

	private final Set<UUID> playersEnteringNewName = new HashSet<>();
	Inventory account = AccountInformationMenu.inventory;
	Inventory main = MainMenu.inventory;

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory clickedInventory = event.getClickedInventory();
		if (clickedInventory == null) {
			return;
		}

		if (clickedInventory.equals(account)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);

			if (clickedInventory.equals(account)) {
				ItemStack clickedItem = event.getCurrentItem();
				if (clickedItem == null) {
					return;
				}
				if (clickedItem.getType() == Material.ARROW) {
					event.getWhoClicked().openInventory(main);
				}
				if (clickedItem.getType() == Material.NAME_TAG) {
					Player player = (Player) event.getWhoClicked();
					player.closeInventory();
					player.sendMessage(ChatColor.YELLOW + "You have 30 seconds to enter a name.");
					playersEnteringNewName.add(player.getUniqueId());
					Bukkit.getScheduler().runTaskLater(DoorDash.instance, () -> {
						if (playersEnteringNewName.contains(player.getUniqueId())) {
							playersEnteringNewName.remove(player.getUniqueId());
							player.sendMessage(ChatColor.YELLOW + "You did not enter a valid username in time. Please try again.");
							player.openInventory(MainMenu.inventory);
						}
					}, 30 * 20);
				}

				if(clickedItem.getType() == Material.REDSTONE) {
					Player player = (Player) event.getWhoClicked();
					player.openInventory(new SettingsMenu(player).getInventory());
				}
			}
		}
	}


	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event) {
		Inventory draggedInventory = event.getInventory();
		if (draggedInventory.equals(account)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	@SuppressWarnings("all")
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory destination = event.getDestination();
		Inventory source = event.getSource();

		if (destination.equals(account) || source.equals(account)) {
			event.setCancelled(true);
			event.setItem(null);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if(playersEnteringNewName.contains(uuid)) {
			event.setCancelled(true);
		}
		if (playersEnteringNewName.contains(uuid)) {
			String newName = event.getMessage().trim();
			if (newName.length() > 16) {
				player.sendMessage(ChatColor.YELLOW + "Your new name must be 16 characters or less.");
				event.setCancelled(true);
				return;
			}

			try {
				PreparedStatement statement = DoorDash.getConnection().prepareStatement("SELECT * FROM playerData WHERE username = ?");
				statement.setString(1, newName);
				ResultSet resultSet = statement.executeQuery();

				if (resultSet.next()) {
					player.sendMessage(ChatColor.RED + "That name is already in use. Please choose another name.");
					event.setCancelled(true);
					return;
				}
			} catch (SQLException e) {
				player.sendMessage(ChatColor.RED + "An error occurred while checking if the name is in use.");
				e.printStackTrace();
				event.setCancelled(true);
				return;
			}

			try {
				PreparedStatement statement = DoorDash.getConnection().prepareStatement("UPDATE playerData SET username = ? WHERE uuid = ?");
				statement.setString(1, newName);
				statement.setString(2, uuid.toString());
				statement.executeUpdate();

				playersEnteringNewName.remove(uuid);

				player.sendMessage(ChatColor.YELLOW + "Your username has been updated to " + newName);
				event.setCancelled(true);
				Bukkit.getScheduler().runTask(DoorDash.getInstance(), () -> player.openInventory(new AccountInformationMenu(player).getInventory()));
			} catch (SQLException e) {
				player.sendMessage(ChatColor.RED + "An error occurred while updating your username.");
				e.printStackTrace();
				event.setCancelled(true);
			}
		}
	}
}