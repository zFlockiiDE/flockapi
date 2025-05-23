package ovh.fedox.flockapi.settings;


import lombok.Getter;
import org.mineacademy.fo.settings.SimpleSettings;

/**
 * Settings.java - Settings for this plugin
 * <p>
 * Created on 3/30/2025 at 3:47 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@Getter
public final class Settings extends SimpleSettings {

	public static String PROXY_IP;

	private static void init() {
		setPathPrefix(null);

		PROXY_IP = getString("Proxy_IP");
	}

	public static class MongoDB {
		public static String MONGO_CONNECTION_STRING;
		public static String MONGO_DATABASE;

		private static void init() {
			setPathPrefix("MongoDB");

			MONGO_CONNECTION_STRING = getString("Connection_String");
			MONGO_DATABASE = getString("Database");
		}
	}

	public static class PlayerListener {
		public static Boolean JOIN_MESSAGE;
		public static Boolean QUIT_MESSAGE;
		public static Boolean CHAT_FORMAT;
		public static Boolean ACHIEVEMENTS;

		private static void init() {
			setPathPrefix("Player_Listener");

			JOIN_MESSAGE = getBoolean("Join_Message");
			QUIT_MESSAGE = getBoolean("Quit_Message");
			CHAT_FORMAT = getBoolean("Chat_Format");
			ACHIEVEMENTS = getBoolean("Achievements");
		}
	}

	public static class Redis {
		public static String REDIS_HOST;
		public static String REDIS_PORT;
		public static String REDIS_USER;
		public static String REDIS_PASSWORD;

		private static void init() {
			setPathPrefix("Redis");

			REDIS_HOST = getString("Host");
			REDIS_PORT = getString("Port");
			REDIS_USER = getString("User");
			REDIS_PASSWORD = getString("Password");
		}
	}

	public static class General_Settings {
		public static Boolean COUNT_PLAYERS;

		private static void init() {
			setPathPrefix("General_Settings");

			COUNT_PLAYERS = getBoolean("Count_Players");
		}
	}


}
