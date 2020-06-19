package grondag.hs.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;

import grondag.fermion.client.RenderRefreshProxy;
import grondag.fermion.varia.NBTDictionary;
import grondag.xm.api.modelstate.ModelState;
import grondag.xm.api.modelstate.primitive.MutablePrimitiveState;
import grondag.xm.api.modelstate.primitive.PrimitiveState;
import grondag.xm.api.modelstate.primitive.PrimitiveStateFunction;
import grondag.xm.api.modelstate.primitive.PrimitiveStateMutator;

public class HsBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
	protected final PrimitiveState defaultModelState;
	protected MutablePrimitiveState modelState;
	protected final PrimitiveStateMutator stateFunction;

	public HsBlockEntity(BlockEntityType<?> type, PrimitiveState defaultModelState, PrimitiveStateMutator stateFunction) {
		super(type);
		this.defaultModelState = defaultModelState;
		this.stateFunction = stateFunction;
	}

	// PERF: cache world refresh
	public MutablePrimitiveState getModelState(boolean refreshFromWorld) {
		MutablePrimitiveState result = modelState;

		if (result == null) {
			result = defaultModelState.mutableCopy();
			modelState = result;
			refreshFromWorld = true;
		}

		if (refreshFromWorld && !result.isStatic()) {
			stateFunction.mutate(result, getCachedState(), world, pos, null, refreshFromWorld);
		}

		return result.mutableCopy();
	}


	public void setModelStateState(PrimitiveState newState) {
		// PERF: can copy instead of release?
		if (modelState != null) {
			modelState.release();
		}

		modelState = newState.mutableCopy();

		markDirty();
		sync();
	}

	protected void writeModelTags(CompoundTag tag) {
		if (modelState != null) {
			tag.put(TAG_MODEL_STATE, modelState.toTag());
		} else {
			tag.put(TAG_MODEL_STATE, defaultModelState.toTag());
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		writeModelTags(tag);
		return tag;
	}

	// PERF: use a custom packet to send more efficiently without nbt
	// PERF: send paint NBT first time only and then integers after
	//	will need a per-player paint cache proxy
	//  negative ID is signal that paint def follow in packet
	// PERF: gobal paint pallette server-side - maybe for model states

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		writeModelTags(tag);
		return tag;
	}

	protected void readModelTags(CompoundTag tag) {
		if (tag.contains(TAG_MODEL_STATE)) {
			// PERF can copy instead of allocate?
			if (modelState != null) {
				modelState.release();
			}

			modelState = (MutablePrimitiveState) ModelState.fromTag(tag.getCompound(TAG_MODEL_STATE));
		}
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return super.toInitialChunkDataTag(); //toTag(new CompoundTag());
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		readModelTags(tag);

		if (world != null && !world.isClient) {
			sync();
		}
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		readModelTags(tag);
		RenderRefreshProxy.refresh(pos);
	}

	public static final String TAG_MODEL_STATE = NBTDictionary.GLOBAL.claim("hsms");

	public static final PrimitiveStateFunction STATE_ACCESS_FUNC = (state, world, pos, refresh) -> {
		if (state.getBlock() instanceof HsBlock) {
			if (world != null && pos != null) {
				final BlockEntity be = world.getBlockEntity(pos);

				if (be != null) {
					return ((HsBlockEntity) world.getBlockEntity(pos)).getModelState(refresh);
				}
			}

			return ((HsBlock) state.getBlock()).defaultModelState.mutableCopy();
		}

		return null;
	};
}
