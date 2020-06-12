package grondag.hs.entity;

import io.netty.buffer.Unpooled;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import grondag.hs.HardScience;

public class EarnestEntity extends MobEntityWithAi {
	public static Identifier IDENTIFIER = new Identifier(HardScience.MODID, "earnest");

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
		return ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
	}
}
