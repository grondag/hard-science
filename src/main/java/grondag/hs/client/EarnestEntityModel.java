package grondag.hs.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import grondag.hs.entity.EarnestEntity;

@Environment(EnvType.CLIENT)
public class EarnestEntityModel extends BipedEntityModel<EarnestEntity> {
	public boolean carryingBlock;
	public boolean angry;

	public EarnestEntityModel(float f) {
		super(0.0F, -14.0F, 64, 32);
		helmet = new ModelPart(this, 0, 16);
		helmet.addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, f - 0.5F);
		helmet.setPivot(0.0F, -14.0F, 0.0F);
		torso = new ModelPart(this, 32, 16);
		torso.addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, f);
		torso.setPivot(0.0F, -14.0F, 0.0F);
		rightArm = new ModelPart(this, 56, 0);
		rightArm.addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, f);
		rightArm.setPivot(-3.0F, -12.0F, 0.0F);
		leftArm = new ModelPart(this, 56, 0);
		leftArm.mirror = true;
		leftArm.addCuboid(-1.0F, -2.0F, -1.0F, 2.0F, 30.0F, 2.0F, f);
		leftArm.setPivot(5.0F, -12.0F, 0.0F);
		rightLeg = new ModelPart(this, 56, 0);
		rightLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, f);
		rightLeg.setPivot(-2.0F, -2.0F, 0.0F);
		leftLeg = new ModelPart(this, 56, 0);
		leftLeg.mirror = true;
		leftLeg.addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, f);
		leftLeg.setPivot(2.0F, -2.0F, 0.0F);
	}

	@Override
	public void setAngles(EarnestEntity livingEntity, float f, float g, float h, float i, float j) {
		super.setAngles(livingEntity, f, g, h, i, j);
		head.visible = true;
		torso.pitch = 0.0F;
		torso.pivotY = -14.0F;
		torso.pivotZ = -0.0F;
		ModelPart var10000 = rightLeg;
		var10000.pitch -= 0.0F;
		var10000 = leftLeg;
		var10000.pitch -= 0.0F;
		var10000 = rightArm;
		var10000.pitch = (float)(var10000.pitch * 0.5D);
		var10000 = leftArm;
		var10000.pitch = (float)(var10000.pitch * 0.5D);
		var10000 = rightLeg;
		var10000.pitch = (float)(var10000.pitch * 0.5D);
		var10000 = leftLeg;
		var10000.pitch = (float)(var10000.pitch * 0.5D);
		if (rightArm.pitch > 0.4F) {
			rightArm.pitch = 0.4F;
		}

		if (leftArm.pitch > 0.4F) {
			leftArm.pitch = 0.4F;
		}

		if (rightArm.pitch < -0.4F) {
			rightArm.pitch = -0.4F;
		}

		if (leftArm.pitch < -0.4F) {
			leftArm.pitch = -0.4F;
		}

		if (rightLeg.pitch > 0.4F) {
			rightLeg.pitch = 0.4F;
		}

		if (leftLeg.pitch > 0.4F) {
			leftLeg.pitch = 0.4F;
		}

		if (rightLeg.pitch < -0.4F) {
			rightLeg.pitch = -0.4F;
		}

		if (leftLeg.pitch < -0.4F) {
			leftLeg.pitch = -0.4F;
		}

		if (carryingBlock) {
			rightArm.pitch = -0.5F;
			leftArm.pitch = -0.5F;
			rightArm.roll = 0.05F;
			leftArm.roll = -0.05F;
		}

		rightArm.pivotZ = 0.0F;
		leftArm.pivotZ = 0.0F;
		rightLeg.pivotZ = 0.0F;
		leftLeg.pivotZ = 0.0F;
		rightLeg.pivotY = -5.0F;
		leftLeg.pivotY = -5.0F;
		head.pivotZ = -0.0F;
		head.pivotY = -14.0F;
		helmet.pivotX = head.pivotX;
		helmet.pivotY = head.pivotY;
		helmet.pivotZ = head.pivotZ;
		helmet.pitch = head.pitch;
		helmet.yaw = head.yaw;
		helmet.roll = head.roll;

		if (angry) {
			head.pivotY -= 5.0F;
		}

		rightArm.setPivot(-5.0F, -12.0F, 0.0F);
		leftArm.setPivot(5.0F, -12.0F, 0.0F);
	}
}
