package ovh.fedox.flockapi.command.flock;


import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;

/**
 * FlockCommandGroup.java -
 * <p>
 * Created on 4/3/2025 at 7:57 PM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@AutoRegister
public final class FlockCommandGroup extends SimpleCommandGroup {

	public FlockCommandGroup() {
		super("flock");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new FlockAddCommand(this));
		registerSubcommand(new FlockRemoveCommand(this));
		registerSubcommand(new FlockSetCommand(this));
	}


}
