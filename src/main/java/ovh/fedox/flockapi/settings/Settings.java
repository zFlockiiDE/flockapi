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

	private static void init() {
		setPathPrefix(null);
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

		private static void init() {
			setPathPrefix("Player_Listener");

			JOIN_MESSAGE = getBoolean("Join_Message");
			QUIT_MESSAGE = getBoolean("Quit_Message");
			CHAT_FORMAT = getBoolean("Chat_Format");
		}
	}


}
