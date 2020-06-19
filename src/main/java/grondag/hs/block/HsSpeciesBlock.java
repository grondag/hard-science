package grondag.hs.block;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import grondag.hs.HsConfig;
import grondag.xm.api.connect.species.Species;
import grondag.xm.api.connect.species.SpeciesFunction;
import grondag.xm.api.connect.species.SpeciesMode;
import grondag.xm.api.connect.species.SpeciesProperty;
import grondag.xm.api.modelstate.primitive.PrimitiveState;

public class HsSpeciesBlock extends HsBlock  {
	private final SpeciesFunction speciesFunc = SpeciesProperty.speciesForBlock(this);

	protected HsSpeciesBlock(FabricBlockSettings settings, Supplier<HsBlockEntity> beFactory, PrimitiveState defaultModelState) {
		super(settings, beFactory, defaultModelState);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(SpeciesProperty.SPECIES);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return beFactory.get();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		final Direction onFace = context.getSide();
		final BlockPos onPos = context.getBlockPos().offset(onFace.getOpposite());
		final SpeciesMode mode = HsConfig.modKey.test(context.getPlayer())
				? SpeciesMode.COUNTER_MOST : SpeciesMode.MATCH_MOST;
		final int species = Species.speciesForPlacement(context.getWorld(), onPos, onFace, mode, speciesFunc);
		return getDefaultState().with(SpeciesProperty.SPECIES, species);
	}
}
