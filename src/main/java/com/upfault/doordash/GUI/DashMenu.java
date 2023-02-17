package com.upfault.doordash.GUI;

import com.upfault.doordash.DoorDash;
import com.upfault.doordash.util.StopWatch;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

import static org.bukkit.Bukkit.createInventory;

public class DashMenu implements Listener {

	StopWatch stopwatch = new StopWatch();
	public static int task = 1;
	public static String title = ChatColor.DARK_GREEN + "Now Dashing";
	public static int size = 27;
	public static final Inventory inventory = createInventory(null, size, title);

	public DashMenu(Player player) {
		ItemStack pane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.displayName(Component.text(" "));
		pane.setItemMeta(paneMeta);

		int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(DoorDash.getInstance(), () -> {

			if(!stopwatch.isRunning(player.getUniqueId())) {
				ItemStack resume = new ItemStack(Material.GREEN_WOOL);
				ItemMeta resumeMeta = resume.getItemMeta();
				resumeMeta.displayName(Component.text(ChatColor.GREEN + "Resume Dash"));
				resumeMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to pause your dash")));
				resume.setItemMeta(resumeMeta);
				inventory.setItem(11, resume);
			} else {
				ItemStack pause = new ItemStack(Material.ORANGE_WOOL);
				ItemMeta pauseMeta = pause.getItemMeta();
				pauseMeta.displayName(Component.text(ChatColor.GOLD + "Pause Dash"));
				pauseMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to pause your dash")));
				pause.setItemMeta(pauseMeta);
				inventory.setItem(11, pause);
			}

			// Get the existing clock item and its meta
			ItemStack clockItem = new ItemStack(Material.CLOCK);
			assert clockItem != null;
			ItemMeta clockMeta = clockItem.getItemMeta();

			// Update the display name with the formatted time
			clockMeta.displayName(Component.text(ChatColor.GOLD + "Time Elapsed - " + stopwatch.getFormattedElapsedTime(player.getUniqueId())));

			// Set the updated meta back to the clock item and put it back in the inventory
			clockItem.setItemMeta(clockMeta);
			inventory.setItem(13, clockItem);
		}, 0L, 10L);

		ItemStack end = new ItemStack(Material.RED_WOOL);
		ItemMeta endMeta = end.getItemMeta();
		endMeta.displayName(Component.text(ChatColor.RED + "End Dash"));
		endMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to end your dash")));
		end.setItemMeta(endMeta);
		inventory.setItem(15, end);

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
}


