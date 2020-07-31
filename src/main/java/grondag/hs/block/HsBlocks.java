package grondag.hs.block;

import static grondag.hs.HardScience.REG;

import net.minecraft.block.entity.BlockEntityType;

import grondag.hs.client.gui.ColorPicker;
import grondag.xm.api.block.XmBlockRegistry;
import grondag.xm.api.connect.species.SpeciesProperty;
import grondag.xm.api.modelstate.primitive.PrimitiveState;
import grondag.xm.api.modelstate.primitive.PrimitiveStateMutator;
import grondag.xm.api.paint.XmPaint;
import grondag.xm.api.primitive.simple.Cube;
import grondag.xm.api.texture.XmTextures;

public abstract class HsBlocks {
	private HsBlocks() { }

	public static final XmPaint DEFAULT_PAINT = XmPaint.finder().texture(0, XmTextures.TILE_NOISE_LIGHT).textureColor(0, ColorPicker.DEFAULT_WHITE_RGB).find();

	public static final PrimitiveState CUBE_DEFAULT_STATE = Cube.INSTANCE.newState().paintAll(DEFAULT_PAINT).releaseToImmutable();
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

	////

	//	public static final PrimitiveState RC_DEFAULT_STATE = RoundedColumn.INSTANCE.newState().paintAll(DEFAULT_PAINT).releaseToImmutable();
	//	public static final HsBlock DURACRETE_ROUND_COLUMN = REG.blockNoItem("dcrc", new HsBlock(HsBlockSettings.duraCrete(), HsBlocks::roundedColumnBe, RC_DEFAULT_STATE));
	//	public static final BlockEntityType<HsBlockEntity> ROUND_COLUMN_BLOCK_ENTITY_TYPE = REG.blockEntityType("dcrc", HsBlocks::roundedColumnBe, DURACRETE_ROUND_COLUMN);
	//	public static final HsBlockItem DURACRETE_ROUNDED_COLUMN_ITEM = REG.item("dcrc_item", new HsBlockItem(DURACRETE_ROUND_COLUMN, REG.itemSettings()));
	//
	//	private static HsBlockEntity roundedColumnBe() {
	//		return new HsBlockEntity(ROUND_COLUMN_BLOCK_ENTITY_TYPE, RC_DEFAULT_STATE, PrimitiveStateMutator.builder().build());
	//	}

	public static void init() {
		XmBlockRegistry.addBlock(DURACRETE_CUBE, HsBlockEntity.STATE_ACCESS_FUNC, HsBlockItem.HS_ITEM_MODEL_FUNCTION);
		//		XmBlockRegistry.addBlock(DURACRETE_ROUND_COLUMN, HsBlockEntity.STATE_ACCESS_FUNC, HsBlockItem.HS_ITEM_MODEL_FUNCTION);
	}
}
