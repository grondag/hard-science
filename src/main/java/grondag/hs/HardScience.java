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
import grondag.hs.earnest.EarnestPlayerState;
import grondag.hs.entity.Entities;
import grondag.hs.packet.c2s.EarnestDialogC2S;

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
	}
}
