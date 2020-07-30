/*******************************************************************************
 * Copyright 2019 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package grondag.hs.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;

import grondag.fermion.client.models.SimpleModel;
import grondag.fermion.gui.GuiUtil;
import grondag.fermion.gui.ScreenRenderContext;
import grondag.fermion.gui.control.AbstractControl;
import grondag.xm.api.item.XmItem;
import grondag.xm.modelstate.AbstractPrimitiveModelState;
import grondag.xm.texture.TextureSetHelper;

@Environment(EnvType.CLIENT)
public class ModelPreview extends AbstractControl<ModelPreview> {
	public ModelPreview(ScreenRenderContext renderContext) {
		super(renderContext);
	}

	private ItemStack previewItem;
	private AbstractPrimitiveModelState<?, ?, ?> modelState;
	private final PreviewModel previewModel = new PreviewModel();

	private float contentLeft;
	private float contentTop;
	private float contentSize;

	@SuppressWarnings("resource")
	public void setStack(ItemStack stack) {
		previewItem = stack;

		if (modelState != null) {
			modelState.release();
		}

		modelState = XmItem.modelState(MinecraftClient.getInstance().world, stack);
	}

	public AbstractPrimitiveModelState<?, ?, ?> modelState() {
		return modelState;
	}

	public void setModelDirty() {
		if (modelState != null) {
			modelState.clearRendering();
		}

		previewModel.clearMesh();
	}

	@Override
	public void drawContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		if (previewItem != null) {
			GuiUtil.renderItemAndEffectIntoGui(renderContext, previewItem, previewModel, contentLeft, contentTop, contentSize);
		}
	}

	@Override
	protected void handleCoordinateUpdate() {
		contentSize = Math.min(width, height);
		contentLeft = left + (width - contentSize) / 2;
		contentTop = top + (height - contentSize) / 2;
	}

	@Override
	public void drawToolTip(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub

	}

	protected class PreviewModel extends SimpleModel {
		public PreviewModel() {
			super(TextureSetHelper.missingSprite(), ModelHelper.MODEL_TRANSFORM_BLOCK);
		}

		@Override
		protected Mesh createMesh() {
			return	modelState.mesh();
		}

		void clearMesh() {
			mesh = null;
		}
	}
}
