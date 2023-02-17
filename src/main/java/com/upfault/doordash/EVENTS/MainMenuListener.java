package com.upfault.doordash.EVENTS;

import com.upfault.doordash.GUI.AccountInformationMenu;
import com.upfault.doordash.GUI.DashMenu;
import com.upfault.doordash.GUI.EarningsMenu;
import com.upfault.doordash.GUI.MainMenu;
import com.upfault.doordash.util.StopWatch;
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

public class MainMenuListener implements Listener {

	Inventory main = MainMenu.inventory;
	StopWatch stopwatch = new StopWatch();
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory clickedInventory = event.getClickedInventory();
		if (clickedInventory == null) {
			return;
		}

		if (clickedInventory.equals(main)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);

			if (clickedInventory.equals(main)) {
				ItemStack clickedItem = event.getCurrentItem();
				if (clickedItem == null) {
					return;
				}

				if (clickedItem.getType() == Material.EMERALD) {
					event.getWhoClicked().openInventory(new EarningsMenu((Player) event.getWhoClicked()).getInventory());
				} else if (clickedItem.getType() == Material.GREEN_WOOL) {
					stopwatch.start(event.getWhoClicked().getUniqueId());
					event.getWhoClicked().openInventory(new DashMenu((Player) event.getWhoClicked()).getInventory());
				} else if(clickedItem.getType() == Material.YELLOW_WOOL) {
					event.getWhoClicked().openInventory(new DashMenu((Player) event.getWhoClicked()).getInventory());
				} else if (clickedItem.getType() == Material.PLAYER_HEAD) {
					event.getWhoClicked().openInventory(new AccountInformationMenu((Player) event.getWhoClicked()).getInventory());
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event) {
		Inventory draggedInventory = event.getInventory();
		if (draggedInventory.equals(main)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory destination = event.getDestination();
		Inventory source = event.getSource();

		if (destination.equals(main) || source.equals(main)) {
			event.setCancelled(true);
			event.setItem(null);
		}
	}
}
