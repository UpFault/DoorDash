package com.upfault.doordash.EVENTS;

import com.upfault.doordash.GUI.DashMenu;
import com.upfault.doordash.GUI.MainMenu;
import com.upfault.doordash.util.StopWatch;
import net.kyori.adventure.text.Component;
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

import java.util.Objects;

public class DashMenuListener  implements Listener {
	Inventory dash = DashMenu.inventory;
	StopWatch stopwatch = new StopWatch();
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory clickedInventory = event.getClickedInventory();
		if (clickedInventory == null) {
			return;
		}

		if (clickedInventory.equals(dash)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);

			if (clickedInventory.equals(dash)) {
				ItemStack clickedItem = event.getCurrentItem();
				if (clickedItem == null) {
					return;
				}
				if (clickedItem.getType() == Material.ARROW) {
					event.getWhoClicked().openInventory(new MainMenu((Player) event.getWhoClicked()).getInventory());
					try {
						Bukkit.getScheduler().cancelTask(DashMenu.task);
					} catch (Exception e) {
						Bukkit.getLogger().severe(e.getMessage());
					}
				}
				if (clickedItem.getType() == Material.ORANGE_WOOL) {
					stopwatch.stop(event.getWhoClicked().getUniqueId());
				}
				if (clickedItem.getType() == Material.GREEN_WOOL) {
					stopwatch.start(event.getWhoClicked().getUniqueId());
				}
				if (clickedItem.getType() == Material.RED_WOOL) {
					event.getWhoClicked().closeInventory();
					Objects.requireNonNull(((Player) event.getWhoClicked()).getPlayer()).sendMessage(
							Component.text(ChatColor.GREEN + "You actively dashed for " + stopwatch.getFormattedElapsedTime(event.getWhoClicked().getUniqueId()) + "!"));
					stopwatch.reset(event.getWhoClicked().getUniqueId());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event) {
		Inventory draggedInventory = event.getInventory();
		if (draggedInventory.equals(dash)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory destination = event.getDestination();
		Inventory source = event.getSource();

		if (destination.equals(dash) || source.equals(dash)) {
			event.setCancelled(true);
		}
	}
}
