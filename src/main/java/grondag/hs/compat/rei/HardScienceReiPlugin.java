/*******************************************************************************
 * Copyright 2019, 2020 grondag
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
package grondag.hs.compat.rei;

import me.shedaniel.rei.api.EntryRegistry;
import me.shedaniel.rei.api.plugins.REIPluginV0;

import net.minecraft.util.Identifier;

import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;

import grondag.hs.HardScience;

public class HardScienceReiPlugin implements REIPluginV0 {
	public static final Identifier ID = HardScience.REG.id("rei_plugin");

	@Override
	public Identifier getPluginIdentifier() {
		return ID;
	}

	@Override
	public SemanticVersion getMinimumVersion() throws VersionParsingException {
		return SemanticVersion.parse("3.0");
	}

	@Override
	public void registerEntries(EntryRegistry entryRegistry) {

	}
}
