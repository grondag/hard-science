package grondag.hs.packet.c2s;

import io.netty.buffer.Unpooled;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;

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
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(earnest.getEntityId());
		buf.writeVarInt(actionIndex);


		final Packet<?> packet = ClientSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
		ClientSidePacketRegistry.INSTANCE.sendToServer(packet);
	}

	public static void handle(PacketContext context, PacketByteBuf buf) {
		final PlayerEntity player = context.getPlayer();

		if (player == null) {
			return;
		}

		final int entityId = buf.readVarInt();
		final int action = buf.readVarInt();

		if (context.getTaskQueue().isOnThread()) {
			handle(player, entityId, action);
		} else {
			context.getTaskQueue().execute(() -> handle(player, entityId, action));
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
