package grondag.hs.packet.s2c;

import io.netty.buffer.Unpooled;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import grondag.hs.HardScience;
import grondag.hs.client.earnest.EarnestClientState;
import grondag.hs.client.earnest.EarnestScreen;
import grondag.hs.dialog.DialogNode;
import grondag.hs.earnest.EarnestPlayerState;


public enum EarnestDialogS2C {
	BEGIN() {
		@Environment(EnvType.CLIENT)
		@Override
		protected void handle(PlayerEntity player, String playerText, String npcText, String[] actions) {
			EarnestClientState.resetDialog();
			MinecraftClient.getInstance().openScreen(new EarnestScreen());
			super.handle(player, playerText, npcText, actions);
		}
	},
	UPDATE,
	END () {
		@Environment(EnvType.CLIENT)
		@Override
		protected void handle(PlayerEntity player, String playerText, String npcText, String[] actions) {
			((ClientPlayerEntity) player).closeScreen();
		}
	};

	private static EarnestDialogS2C[] VALUES = values();

	public void send(ServerPlayerEntity player, String playerText, DialogNode<EarnestPlayerState> dialoge) {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeByte(ordinal());
		buf.writeString(playerText);
		buf.writeString(dialoge.npcText);
		final int limit = dialoge.actions.size();
		buf.writeVarInt(limit);

		for (int i = 0; i < limit; ++i) {
			buf.writeString(dialoge.actions.get(i).playerText);
		}

		final Packet<?> packet = ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packet);
	}

	@Environment(EnvType.CLIENT)
	public static void handle(PacketContext context, PacketByteBuf buf) {
		final PlayerEntity player = context.getPlayer();

		if (player == null) {
			return;
		}

		final EarnestDialogS2C handler = VALUES[buf.readByte()];
		final String playerText = buf.readString();
		final String npcText = buf.readString();

		final int limit = buf.readVarInt();
		final String[] actions = new String[limit];

		for (int i = 0; i < limit; ++i) {
			actions[i] = buf.readString();
		}

		if (context.getTaskQueue().isOnThread()) {
			handler.handle(player, playerText, npcText, actions);
		} else {
			context.getTaskQueue().execute(() -> handler.handle(player, playerText, npcText, actions));
		}
	}

	@Environment(EnvType.CLIENT)
	protected void handle(PlayerEntity player, String playerText, String npcText, String[] actions) {
		EarnestClientState.advanceDialog(playerText, npcText, actions);
	}

	public static Identifier IDENTIFIER = HardScience.REG.id("earnest_dialog");

}
