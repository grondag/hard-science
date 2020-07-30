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
package grondag.hs;


import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.util.EntityComponents;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.PlayerEntity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import grondag.fermion.registrar.Registrar;
import grondag.hs.block.HsBlocks;
import grondag.hs.earnest.EarnestPlayerState;
import grondag.hs.entity.Entities;
import grondag.hs.packet.c2s.EarnestDialogC2S;
import grondag.xm.api.texture.XmTextures;
import grondag.xm.api.texture.core.CoreTextures;
import grondag.xm.api.texture.tech.TechTextures;

public class HardScience implements ModInitializer {
	public static final Logger LOG = LogManager.getLogger("Hard Science");
	public static final String MODID = "hard-science";
	public static Registrar REG  = new Registrar(MODID, "hard-science");

	@Override
	public void onInitialize() {
		Entities.values();
		HardScienceConfig.init();

		EntityComponentCallback.event(PlayerEntity.class).register((player, components) -> components.put(EarnestPlayerState.COMPONENT, new EarnestPlayerState(player)));
		EntityComponents.setRespawnCopyStrategy(EarnestPlayerState.COMPONENT, RespawnCopyStrategy.ALWAYS_COPY);

		ServerSidePacketRegistry.INSTANCE.register(EarnestDialogC2S.IDENTIFIER, EarnestDialogC2S::handle);

		HsBlocks.init();

		XmTextures.TILE_NOISE_STRONG.use();
		XmTextures.TILE_NOISE_MODERATE.use();
		XmTextures.TILE_NOISE_LIGHT.use();
		XmTextures.TILE_NOISE_SUBTLE.use();
		XmTextures.TILE_NOISE_EXTREME.use();

		XmTextures.WHITE.use();
		XmTextures.BORDER_SINGLE_LINE.use();
		XmTextures.TILE_NOISE_BLUE_A.use();
		XmTextures.TILE_NOISE_BLUE_B.use();
		XmTextures.TILE_NOISE_BLUE.use();
		XmTextures.TILE_NOISE_EXTREME.use();

		TechTextures.DECAL_PLUS.use();
		TechTextures.DECAL_MINUS.use();

		CoreTextures.TILE_COBBLE.use();
		CoreTextures.BORDER_COBBLE.use();
		CoreTextures.BORDER_SMOOTH_BLEND.use();
		CoreTextures.BORDER_WEATHERED_BLEND.use();
		CoreTextures.BORDER_BEVEL.use();
		CoreTextures.BORDER_WEATHERED_LINE.use();
		CoreTextures.BIGTEX_SANDSTONE.use();
		CoreTextures.BIGTEX_RAMMED_EARTH.use();
		CoreTextures.BIGTEX_COBBLE_SQUARES.use();
		CoreTextures.BIGTEX_GRANITE.use();
		CoreTextures.BIGTEX_SNOW.use();

	}
}
