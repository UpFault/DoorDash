package com.upfault.doordash;

import com.upfault.doordash.CMD.DashCommand;
import com.upfault.doordash.EVENTS.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DoorDash extends JavaPlugin {

	@Getter
	public static DoorDash instance;
	public static final String logPrefix = "[DD]";
	public static final String pluginPrefix = ChatColor.RED + "[DoorDash]";
	private static Connection connection;

	@Override
	public void onEnable() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		connection = createDatabaseConnection();
		instance = this;
//		VersionCheck.check();
		Bukkit.getPluginManager().registerEvents(new DashMenuListener(), this);
		Bukkit.getPluginManager().registerEvents(new EarningsMenuListener(), this);
		Bukkit.getPluginManager().registerEvents(new RatingsMenuListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInfoListener(), this);
		Bukkit.getPluginManager().registerEvents(new MainMenuListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDataEvent(), this);
		Bukkit.getPluginManager().registerEvents(new SettingsMenuListener(), this);
		Objects.requireNonNull(getServer().getPluginCommand("dash")).setExecutor(new DashCommand());
		generateConfig();
	}

	@Override
	public void onDisable() {
		saveDefaultConfig();
		instance = null;
	}

	private void generateConfig() {
		String[] files = new String[]{"config.yml"};
		File dataFolder = getDataFolder();

		File databaseDir = new File(getDataFolder(), "database");
		if (!databaseDir.exists() && !databaseDir.mkdirs()) {
			getLogger().severe("Failed to create database directory");
		}

		for (String file : files) {
			File configFile = new File(dataFolder, file);
			File parentDir = configFile.getParentFile();
			if (!parentDir.exists() && !parentDir.mkdirs()) {
				getLogger().severe("Failed to create the directory for " + file);
				continue;
			}
			this.saveDefaultConfig(configFile, file);
		}
	}

	private void saveDefaultConfig(final File configFile, final String defaultConfigFile) {
		if (!configFile.exists()) {
			saveResource(defaultConfigFile, false);
		}

		final FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		final YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new File(defaultConfigFile));
		AtomicBoolean needsSaving = new AtomicBoolean(false);

		defaultConfig.getKeys(true).forEach(key -> {
			if (!config.contains(key)) {
				config.set(key, defaultConfig.get(key));
				needsSaving.set(true);
			}
		});

		if (needsSaving.get()) {
			try {
				config.save(configFile);
			} catch (IOException ignored) {
				getLogger().severe("Failed to save " + configFile.getName() + " to disk!");
			}
		}
	}

	public static Connection getConnection() {
		return connection;
	}

	private Connection createDatabaseConnection() {
		FileConfiguration config = getConfig();
		File localDbFile = new File(getDataFolder(), "database/playerData.db");
		try {
			if (localDbFile.exists()) {
				connection = DriverManager.getConnection("jdbc:sqlite:" + localDbFile.getAbsolutePath());
				Bukkit.getLogger().info("Connection established to: " + connection.toString());
			} else {
				String host = config.getString("database.host");
				int port = config.getInt("database.port");
				String databaseName = config.getString("database.database");
				String username = config.getString("database.user");
				String password = config.getString("database.password");

				if (host == null || databaseName == null || username == null || password == null) {
					Bukkit.getLogger().severe("[PlayerDataEvent] Error: Missing required values in config.yml");
					return null;
				}

				if (host.equals("localhost") && port == 3306 && databaseName.equals("db") && username.equals("username") && password.equals("password")) {
					Bukkit.getLogger().warning("[PlayerDataEvent] The database credentials are still set to the default values!");
					return null;
				}
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName, username, password);
				Bukkit.getLogger().info("Connection established to: " + connection.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
