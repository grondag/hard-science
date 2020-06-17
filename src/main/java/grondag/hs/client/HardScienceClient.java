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
package grondag.hs.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

import grondag.fermion.client.ClientRegistrar;
import grondag.hs.HardScience;
import grondag.hs.client.earnest.EarnestEntityRenderer;
import grondag.hs.earnest.EarnestEntity;
import grondag.hs.entity.Entities;
import grondag.hs.packet.s2c.EarnestDialogS2C;
import grondag.hs.packet.s2c.EarnestSpawnS2C;

public class HardScienceClient implements ClientModInitializer {
	public static ClientRegistrar REG  = new ClientRegistrar(HardScience.MODID);

	@Override
	public void onInitializeClient() {
		ClientSidePacketRegistry.INSTANCE.register(EarnestEntity.IDENTIFIER, EarnestSpawnS2C::accept);
		ClientSidePacketRegistry.INSTANCE.register(EarnestDialogS2C.IDENTIFIER, EarnestDialogS2C::handle);
		EntityRendererRegistry.INSTANCE.register(Entities.EARNEST,
				(entityRenderDispatcher, context) -> new EarnestEntityRenderer(entityRenderDispatcher));
	}
}
