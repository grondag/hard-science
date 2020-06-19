/*******************************************************************************
 * Copyright 2019 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package grondag.hs;

import java.io.File;
import java.io.FileOutputStream;
import java.util.function.Predicate;

import blue.endless.jankson.Comment;
import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.loader.api.FabricLoader;

import grondag.fermion.modkeys.api.ModKeys;

public class HsConfig {

	public enum Key {
		PRIMARY(ModKeys::isPrimaryPressed),
		SECONDARY(ModKeys::isSecondaryPressed),
		TERTIARY(ModKeys::isTertiaryPressed);

		private final Predicate<PlayerEntity> test;

		Key(Predicate<PlayerEntity> test) {
			this.test = test;
		}

		public boolean test(PlayerEntity player) {
			return test.test(player);
		}
	}

	@SuppressWarnings("hiding")
	public static class ConfigData {
		@Comment("Modifier key for horizontal placement/connection breaking. PRIMARY, SECONDARY or TERTIARY.")
		public Key modKey = Key.PRIMARY;

		@Comment("Modifier key for block placement. PRIMARY, SECONDARY or TERTIARY.")
		public Key forceKey = Key.SECONDARY;
	}

	public static final ConfigData DEFAULTS = new ConfigData();
	private static final Gson GSON = new GsonBuilder().create();
	private static final Jankson JANKSON = Jankson.builder().build();

	public static Key modKey = DEFAULTS.modKey;
	public static Key forceKey = DEFAULTS.forceKey;

	private static File configFile;

	public static void init() {
		configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "hard-science.json5");
		if (configFile.exists()) {
			loadConfig();
		} else {
			saveConfig();
		}
	}

	private static void loadConfig() {
		ConfigData config = new ConfigData();
		try {
			final JsonObject configJson = JANKSON.load(configFile);
			final String regularized = configJson.toJson(false, false, 0);
			config = GSON.fromJson(regularized, ConfigData.class);
		} catch (final Exception e) {
			e.printStackTrace();
			HardScience.LOG.error("Unable to load config. Using default values.");
		}

		modKey =  config.modKey;
		forceKey = config.forceKey;
	}

	public static void saveConfig() {
		final ConfigData config = new ConfigData();

		config.modKey = modKey;
		config.forceKey = forceKey;

		try {
			final String result = JANKSON.toJson(config).toJson(true, true, 0);
			if (!configFile.exists()) {
				configFile.createNewFile();
			}

			try (FileOutputStream out = new FileOutputStream(configFile, false);) {
				out.write(result.getBytes());
				out.flush();
				out.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			HardScience.LOG.error("Unable to save config.");
			return;
		}
	}

	public static boolean isModKeyPressed(PlayerEntity player) {
		return modKey.test(player);
	}

	public static boolean isForceKeyPressed(PlayerEntity player) {
		return forceKey.test(player);
	}
}
