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
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import grondag.fermion.registrar.Registrar;
import grondag.hs.block.HsBlocks;
import grondag.hs.earnest.EarnestPlayerState;
import grondag.hs.entity.Entities;
import grondag.hs.packet.c2s.EarnestDialogC2S;
import grondag.hs.packet.c2s.UpdateStackPaintC2S;
import grondag.xm.api.texture.XmTextures;
import grondag.xm.api.texture.core.CoreTextures;
import grondag.xm.api.texture.tech.TechTextures;
import grondag.xm.api.texture.unstable.UnstableTextures;

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

		ServerPlayNetworking.registerGlobalReceiver(EarnestDialogC2S.IDENTIFIER, EarnestDialogC2S::handle);
		ServerPlayNetworking.registerGlobalReceiver(UpdateStackPaintC2S.IDENTIFIER, UpdateStackPaintC2S::handle);

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


		UnstableTextures.BIGTEX_ASPHALT.use();
		UnstableTextures.BIGTEX_CRACKED_EARTH.use();
		UnstableTextures.BIGTEX_MARBLE.use();
		UnstableTextures.BIGTEX_ROUGH_ROCK.use();
		UnstableTextures.BIGTEX_WEATHERED_STONE.use();
		UnstableTextures.BIGTEX_WOOD.use();
		UnstableTextures.BIGTEX_WOOD_FLIP.use();
		UnstableTextures.BIGTEX_WORN_ASPHALT.use();
		UnstableTextures.BORDER_CAUTION.use();
		UnstableTextures.BORDER_CHECKERBOARD.use();
		UnstableTextures.BORDER_FILMSTRIP.use();
		UnstableTextures.BORDER_GROOVY_PINSTRIPES.use();
		UnstableTextures.BORDER_SIGNAL.use();
		UnstableTextures.BORDER_LOGIC.use();
		UnstableTextures.BORDER_GRITTY_CHECKERBOARD.use();
		UnstableTextures.BORDER_GRITTY_PINSTRIPE_GROOVES.use();
		UnstableTextures.BORDER_GRITTY_CHECKERBOARD.use();
		UnstableTextures.BORDER_GRITTY_SIGNAL.use();
		UnstableTextures.DECAL_DIAGONAL_BARS.use();

		UnstableTextures.DECAL_SKINNY_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_THICK_DIAGONAL_CROSS_RIDGES.use();
		UnstableTextures.DECAL_THICK_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_CROSS_RIDGES.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_CROSS_BARS.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_SKINNY_DIAGNAL_CROSS_BARS.use();
		UnstableTextures.DECAL_SKINNY_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_DIAGONAL_CROSS_BARS.use();
		UnstableTextures.DECAL_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_FAT_DIAGONAL_CROSS_BARS.use();
		UnstableTextures.DECAL_FAT_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_DIAGONAL_CROSS_RIDGES.use();
		UnstableTextures.DECAL_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_SKINNY_BARS.use();
		UnstableTextures.DECAL_FAT_BARS.use();
		UnstableTextures.DECAL_THICK_BARS.use();
		UnstableTextures.DECAL_THIN_BARS.use();
		UnstableTextures.DECAL_SKINNY_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_THICK_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_SKINNY_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_FAT_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_SKINNY_BARS_90.use();
		UnstableTextures.DECAL_FAT_BARS_90.use();
		UnstableTextures.DECAL_THICK_BARS_90.use();
		UnstableTextures.DECAL_THIN_BARS_90.use();
		UnstableTextures.DECAL_SKINNY_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_THICK_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_THIN_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_SKINNY_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_FAT_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_SKINNY_BARS_RANDOM.use();
		UnstableTextures.DECAL_FAT_BARS_RANDOM.use();
		UnstableTextures.DECAL_THICK_BARS_RANDOM.use();
		UnstableTextures.DECAL_THIN_BARS_RANDOM.use();

		UnstableTextures.DECAL_SOFT_SKINNY_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_SOFT_THICK_DIAGONAL_CROSS_RIDGES.use();
		UnstableTextures.DECAL_SOFT_THICK_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_CROSS_RIDGES.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_CROSS_BARS.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_SOFT_SKINNY_DIAGNAL_CROSS_BARS.use();
		UnstableTextures.DECAL_SOFT_SKINNY_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_CROSS_BARS.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_SOFT_FAT_DIAGONAL_CROSS_BARS.use();
		UnstableTextures.DECAL_SOFT_FAT_DIAGONAL_BARS.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_CROSS_RIDGES.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_RIDGES.use();
		UnstableTextures.DECAL_SOFT_SKINNY_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_SOFT_THICK_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_RIDGES_90.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_SOFT_SKINNY_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_SOFT_FAT_DIAGONAL_BARS_90.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_RIDGES_90.use();

		UnstableTextures.DECAL_SOFT_SKINNY_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_SOFT_THICK_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_RIDGES_RANDOM.use();
		UnstableTextures.DECAL_SOFT_THIN_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_SOFT_SKINNY_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_SOFT_FAT_DIAGONAL_BARS_RANDOM.use();
		UnstableTextures.DECAL_SOFT_DIAGONAL_RIDGES_RANDOM.use();

		UnstableTextures.TILE_DOTS.use();
		UnstableTextures.TILE_DOTS_SUBTLE.use();
		UnstableTextures.TILE_DOTS_INVERSE.use();
		UnstableTextures.TILE_DOTS_INVERSE_SUBTLE.use();

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
