package ovh.fedox.flockapi.database;


import lombok.Getter;
import org.mineacademy.fo.Common;
import ovh.fedox.flockapi.settings.Settings;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * RedisManager.java -
 * <p>
 * Created on 3/31/2025 at 8:11 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class RedisManager {

	private static final String REDIS_HOST = Settings.Redis.REDIS_HOST;
	private static final int REDIS_PORT = Integer.parseInt(Settings.Redis.REDIS_PORT);
	private static final String REDIS_USER = Settings.Redis.REDIS_USER;
	private static final String REDIS_PASSWORD = Settings.Redis.REDIS_PASSWORD;

	@Getter
	private static Jedis jedis;

	public static void connect() {
		jedis = new JedisPool(REDIS_HOST, REDIS_PORT, REDIS_USER, REDIS_PASSWORD).getResource();

		if (jedis.ping() != null) {
			Common.log("&aSuccess: &7Successfully connected to Redis!");
		}
	}

	public static void close() {
		jedis.close();

		Common.log("&aSuccess: &7Redis connection closed.");
	}
}
