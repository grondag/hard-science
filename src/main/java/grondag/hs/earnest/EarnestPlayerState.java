package grondag.hs.earnest;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;
import nerdhub.cardinal.components.api.component.ComponentProvider;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

import grondag.fermion.bits.BitPacker;
import grondag.fermion.bits.BitPacker.IntElement;
import grondag.hs.HardScience;
import grondag.hs.dialog.DialogNode;
import grondag.hs.earnest.dialog.EarnestDialog;

public class EarnestPlayerState implements Component  {
	private final BitPacker.PackedState state;
	public final PlayerEntity owner;

	public EarnestPlayerState(PlayerEntity owner) {
		this.owner = owner;
		state = PACKER.newState();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		if (tag.contains("data")) {
			assert tag.getInt("version") == VERSION;
			state.readNbt("data", tag);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("version", VERSION);
		state.writeNbt("data", tag);
		return tag;
	}

	public int visitCount() {
		return VISIT_COUNT.get(state);
	}

	public int addVisit() {
		final int result = visitCount() + 1;
		VISIT_COUNT.set(state, result);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EarnestPlayerState)) return false;

		final EarnestPlayerState other = (EarnestPlayerState) o;

		return owner == other.owner
				&& other.state.areBitEqual(other.state);
	}

	@Override
	public int hashCode() {
		return 31 * state.hashCode() + owner.hashCode();
	}

	public static final ComponentType<EarnestPlayerState> COMPONENT =
			ComponentRegistry.INSTANCE.registerIfAbsent(HardScience.REG.id("earnest"), EarnestPlayerState.class);

	private static final BitPacker PACKER = new BitPacker();
	private static final int VERSION = 0;
	private static final IntElement VISIT_COUNT = PACKER.createIntElement(0x100000);

	public static EarnestPlayerState get(PlayerEntity player) {
		return COMPONENT.get(ComponentProvider.fromEntity(player));
	}

	public DialogNode<EarnestPlayerState> rootDialog() {
		return EarnestDialog.root(this, true);
	}
}
