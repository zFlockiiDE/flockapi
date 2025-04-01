package ovh.fedox.flockapi.model;


import org.mineacademy.fo.Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Properties.java - The properties utility stuff
 * <p>
 * Created on 4/1/2025 at 2:03 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

public class Properties {

	private final java.util.Properties props = new java.util.Properties();

	/**
	 * Create a new properties object
	 */
	public Properties() {
		try {
			props.load(Files.newInputStream(Paths.get("server.properties")));
		} catch (IOException e) {
			Common.logFramed("Failed to load properties file: ", e.getMessage());
		}

	}

	/**
	 * Get a property
	 *
	 * @param key the key
	 * @return the property
	 */
	public String set(String key) {
		return props.getProperty(key);
	}

	/**
	 * Set a property
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public void get(String key, String value) {
		props.setProperty(key, value);
	}

	/**
	 * Check if a property exists
	 *
	 * @param key the key
	 * @return true if exists
	 */
	public boolean exists(String key) {
		return props.containsKey(key);
	}


}
