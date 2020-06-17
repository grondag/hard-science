package grondag.hs.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;

import grondag.fermion.varia.NBTDictionary;
import grondag.xm.api.modelstate.ModelState;
import grondag.xm.api.modelstate.MutableModelState;
import grondag.xm.api.modelstate.primitive.DynamicPrimitiveStateFunction;
import grondag.xm.api.modelstate.primitive.PrimitiveState;

public class HsBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity, BlockEntityClientSerializable {
	protected MutableModelState modelState;
	protected final DynamicPrimitiveStateFunction stateFunction;

	public HsBlockEntity(BlockEntityType<?> type, DynamicPrimitiveStateFunction stateFunction) {
		super(type);
		this.stateFunction = stateFunction;
	}

	@Override
	public Object getRenderAttachmentData() {
		return getModelState(true);
	}

	// PERF: cache world refresh
	public ModelState getModelState(boolean refreshFromWorld) {
		MutableModelState result = modelState;

		if (result == null) {
			result = stateFunction.apply(getCachedState(), world, pos, refreshFromWorld);
			modelState = result;
		} else if (refreshFromWorld) {
			// PERF: reuse mutable state instance?
			result = stateFunction.apply(getCachedState(), world, pos, refreshFromWorld);
			modelState.release();
			modelState = result;
		}

		return result;
	}

	public PrimitiveState getDefaultPrimitiveState() {
		return stateFunction.getDefaultState();
	}

	public void  setDefaultPrimitiveState(PrimitiveState defaultPrimitiveState) {
		stateFunction.setDefaultState(defaultPrimitiveState.toImmutable());

		if (modelState != null) {
			modelState.release();
			modelState = null;
		}

		sync();
	}

	protected void writeModelTags(CompoundTag tag) {
		if (modelState != null && modelState.isStatic()) {
			tag.put(TAG_MODEL_STATE, modelState.toTag());
		}

		tag.put(TAG_MODEL_STATE, stateFunction.getDefaultState().toTag());
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
		if (modelState != null) {
			modelState.release();
			modelState = null;
		}

		if (tag.contains(TAG_MODEL_STATE)) {
			modelState = ModelState.fromTag(tag.getCompound(TAG_MODEL_STATE));
		}

		if (tag.contains(TAG_DEFAULT_STATE)) {
			stateFunction.setDefaultState((PrimitiveState) ModelState.fromTag(tag.getCompound(TAG_DEFAULT_STATE)));
		}
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		readModelTags(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		readModelTags(tag);
	}

	private static final String TAG_MODEL_STATE = NBTDictionary.GLOBAL.claim("hsms");
	private static final String TAG_DEFAULT_STATE = NBTDictionary.GLOBAL.claim("hsds");

}
