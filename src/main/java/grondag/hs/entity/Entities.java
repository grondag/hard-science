package grondag.hs.entity;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import grondag.hs.earnest.EarnestEntity;

public enum Entities {
	;

	public static final EntityType<? extends EarnestEntity> EARNEST = Registry.register(Registry.ENTITY_TYPE, EarnestEntity.IDENTIFIER,
			FabricEntityTypeBuilder.<EarnestEntity>create(SpawnGroup.MISC, EarnestEntity::new).dimensions(EntityDimensions.fixed(0.6f, 2.9f)).build());

	static {
		FabricDefaultAttributeRegistry.register(EARNEST, LivingEntity.createLivingAttributes());
	}
}
