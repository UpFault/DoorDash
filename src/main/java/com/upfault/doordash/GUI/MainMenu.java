package com.upfault.doordash.GUI;

import com.upfault.doordash.DoorDash;
import com.upfault.doordash.util.StopWatch;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.createInventory;

@SuppressWarnings("all")
public class MainMenu {

	StopWatch stopWatch = new StopWatch();
	public static String title = ChatColor.RED + "DoorDash";
	public static int size = 27;
	public static final Inventory inventory = createInventory(null, size, title);

	public MainMenu(Player player) {
		ItemStack pane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.displayName(Component.text(" "));
		pane.setItemMeta(paneMeta);

		if(!stopWatch.playerTimes.containsKey(player.getUniqueId())) {
			ItemStack dashNow = new ItemStack(Material.GREEN_WOOL);
			ItemMeta dashMeta = dashNow.getItemMeta();
			dashMeta.displayName(Component.text(ChatColor.DARK_GREEN + "Dash Now"));
			dashMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to start dashing!")));
			dashNow.setItemMeta(dashMeta);
			inventory.setItem(10, dashNow);
		}
		else {
			ItemStack dashNow = new ItemStack(Material.YELLOW_WOOL);
			ItemMeta dashMeta = dashNow.getItemMeta();
			dashMeta.displayName(Component.text(ChatColor.YELLOW + "Currently Dashing"));
			dashMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to return to dash!")));
			dashNow.setItemMeta(dashMeta);
			inventory.setItem(10, dashNow);
		}

		ItemStack account = getPlayerHeadItemStack(player);
		SkullMeta accountMeta = (SkullMeta) account.getItemMeta();
		accountMeta.displayName(Component.text(ChatColor.YELLOW + "Your Account"));
		accountMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "View your account information!")));
		account.setItemMeta(accountMeta);
		inventory.setItem(12, account);

		ItemStack ratings = new ItemStack(Material.NETHER_STAR);
		ItemMeta ratingsMeta = ratings.getItemMeta();
		ratingsMeta.displayName(Component.text(ChatColor.GOLD + "Ratings"));
		ratingsMeta.lore(getLore(player));
		ratings.setItemMeta(ratingsMeta);
		inventory.setItem(14, ratings);

		ItemStack earnings = new ItemStack(Material.EMERALD);
		ItemMeta earningsMeta = earnings.getItemMeta();
		earningsMeta.displayName(Component.text(ChatColor.GREEN + "Earnings"));
		earningsMeta.lore(Collections.singletonList(Component.text(ChatColor.GRAY + "Click to view your total dash earnings!")));
		earnings.setItemMeta(earningsMeta);
		inventory.setItem(16, earnings);

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

	private List<Component> getLore(Player player) {
		UUID uuid = player.getUniqueId();
		List<Component> lore = new ArrayList<>();
		Connection conn = null;

		conn = DoorDash.getConnection();

		if (conn != null) {
			try (PreparedStatement stmt = conn.prepareStatement("SELECT cr, ar, cor, ote, ltd FROM playerData WHERE uuid = ?")) {
				stmt.setString(1, uuid.toString());
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						lore.add(Component.text(ChatColor.WHITE + "Customer Rating: " + rs.getDouble("cr")));
						lore.add(Component.text(ChatColor.WHITE + "Acceptance Rate: " + rs.getInt("ar")));
						lore.add(Component.text(ChatColor.WHITE + "Completion Rate: " + rs.getInt("cor")));
						lore.add(Component.text(ChatColor.WHITE + "On time or early: " + rs.getInt("ote")));
						lore.add(Component.text(ChatColor.WHITE + "Lifetime Deliveries: " + rs.getInt("ltd")));
					}
				}
			} catch (SQLException ex) {
				if (ex.getMessage().contains("database connection closed")) {
					// try reopening the connection
					conn = DoorDash.getConnection();
					// retry the query
					if (conn != null) {
						try (PreparedStatement stmt = conn.prepareStatement("SELECT cr, ar, cor, ote, ltd FROM playerData WHERE uuid = ?")) {
							stmt.setString(1, uuid.toString());
							try (ResultSet rs = stmt.executeQuery()) {
								if (rs.next()) {
									lore.add(Component.text(ChatColor.WHITE + "Customer Rating: " + rs.getDouble("cr")));
									lore.add(Component.text(ChatColor.WHITE + "Acceptance Rate: " + rs.getInt("ar")));
									lore.add(Component.text(ChatColor.WHITE + "Completion Rate: " + rs.getInt("cor")));
									lore.add(Component.text(ChatColor.WHITE + "On time or early: " + rs.getInt("ote")));
									lore.add(Component.text(ChatColor.WHITE + "Lifetime Deliveries: " + rs.getInt("ltd")));
								}
							}

						} catch (SQLException e) {
							throw new RuntimeException(e);
						} finally {
							try {
								conn.close();
							} catch (SQLException ex1) {
								ex1.printStackTrace();
							}
						}
					} else {
						ex.printStackTrace();
					}
				}
			}
		}
		return lore;
	}
}
