package grondag.hs.block;

import static grondag.hs.HardScience.REG;

import net.minecraft.block.entity.BlockEntityType;

import grondag.xm.api.block.XmBlockRegistry;
import grondag.xm.api.connect.species.SpeciesProperty;
import grondag.xm.api.modelstate.primitive.PrimitiveState;
import grondag.xm.api.modelstate.primitive.PrimitiveStateMutator;
import grondag.xm.api.paint.XmPaint;
import grondag.xm.api.primitive.simple.Cube;
import grondag.xm.api.texture.XmTextures;

public abstract class HsBlocks {
	private HsBlocks() { }

	public static final XmPaint DEFAULT_PAINT = XmPaint.finder().texture(0, XmTextures.TILE_NOISE_LIGHT).find();
	public static final PrimitiveState CUBE_DEFAULT_STATE = Cube.INSTANCE.newState().releaseToImmutable();
	public static final HsBlock DURACRETE_CUBE = REG.blockNoItem("dcc", new HsSpeciesBlock(HsBlockSettings.duraCrete(), HsBlocks::cubeBe, CUBE_DEFAULT_STATE));
	public static final BlockEntityType<HsBlockEntity> CUBE_BLOCK_ENTITY_TYPE = REG.blockEntityType("dcc", HsBlocks::cubeBe, DURACRETE_CUBE);

	public static final PrimitiveStateMutator CUBE_STATE_FUNC = PrimitiveStateMutator.builder()
			.withJoin(SpeciesProperty.matchBlockAndSpecies())
			.withUpdate(SpeciesProperty.SPECIES_MODIFIER)
			.build();

	public static final HsBlockItem DURACRETE_CUBE_ITEM = REG.item("dcc_item", new HsBlockItem(DURACRETE_CUBE, REG.itemSettings()));


	private static HsBlockEntity cubeBe() {
		return new HsBlockEntity(CUBE_BLOCK_ENTITY_TYPE, CUBE_DEFAULT_STATE, CUBE_STATE_FUNC);
	}

	public static void init() {
		XmBlockRegistry.addBlock(DURACRETE_CUBE, HsBlockEntity.STATE_ACCESS_FUNC, HsBlockItem.HS_ITEM_MODEL_FUNCTION);
	}
}
