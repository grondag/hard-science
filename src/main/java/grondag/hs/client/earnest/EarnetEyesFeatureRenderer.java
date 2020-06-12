package grondag.hs.client.earnest;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import grondag.hs.entity.EarnestEntity;

@Environment(EnvType.CLIENT)
public class EarnetEyesFeatureRenderer extends FeatureRenderer<EarnestEntity, EarnestEntityModel> {
	private static final Identifier EYES = new Identifier("hard-science:textures/entity/earnest/earnest_eyes.png");

	public EarnetEyesFeatureRenderer(FeatureRendererContext<EarnestEntity, EarnestEntityModel> featureRendererContext) {
		super(featureRendererContext);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EarnestEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		renderModel(getContextModel(), getTexture(entity), matrices, vertexConsumers, light, entity, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public Identifier getTexture(EarnestEntity entity) {
		return EYES;
	}
}
