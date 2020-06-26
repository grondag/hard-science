package grondag.hs.block;

import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;

public abstract class HsMaterials {
	private HsMaterials() { }

	public static final Material DURACRETE = new FabricMaterialBuilder(MaterialColor.STONE).build();
	public static final Material DURAGLASS = new FabricMaterialBuilder(MaterialColor.CLEAR).build();
	public static final Material DURAWOOD = new FabricMaterialBuilder(MaterialColor.WOOD).build();
	public static final Material DURASTEEL = new FabricMaterialBuilder(MaterialColor.IRON).build();

	public static final Material HYPERCRETE = new FabricMaterialBuilder(MaterialColor.STONE).blocksPistons().build();
	public static final Material HYPERGLASS = new FabricMaterialBuilder(MaterialColor.CLEAR).blocksPistons().build();
	public static final Material HYPERWOOD = new FabricMaterialBuilder(MaterialColor.WOOD).blocksPistons().build();
	public static final Material HYPERSTEEL = new FabricMaterialBuilder(MaterialColor.IRON).blocksPistons().build();
}
