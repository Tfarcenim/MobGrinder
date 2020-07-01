package com.tfar.mobgrinder;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MobGrinderConfigs {

	public static class ServerConfig {

		public static ForgeConfigSpec.IntValue timeBetweenKills;
		public static ForgeConfigSpec.IntValue damagePerKill;

		ServerConfig(ForgeConfigSpec.Builder builder) {
			builder.push("general");

			timeBetweenKills = builder
							.comment("time in between 'kills' performed by the machine in ticks")
							.defineInRange("time_between_kills", 100, 1, Integer.MAX_VALUE);
			damagePerKill = builder
							.comment("How much damage the catalyst item takes per kill, set to 0 to disable")
							.defineInRange("damage_per_kill", 1, 0, Integer.MAX_VALUE);
			builder.pop();
		}
	}

	public static final ServerConfig SERVER;
	public static final ForgeConfigSpec SERVER_SPEC;

	static {
		final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}
}
