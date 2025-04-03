package ovh.fedox.flockapi;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import ovh.fedox.flockapi.database.MongoDBManager;
import ovh.fedox.flockapi.database.RedisManager;
import ovh.fedox.flockapi.listener.PlayerListener;
import ovh.fedox.flockapi.model.Properties;
import ovh.fedox.flockapi.settings.Settings;

/**
 * FlockAPI.java - Main instance of the FlockAPI
 * <p>
 * Created on 3/31/2025 at 4:02 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public final class FlockAPI extends SimplePlugin implements PluginMessageListener {

	public static final String BUNGEECORD_CHANNEL = "BungeeCord";
	@Getter
	public static MongoDBManager mongoManager;
	@Getter
	public static LuckPerms luckPerms;
	@Getter
	public static Properties properties;

	public static FlockAPI getInstance() {
		return (FlockAPI) SimplePlugin.getInstance();
	}

	@Override
	protected void onReloadablesStart() {
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

		Common.setTellPrefix("&8&l➽ &a&lzFlockii.de &8&l•&7");

		String connectionString = Settings.MongoDB.MONGO_CONNECTION_STRING;
		String database = Settings.MongoDB.MONGO_DATABASE;

		mongoManager = new MongoDBManager(connectionString, database);

		RedisManager.connect();

		if (provider == null) {
			Common.log("&cError: &7LuckPerms is not installed on this server.");
			return;
		}

		luckPerms = provider.getProvider();

		PlayerListener.initializeTeams();
	}

	@Override
	protected void onPluginStart() {
		Common.setTellPrefix("&8&l➽ &a&lzFlockii.de &8&l•&7");

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, BUNGEECORD_CHANNEL);
		this.getServer().getMessenger().registerIncomingPluginChannel(this, BUNGEECORD_CHANNEL, this);

		properties = new Properties();

		if (!properties.exists("server-name")) {
			throw new RuntimeException("The server-name key is not set in the properties file.");
		}

		Common.log("&aSuccess: &7The FlockAPI has been enabled.");
	}

	@Override
	protected void onPluginStop() {
		mongoManager.close();

		String serverName = properties.get("server-name");
		RedisManager.getJedis().hdel("players", serverName);

		RedisManager.close();

		Common.log("&aSuccess: &7The FlockAPI has been disabled.");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals(BUNGEECORD_CHANNEL)) {
			return;
		}

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();

		if (subchannel.equals("KickPlayer")) {
			getLogger().info("Received response from BungeeCord on KickPlayer channel");
		}
	}
}
