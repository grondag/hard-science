package grondag.hs.client.earnest;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import grondag.hs.earnest.EarnestEntity;

@Environment(EnvType.CLIENT)
public class EarnetClothesFeatureRenderer extends FeatureRenderer<EarnestEntity, EarnestEntityModel> {
	private static Identifier ENBY = new Identifier("hard-science:textures/entity/earnest/earnest_shirt_enby.png");
	private static Identifier TATER = new Identifier("hard-science:textures/entity/earnest/earnest_shirt_tater.png");

	public EarnetClothesFeatureRenderer(FeatureRendererContext<EarnestEntity, EarnestEntityModel> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EarnestEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		final EarnestEntityModel model = getContextModel();
		model.leftLeg.visible = false;
		model.rightLeg.visible = false;
		renderModel(model, getTexture(entity), matrices, vertexConsumers, light, entity, 1.0F, 1.0F, 1.0F);
		model.leftLeg.visible = true;
		model.rightLeg.visible = true;
	}

	@Override
	public Identifier getTexture(EarnestEntity entity) {
		return TATER;
	}
}