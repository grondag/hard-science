package grondag.hs.client.gui;

import net.minecraft.client.util.math.MatrixStack;

import grondag.fermion.gui.AbstractSimpleScreen;
import grondag.fermion.gui.GuiUtil;
import grondag.fermion.gui.ScreenTheme;
import grondag.hs.block.HsBlocks;
import grondag.xm.api.paint.XmPaint;
import grondag.xm.api.paint.XmPaintFinder;
import grondag.xm.api.texture.TextureSet;

public class PaintScreen extends AbstractSimpleScreen {
	private static final int PREVIEW_SIZE = 80;
	private static final int LAYER_SELECTOR_SIZE = 60;
	private static final int TEXTURE_SIZE = 40;
	private static final int MARGIN = 4;
	private static final int SPACING = 8;

	protected ColorPicker colorPicker;
	protected ModelPreview modelPreview;
	protected TexturePicker texturePicker;

	protected final XmPaintFinder finder = XmPaint.finder();

	@Override
	protected void computeScreenBounds() {
		screenWidth = MARGIN + PREVIEW_SIZE + SPACING + (TEXTURE_SIZE + MARGIN) * 5 + MARGIN + ScreenTheme.current().tabMargin + ScreenTheme.current().tabWidth + MARGIN;
		screenLeft = (width - screenWidth) / 2;
		screenHeight = screenWidth * 1000 / 1618;
		screenTop = (height - screenHeight) / 2;
	}

	@Override
	protected void drawControls(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		GuiUtil.drawRect(screenLeft, screenTop, screenLeft + screenWidth, screenTop + screenHeight, 0xFF202020);

		modelPreview.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		colorPicker.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		texturePicker.drawControl(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void addControls() {
		modelPreview = new ModelPreview(this);
		modelPreview.setLeft(screenLeft + MARGIN);
		modelPreview.setTop(screenTop + MARGIN);
		modelPreview.setWidth(PREVIEW_SIZE);
		modelPreview.setHeight(PREVIEW_SIZE);
		modelPreview.setStack(HsBlocks.DURACRETE_CUBE_ITEM.getStackForRender());

		colorPicker = new ColorPicker(this);
		colorPicker.setLeft(screenLeft + MARGIN);
		colorPicker.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING);
		colorPicker.setWidth(PREVIEW_SIZE);
		colorPicker.setHeight(PREVIEW_SIZE);
		colorPicker.onChange(this::updateColor);

		texturePicker = new TexturePicker(this);
		texturePicker.setLeft(screenLeft + MARGIN + PREVIEW_SIZE + SPACING);
		texturePicker.setTop(screenTop + MARGIN + LAYER_SELECTOR_SIZE + MARGIN);
		texturePicker.setWidth(screenWidth - MARGIN - PREVIEW_SIZE - SPACING - MARGIN);
		texturePicker.setHeight(screenHeight - MARGIN - 50 - SPACING - MARGIN);
		texturePicker.setItemSize(TEXTURE_SIZE);
		texturePicker.setItemSpacing(MARGIN);
		texturePicker.setItemsPerRow();
		texturePicker.onChanged(this::updateTexture);

		children.add(modelPreview);
		children.add(colorPicker);
		children.add(texturePicker);
	}

	private void updateColor(int rgb) {
		finder.clear();
		finder.copy(modelPreview.modelState().paint(0));
		finder.textureColor(0, rgb);
		modelPreview.modelState().paint(0, finder.find());
		modelPreview.setModelDirty();
	}

	private void updateTexture(TextureSet tex) {
		finder.clear();
		finder.copy(modelPreview.modelState().paint(0));
		finder.texture(0, tex);
		modelPreview.modelState().paint(0, finder.find());
		modelPreview.setModelDirty();
	}
}
