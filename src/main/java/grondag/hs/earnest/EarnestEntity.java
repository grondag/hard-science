package grondag.hs.earnest;

import java.util.IdentityHashMap;

import com.google.common.collect.ImmutableList;
import io.netty.buffer.Unpooled;

import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Npc;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import grondag.hs.HardScience;
import grondag.hs.client.earnest.EarnestClientState;
import grondag.hs.dialog.DialogNode;
import grondag.hs.dialog.DialogOption;
import grondag.hs.earnest.dialog.EarnestDialog;
import grondag.hs.packet.s2c.EarnestDialogS2C;

public class EarnestEntity extends LivingEntity implements Npc {
	public static Identifier IDENTIFIER = HardScience.REG.id("earnest");

	// TODO: message clients if Earnest moves or becomes unable to converse
	private final IdentityHashMap<PlayerEntity, DialogNode<EarnestPlayerState>> dialogs = new IdentityHashMap<>();

	public EarnestEntity(EntityType<? extends EarnestEntity> entityType, World world) {
		super(entityType, world);
	}

	public void toBuffer(PacketByteBuf buf) {
		buf.writeVarInt(getEntityId());
		buf.writeUuid(uuid);
		buf.writeDouble(getX());
		buf.writeDouble(getY());
		buf.writeDouble(getZ());
		buf.writeByte(MathHelper.floor(pitch * 256.0F / 360.0F));
		buf.writeByte(MathHelper.floor(yaw * 256.0F / 360.0F));
		final Vec3d velocity = getVelocity();
		buf.writeShort((int) (MathHelper.clamp(velocity.x, -3.9D, 3.9D) * 8000.0D));
		buf.writeShort((int) (MathHelper.clamp(velocity.y, -3.9D, 3.9D) * 8000.0D));
		buf.writeShort((int) (MathHelper.clamp(velocity.z, -3.9D, 3.9D) * 8000.0D));
	}

	public void fromBuffer(PacketByteBuf buf) {
		setEntityId(buf.readVarInt());
		uuid = buf.readUuid();
		final double x = buf.readDouble();
		final double y = buf.readDouble();
		final double z = buf.readDouble();
		setPos(x, y, z);
		updateTrackedPosition(x, y, z);
		pitch = buf.readByte() * 360 / 256.0F;
		yaw = buf.readByte();
		final double vx = buf.readShort() / 8000.0D;
		final double vy = buf.readShort() / 8000.0D;
		final double vz = buf.readShort() / 8000.0D;
		this.setVelocity(vx, vy, vz);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		toBuffer(buf);
		return ServerPlayNetworking.createS2CPacket(IDENTIFIER, buf);
	}

	/**
	 * Called when a player interacts with this entity.
	 *
	 * @param player the player
	 * @param hand the hand the player used to interact with this entity
	 */
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		assert player.world == world;

		if (world.isClient) {
			EarnestClientState.earnest = this;
		} else if (!dialogs.containsKey(player)) {
			player.lookAt(EntityAnchor.EYES, getPos().add(0, getStandingEyeHeight(), 0));
			final DialogNode<EarnestPlayerState> root = EarnestPlayerState.get(player).rootDialog();
			dialogs.put(player, root);
			EarnestDialogS2C.BEGIN.send((ServerPlayerEntity) player, "", root);
		}

		return ActionResult.success(world.isClient);
	}

	public void endDialog(PlayerEntity player) {
		assert !world.isClient;
		dialogs.remove(player);
	}

	public void advanceDialog(PlayerEntity player, int action) {
		assert !world.isClient;

		final DialogNode<EarnestPlayerState> oldDialog = dialogs.get(player);

		if (oldDialog != null) {
			final DialogOption<EarnestPlayerState> chosen = oldDialog.actions.get(action);

			if (chosen == EarnestDialog.EXIT) {
				dialogs.remove(player);
				EarnestDialogS2C.END.send((ServerPlayerEntity) player, "", EarnestDialog.EMPTY_DIALOG);
			} else {
				final DialogNode<EarnestPlayerState> newDialog = chosen.action.apply(EarnestPlayerState.get(player));
				dialogs.put(player, newDialog);
				EarnestDialogS2C.UPDATE.send((ServerPlayerEntity) player, chosen.playerJournalText(), newDialog);
			}
		}
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return ImmutableList.of();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		// NOOP
	}

	@Override
	public Arm getMainArm() {
		return Arm.LEFT;
	}
}
