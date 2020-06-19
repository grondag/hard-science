package grondag.hs.block;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import grondag.xm.api.modelstate.primitive.PrimitiveState;

public class HsBlock extends Block implements BlockEntityProvider {
	protected final Supplier<HsBlockEntity> beFactory;
	protected final PrimitiveState defaultModelState;

	protected HsBlock(FabricBlockSettings settings, Supplier<HsBlockEntity> beFactory, PrimitiveState defaultModelState) {
		super(settings);
		this.beFactory = beFactory;
		this.defaultModelState = defaultModelState.toImmutable();
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return beFactory.get();
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof HsBlockEntity) {
			final HsBlockEntity hsBlockEntity = (HsBlockEntity) blockEntity;

			if (!world.isClient && !player.isCreative()) {
				final ItemStack itemStack = new ItemStack(this);

				final CompoundTag compoundTag = new CompoundTag();
				hsBlockEntity.writeModelTags(compoundTag);
				itemStack.putSubTag("BlockEntityTag", compoundTag);

				final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			}
		}

		super.onBreak(world, pos, state, player);
	}

	//	@Override
	//	@Environment(EnvType.CLIENT)
	//	public void buildTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
	//		super.buildTooltip(stack, world, tooltip, options);
	//		final CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
	//		if (compoundTag != null) {
	//			if (compoundTag.contains("LootTable", 8)) {
	//				tooltip.add(new LiteralText("???????"));
	//			}
	//
	//			if (compoundTag.contains("Items", 9)) {
	//				final DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
	//				Inventories.fromTag(compoundTag, defaultedList);
	//				int i = 0;
	//				int j = 0;
	//				final Iterator var9 = defaultedList.iterator();
	//
	//				while(var9.hasNext()) {
	//					final ItemStack itemStack = (ItemStack)var9.next();
	//					if (!itemStack.isEmpty()) {
	//						++j;
	//						if (i <= 4) {
	//							++i;
	//							final MutableText mutableText = itemStack.getName().shallowCopy();
	//							mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
	//							tooltip.add(mutableText);
	//						}
	//					}
	//				}
	//
	//				if (j - i > 0) {
	//					tooltip.add((new TranslatableText("container.shulkerBox.more", new Object[]{j - i})).formatted(Formatting.ITALIC));
	//				}
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	@Environment(EnvType.CLIENT)
	//	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
	//		final ItemStack itemStack = super.getPickStack(world, pos, state);
	//		final ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)world.getBlockEntity(pos);
	//		final CompoundTag compoundTag = shulkerBoxBlockEntity.serializeInventory(new CompoundTag());
	//		if (!compoundTag.isEmpty()) {
	//			itemStack.putSubTag("BlockEntityTag", compoundTag);
	//		}
	//
	//		return itemStack;
	//	}
}
