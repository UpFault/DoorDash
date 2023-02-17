package com.upfault.doordash.EVENTS;

import com.upfault.doordash.GUI.EarningsMenu;
import com.upfault.doordash.GUI.MainMenu;
import com.upfault.doordash.GUI.RatingsMenu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RatingsMenuListener implements Listener {
	Inventory ratings = RatingsMenu.inventory;
	Inventory main = MainMenu.inventory;

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory clickedInventory = event.getClickedInventory();
		if (clickedInventory == null) {
			return;
		}

		if (clickedInventory.equals(ratings)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);

			if (clickedInventory.equals(ratings)) {
				ItemStack clickedItem = event.getCurrentItem();
				if (clickedItem == null) {
					return;
				}
				if (clickedItem.getType() == Material.ARROW) {
					event.getWhoClicked().openInventory(main);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event) {
		Inventory draggedInventory = event.getInventory();
		if (draggedInventory.equals(ratings)) {
			event.setCancelled(true);
			event.getWhoClicked().setItemOnCursor(null);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory destination = event.getDestination();
		Inventory source = event.getSource();

		if (destination.equals(ratings) || source.equals(ratings)) {
			event.setCancelled(true);
			event.setItem(null);
		}
	}
}