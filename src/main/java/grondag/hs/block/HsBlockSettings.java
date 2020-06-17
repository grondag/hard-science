package grondag.hs.block;

import net.minecraft.sound.BlockSoundGroup;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

public abstract class HsBlockSettings {
	private HsBlockSettings() { }

	private static final float DURA_HARDNESS = 50.0F;
	private static final float DURA_RESISTANCE = 1200.0F;

	private static final float DURASTEEL_MULTIPLIER = 1.2F;
	private static final float DURAWOOD_MULTIPLIER = 0.8F;
	private static final float DURAGLASS_MULTIPLIER = 0.8F;

	private static final float HYPER_SLIP = 0.989F;

	public static FabricBlockSettings duraSteel() {
		return FabricBlockSettings.of(HsMaterials.DURASTEEL)
				.breakByTool(FabricToolTags.PICKAXES, 3)
				.requiresTool()
				.sounds(BlockSoundGroup.METAL)
				.strength(DURA_HARDNESS, DURA_RESISTANCE);
	}

	public static FabricBlockSettings duraCrete() {
		return FabricBlockSettings.of(HsMaterials.DURACRETE)
				.breakByTool(FabricToolTags.PICKAXES, 3)
				.requiresTool()
				.sounds(BlockSoundGroup.STONE)
				.strength(DURA_HARDNESS * DURASTEEL_MULTIPLIER, DURA_RESISTANCE * DURASTEEL_MULTIPLIER);
	}

	public static FabricBlockSettings duraGlass() {
		return FabricBlockSettings.of(HsMaterials.DURAGLASS)
				.breakByTool(FabricToolTags.PICKAXES, 3)
				.requiresTool()
				.sounds(BlockSoundGroup.GLASS)
				.nonOpaque()
				.strength(DURA_HARDNESS * DURAGLASS_MULTIPLIER, DURA_RESISTANCE * DURAGLASS_MULTIPLIER);
	}

	public static FabricBlockSettings duraWood() {
		return FabricBlockSettings.of(HsMaterials.DURAWOOD)
				.breakByTool(FabricToolTags.AXES, 3)
				.requiresTool()
				.sounds(BlockSoundGroup.WOOD)
				.strength(DURA_HARDNESS * DURAWOOD_MULTIPLIER, DURA_RESISTANCE * DURAWOOD_MULTIPLIER);
	}

	public static FabricBlockSettings hyperSteel() {
		return FabricBlockSettings.of(HsMaterials.HYPERSTEEL)
				.breakByTool(FabricToolTags.PICKAXES, 4)
				.requiresTool()
				.sounds(BlockSoundGroup.METAL)
				.allowsSpawning((s, w, p, t) -> false)
				.slipperiness(HYPER_SLIP)
				.strength(-1.0F, 3600000.0F);
	}

	public static FabricBlockSettings hyperCrete() {
		return FabricBlockSettings.of(HsMaterials.HYPERCRETE)
				.breakByTool(FabricToolTags.PICKAXES, 4)
				.requiresTool()
				.sounds(BlockSoundGroup.STONE)
				.allowsSpawning((s, w, p, t) -> false)
				.slipperiness(HYPER_SLIP)
				.strength(-1.0F, 3600000.0F);
	}

	public static FabricBlockSettings hyperGlass() {
		return FabricBlockSettings.of(HsMaterials.HYPERGLASS)
				.breakByTool(FabricToolTags.PICKAXES, 4)
				.requiresTool()
				.sounds(BlockSoundGroup.GLASS)
				.nonOpaque()
				.allowsSpawning((s, w, p, t) -> false)
				.slipperiness(HYPER_SLIP)
				.strength(-1.0F, 3600000.0F);
	}

	public static FabricBlockSettings hyperWood() {
		return FabricBlockSettings.of(HsMaterials.HYPERWOOD)
				.breakByTool(FabricToolTags.AXES, 4)
				.requiresTool()
				.sounds(BlockSoundGroup.WOOD)
				.allowsSpawning((s, w, p, t) -> false)
				.slipperiness(HYPER_SLIP)
				.strength(-1.0F, 3600000.0F);
	}
}
