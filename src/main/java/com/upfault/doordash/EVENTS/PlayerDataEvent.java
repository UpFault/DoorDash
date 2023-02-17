package com.upfault.doordash.EVENTS;

import com.upfault.doordash.DoorDash;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author upfault
 */

public class PlayerDataEvent implements Listener {
	public PlayerDataEvent() {
		try {
			PreparedStatement statement = DoorDash.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS playerData (uuid TEXT PRIMARY KEY, username TEXT, cod BOOLEAN, ltd INTEGER, ote INTEGER, cor, INTEGER, ar INTEGER, cr DOUBLE)");
			statement.executeUpdate();
		} catch (SQLException e) {
			Bukkit.getLogger().severe(e.getMessage());
		}
	}



	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID playerUUID = player.getUniqueId();

		String checkQuery = "SELECT * FROM playerData WHERE uuid = ?";
		try (PreparedStatement checkStmt = DoorDash.getConnection().prepareStatement(checkQuery)) {
			checkStmt.setString(1, playerUUID.toString());
			ResultSet result = checkStmt.executeQuery();
			if (!result.next()) {
				String insertQuery = "INSERT INTO playerData (uuid, username, cod, ltd, ote, cor, ar, cr) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement insertStmt = DoorDash.getConnection().prepareStatement(insertQuery)) {
					insertStmt.setString(1, playerUUID.toString());
					insertStmt.setString(2, player.getName());
					insertStmt.setInt(3, 0);
					insertStmt.setInt(3, 0);
					insertStmt.setInt(3, 0);
					insertStmt.setInt(3, 0);
					insertStmt.setDouble(3, 0.00);
					insertStmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
