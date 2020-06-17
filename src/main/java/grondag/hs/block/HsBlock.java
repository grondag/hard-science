package grondag.hs.block;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class HsBlock extends Block implements BlockEntityProvider {
	protected final Supplier<BlockEntity> beFactory;

	protected HsBlock(FabricBlockSettings settings, Supplier<BlockEntity> beFactory) {
		super(settings);
		this.beFactory = beFactory;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return beFactory.get();
	}
}
