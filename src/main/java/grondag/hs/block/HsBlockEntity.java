package grondag.hs.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;

import grondag.xm.api.modelstate.ModelState;
import grondag.xm.api.modelstate.MutableModelState;

public class HsBlockEntity extends BlockEntity implements RenderAttachmentBlockEntity { //, BlockEntityClientSerializable {
	protected MutableModelState modelState;

	public HsBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public Object getRenderAttachmentData() {
		return modelState;
	}

	public ModelState getModelState() {
		return modelState;
	}

	public void  setModelState(ModelState modelState) {
		if (this.modelState != null) {
			this.modelState.release();
		}

		this.modelState = modelState.mutableCopy();
		// TODO: put back
		//sync();
	}

	//	@Override
	//	public CompoundTag toTag(CompoundTag tag) {
	//		super.toTag(tag);
	//		return toContainerTag(tag);
	//	}
	//
	//	@Override
	//	public void fromTag(BlockState state, CompoundTag tag) {
	//		super.fromTag(state, tag);
	//
	//		if (modelState != null) {
	//			modelState.release();
	//		}
	//
	//		fromContainerTag(tag);
	//	}
	//
	//	@Override
	//	public void fromClientTag(CompoundTag tag) {
	//		if (modelState != null) {
	//			modelState.release();
	//		}
	//
	//		label = tag.getString(TAG_LABEL);
	//	}
	//
	//	// PERF: use a custom packet to send more efficiently without nbt
	//	// PERF: send paint NBT first time only and then integers after
	//	//	will need a per-player paint cache proxy
	//	//  negative ID is signal that paint def follow in packet
	//	// PERF: gobal paint pallette server-side - maybe for model states
	//
	//	@Override
	//	public CompoundTag toClientTag(CompoundTag tag) {
	//		if (modelState != null) {
	//			modelState.toTag(tag);
	//		}
	//		tag.putString(TAG_LABEL, label);
	//		return tag;
	//	}
}
