package grondag.hs.client.earnest;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;

import grondag.hs.earnest.EarnestEntity;
import grondag.hs.entity.Entities;


@Environment(EnvType.CLIENT)
public class EarnestNetworkHandler {
	public static void accept(PacketContext context, PacketByteBuf buffer) {
		final MinecraftClient client = MinecraftClient.getInstance();
		final EarnestEntity entity = new EarnestEntity(Entities.EARNEST, client.world);
		entity.fromBuffer(buffer);

		if (client.isOnThread()) {
			spawn(client, entity);
		} else {
			client.execute(() -> spawn(client, entity));
		}
	}

	private static void spawn(MinecraftClient client, EarnestEntity entity) {
		final ClientWorld world = client.world;

		if (world == null) {
			return;
		}

		world.addEntity(entity.getEntityId(), entity);
	}
}
