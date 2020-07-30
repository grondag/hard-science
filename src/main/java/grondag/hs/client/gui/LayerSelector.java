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

import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import grondag.fermion.gui.GuiUtil;
import grondag.fermion.gui.ScreenRenderContext;
import grondag.fermion.gui.ScreenTheme;
import grondag.fermion.gui.control.AbstractControl;
import grondag.xm.api.texture.TextureSet;
import grondag.xm.api.texture.tech.TechTextures;

@Environment(EnvType.CLIENT)
public class LayerSelector extends AbstractControl<LayerSelector> {
	protected int itemSize = ScreenTheme.current().itemSize;
	protected int itemSpacing = ScreenTheme.current().itemSpacing;
	protected int itemSelectionMargin = ScreenTheme.current().itemSelectionMargin;
	protected boolean isSelected = false;
	protected boolean isClearable = true;
	protected int rgb = -1;
	protected int srgb = -1;

	Consumer<TextureSet> onSelected = t -> {};

	protected TextureSet tex;

	protected enum MouseLocation {
		NONE, TEXTURE, CLEAR
	}

	protected MouseLocation currentMouseLocation;
	protected int currentMouseIndex;

	public LayerSelector(ScreenRenderContext renderContext) {
		super(renderContext);
	}

	public TextureSet getTexture() {
		return tex;
	}

	public void setTexture(TextureSet tex) {
		this.tex = tex;
	}

	@Override
	protected void drawContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		updateMouseLocation(mouseX, mouseY);
		RenderSystem.disableLighting();
		RenderSystem.disableDepthTest();

		if (isSelected || currentMouseLocation == MouseLocation.TEXTURE) {
			GuiUtil.drawBoxRightBottom(left, top, left + itemSelectionMargin + itemSize + itemSelectionMargin,
					bottom, 1, currentMouseLocation == MouseLocation.TEXTURE ? theme.buttonColorFocus : theme.buttonColorActive);
		}

		TextureSet tex = this.tex;

		final BufferBuilder buffer =  TextureUtil.setupRendering(renderContext);

		int color = srgb;


		if (tex == null) {
			tex = TechTextures.DECAL_PLUS;
			color = 0xFF50FF50;
		}

		TextureUtil.bufferTexture(buffer, left + itemSelectionMargin, top + itemSelectionMargin, itemSize, color, tex);
		TextureUtil.tearDownRendering();
	}

	@Override
	public final void drawToolTip(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		updateMouseLocation(mouseX, mouseY);

		if (currentMouseLocation == MouseLocation.TEXTURE && tex != null) {
			renderContext.drawLocalizedToolTip(matrixStack, tex.displayNameToken(), mouseX, mouseY);
		}
	}

	private void updateMouseLocation(double mouseX, double mouseY) {
		if (mouseX < left || mouseX > right || mouseY < top || mouseY > bottom) {
			currentMouseLocation = MouseLocation.NONE;
		} else if (mouseX <= left + itemSpacing + itemSize + itemSpacing) {
			currentMouseLocation = MouseLocation.TEXTURE;
		} else {
			currentMouseLocation = MouseLocation.CLEAR;
		}
	}

	@Override
	protected void handleCoordinateUpdate() {
		height = top + itemSelectionMargin * 2 + itemSize;
	}

	@Override
	protected void handleMouseClick(double mouseX, double mouseY, int clickedMouseButton) {
		updateMouseLocation(mouseX, mouseY);

		switch (currentMouseLocation) {
		case TEXTURE:
			onSelected.accept(tex);
			break;

		case CLEAR:
			tex = null;
			onSelected.accept(tex);
			break;

		case NONE:
		default:
			break;

		}
	}

	public void onSelected(Consumer<TextureSet> onSelected) {
		this.onSelected = onSelected;
	}

	@Override
	protected void handleMouseDrag(double mouseX, double mouseY, int clickedMouseButton, double dx, double dy) {
		// NOOP
	}

	@Override
	protected void handleMouseScroll(double mouseX, double mouseY, double scrollDelta) {
		// NOOP
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
		srgb = ColorUtil.rbgLinearToSrgb(rgb);
	}

	public int getRgb() {
		return rgb;
	}

	public void setItemSize(int itemSize) {
		this.itemSize = itemSize;
	}

	public void setItemSpacing(int itemSpacing) {
		this.itemSpacing = itemSpacing;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setClearable(boolean isClearable) {
		this.isClearable = isClearable;
	}
}
