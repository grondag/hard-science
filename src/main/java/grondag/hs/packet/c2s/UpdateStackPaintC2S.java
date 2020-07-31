package grondag.hs.packet.c2s;

import io.netty.buffer.Unpooled;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;

import grondag.hs.HardScience;
import grondag.hs.block.HsBlockItem;
import grondag.xm.api.modelstate.ModelState;
import grondag.xm.api.paint.PaintIndex;


public enum UpdateStackPaintC2S {
	;

	@Environment(EnvType.CLIENT)
	public static void send(ModelState state, Hand hand) {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(hand == Hand.OFF_HAND);
		state.toBytes(buf);
		final Packet<?> packet = ClientSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
		ClientSidePacketRegistry.INSTANCE.sendToServer(packet);
	}

	public static void handle(PacketContext context, PacketByteBuf buf) {
		final PlayerEntity player = context.getPlayer();

		if (player == null) {
			return;
		}

		final boolean offHand = buf.readBoolean();
		final ModelState modelState = ModelState.fromBytes(buf, PaintIndex.forWorld(player.world));

		if (context.getTaskQueue().isOnThread()) {
			handle(player, modelState, offHand);
		} else {
			context.getTaskQueue().execute(() -> handle(player, modelState, offHand));
		}
	}

	protected static void handle(PlayerEntity player, ModelState modelState, boolean offHand) {
		final ItemStack stack = offHand ? player.getOffHandStack() : player.getMainHandStack();

		if (stack.getItem() instanceof HsBlockItem) {
			((HsBlockItem) stack.getItem()).acceptClientModelStateUpdate(player, stack, modelState, offHand);
		}
	}

	public static Identifier IDENTIFIER = HardScience.REG.id("usp");

}
