package grondag.hs.client.earnest;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import grondag.hs.entity.EarnestEntity;

@Environment(EnvType.CLIENT)
public class EarnestEntityRenderer extends LivingEntityRenderer<EarnestEntity, EarnestEntityModel> {
	private static final Identifier TEXTURE = new Identifier("hard-science:textures/entity/earnest/earnest.png");
	//	private final Random random = new Random();

	public EarnestEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new EarnestEntityModel(0.0F), 0.5F);
		addFeature(new EarnetEyesFeatureRenderer(this));
		addFeature(new EarnetClothesFeatureRenderer(this));
	}

	@Override
	public void render(EarnestEntity endermanEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(endermanEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Vec3d getPositionOffset(EarnestEntity endermanEntity, float f) {
		return super.getPositionOffset(endermanEntity, f);
	}

	@Override
	public Identifier getTexture(EarnestEntity endermanEntity) {
		return TEXTURE;
	}
}
