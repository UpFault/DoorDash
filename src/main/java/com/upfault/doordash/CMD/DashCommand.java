package com.upfault.doordash.CMD;

import com.upfault.doordash.DoorDash;
import com.upfault.doordash.GUI.MainMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DashCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(DoorDash.logPrefix + " You must be a player to use this command.");
		}
		assert sender instanceof Player;
		Player player = (Player) sender;
		if(!(player.hasPermission("doordash.main"))) {
			player.sendMessage(DoorDash.pluginPrefix +  ChatColor.RED + " You do not have the correct permissions for this command.");
		}
		if(command.getName().equalsIgnoreCase("dash") || command.getName().equalsIgnoreCase("dd") || command.getName().equalsIgnoreCase("doordash")) {
			player.openInventory(new MainMenu(player).getInventory());
		}
		return false;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		List<String> list = Arrays.asList("start", "end", "pause", "help");
		if(args.length == 1) {
			return list;
		}
		return null;
	}
}
