package grondag.hs.packet.c2s;

import io.netty.buffer.Unpooled;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;

import grondag.hs.HardScience;
import grondag.hs.earnest.EarnestEntity;


public enum EarnestDialogC2S {
	CHOOSE,
	END () {
		@Environment(EnvType.CLIENT)
		@Override
		public void send(EarnestEntity earnest, int actionIndex) {
			sendInner(earnest, -1);
		}
	};

	@Environment(EnvType.CLIENT)
	public void send(EarnestEntity earnest, int actionIndex) {
		sendInner(earnest, actionIndex);
	}

	@Environment(EnvType.CLIENT)
	protected void sendInner(EarnestEntity earnest, int actionIndex) {
		if (MinecraftClient.getInstance().getNetworkHandler() != null) {
			final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeVarInt(earnest.getEntityId());
			buf.writeVarInt(actionIndex);
			ClientPlayNetworking.send(IDENTIFIER, buf);
		}
	}

	public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		if (player == null) {
			return;
		}

		final int entityId = buf.readVarInt();
		final int action = buf.readVarInt();

		if (server.isOnThread()) {
			handle(player, entityId, action);
		} else {
			server.execute(() -> handle(player, entityId, action));
		}
	}

	protected static void handle(PlayerEntity player, int entityId, int action) {
		final EarnestEntity earnest = (EarnestEntity) player.world.getEntityById(entityId);

		if (action == -1) {
			earnest.endDialog(player);
		} else {
			earnest.advanceDialog(player, action);
		}
	}

	public static Identifier IDENTIFIER = HardScience.REG.id("earnest_dialog_c2s");

}
