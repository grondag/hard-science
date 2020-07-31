package grondag.hs.client.gui;

import java.util.ArrayList;
import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

import grondag.fermion.gui.ScreenRenderContext;
import grondag.fermion.gui.control.TabBar;
import grondag.xm.api.texture.TextureGroup;
import grondag.xm.api.texture.TextureSet;
import grondag.xm.api.texture.TextureSetRegistry;

public class TexturePicker extends TabBar<TextureSet> {
	protected Consumer<TextureSet> onChanged = t -> {};
	protected int rgb = -1;
	protected boolean notify = false;

	public TexturePicker(ScreenRenderContext renderContext) {
		super(renderContext, new ArrayList<TextureSet>());
		itemSize = 40;
		itemSpacing = 4;
		computeSpacing();
		setItemsPerRow(5);

		TextureSetRegistry.instance().forEach(t -> {
			if ((t.textureGroupFlags() & TextureGroup.HIDDEN) == 0 && t.used()) { //t.renderIntent() != TextureRenderIntent.OVERLAY_ONLY
				items.add(t);
			}
		});

		isDirty = true;
	}

	public void onChanged(Consumer<TextureSet> onChanged) {
		this.onChanged = onChanged;
	}

	@Override
	protected void drawItemToolTip(MatrixStack matrixStack, TextureSet item, ScreenRenderContext renderContext, int mouseX, int mouseY, float partialTicks) {
		renderContext.drawLocalizedToolTip(matrixStack, item.displayNameToken(), mouseX, mouseY);
	}

	@Override
	protected void setupItemRendering() {
		TextureUtil.setupRendering(renderContext);
	}

	@Override
	protected void tearDownItemRendering() {
		TextureUtil.tearDownRendering();
	}

	@Override
	protected void drawItem(MatrixStack matrixStack, TextureSet item, MinecraftClient mc, ItemRenderer itemRender, double left, double top, float partialTicks, boolean isHighlighted) {
		TextureUtil.bufferTexture(Tessellator.getInstance().getBuffer(), left, top, itemSize, rgb, item);
	}

	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);

		if (notify && index != NO_SELECTION && items != null) {
			onChanged.accept(items.get(index));
		}
	}

	@Override
	protected void handleMouseClick(double mouseX, double mouseY, int clickedMouseButton) {
		notify = true;
		super.handleMouseClick(mouseX, mouseY, clickedMouseButton);
		notify = false;
	}

	@Override
	protected void handleMouseDrag(double mouseX, double mouseY, int clickedMouseButton, double dx, double dy) {
		notify = true;
		super.handleMouseDrag(mouseX, mouseY, clickedMouseButton, dx, dy);
		notify = false;
	}

	@Override
	protected void handleMouseScroll(double mouseX, double mouseY, double scrollDelta) {
		notify = true;
		super.handleMouseScroll(mouseX, mouseY, scrollDelta);
		notify = false;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public int getRgb() {
		return rgb;
	}
}
