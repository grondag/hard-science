package grondag.hs.block;

import java.util.function.BiFunction;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import grondag.hs.client.gui.PaintScreen;
import grondag.xm.api.modelstate.ModelState;
import grondag.xm.api.modelstate.MutableModelState;
import grondag.xm.api.modelstate.primitive.MutablePrimitiveState;
import grondag.xm.api.paint.PaintIndex;



public class HsBlockItem extends BlockItem {
	public HsBlockItem(HsBlock block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		//		if(!ctx.getPlayer().isSneaking()) {
		//			if(use(ctx.getWorld(), ctx.getPlayer(), ctx.getHand()).getResult().isAccepted()) {
		//				return ActionResult.SUCCESS;
		//			}
		//		}

		return super.useOnBlock(ctx);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		final ItemStack itemStack = playerEntity.getStackInHand(hand);

		if (world.isClient) {
			MinecraftClient.getInstance().openScreen(new PaintScreen(itemStack));
		}

		//		if (!world.isClient) {
		//			final MutablePrimitiveState modelState = readModelState(itemStack, world);
		//
		//			//			final ArrayList<TextureSet> list = new ArrayList<>();
		//
		//			//			TextureSetRegistry.instance().forEach(t -> {
		//			//				if (!t.id().getNamespace().equals("minecraft") && (t.textureGroupFlags() & TextureGroup.STATIC_TILES.bitFlag) != 0) {
		//			//					list.add(t);
		//			//				}
		//			//			});
		//
		//			if (!modelState.isStatic()) {
		//				final XmPaint newPaint = XmPaint.finder()
		//						.copy(modelState.paint(0))
		//						//						.texture(0, list.get(ThreadLocalRandom.current().nextInt(list.size())))
		//						.textureColor(0, 0xFF000000 | ThreadLocalRandom.current().nextInt(0xFFFFFF)).find();
		//
		//				modelState.paintAll(newPaint);
		//
		//				writeModelState(itemStack, modelState);
		//
		//				playerEntity.setStackInHand(hand, itemStack);
		//			}
		//
		//			modelState.release();
		//		}

		return TypedActionResult.success(itemStack);
	}

	public MutablePrimitiveState readModelState(ItemStack stack, World world) {
		assert stack.getItem() == this;

		final CompoundTag tag = stack.getOrCreateSubTag("BlockEntityTag");

		if (tag.contains(HsBlockEntity.TAG_MODEL_STATE)) {
			return (MutablePrimitiveState) ModelState.fromTag(tag.getCompound(HsBlockEntity.TAG_MODEL_STATE), PaintIndex.forWorld(world));
		} else {
			return ((HsBlock) getBlock()).defaultModelState.mutableCopy();
		}
	}

	public void writeModelState(ItemStack stack, MutablePrimitiveState modelState) {
		assert stack.getItem() == this;
		stack.getOrCreateSubTag("BlockEntityTag").put(HsBlockEntity.TAG_MODEL_STATE, modelState.toTag());
	}

	@Override
	protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		if (world.isClient) {
			final CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");

			if (compoundTag != null) {
				final BlockEntity blockEntity = world.getBlockEntity(pos);

				if (blockEntity != null && blockEntity instanceof HsBlockEntity) {
					final HsBlockEntity be = (HsBlockEntity) blockEntity;
					be.setModelStateState(readModelState(stack, world));
				}
			}

			return false;
		} else {
			return writeTagToBlockEntity(world, player, pos, stack);
		}
	}

	public static final BiFunction<ItemStack, World, MutableModelState> HS_ITEM_MODEL_FUNCTION  = (s, w) -> {
		if (s.getItem() instanceof HsBlockItem) {
			return ((HsBlockItem) s.getItem()).readModelState(s, w);
		}

		return null;
	};
}
