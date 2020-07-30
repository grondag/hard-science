package grondag.hs.client.gui;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import grondag.fermion.gui.AbstractSimpleScreen;
import grondag.fermion.gui.GuiUtil;
import grondag.fermion.gui.ScreenTheme;
import grondag.xm.api.paint.XmPaint;
import grondag.xm.api.paint.XmPaintFinder;
import grondag.xm.api.texture.TextureSet;
import grondag.xm.api.texture.XmTextures;
import grondag.xm.modelstate.AbstractPrimitiveModelState;

public class PaintScreen extends AbstractSimpleScreen {
	private static final int PREVIEW_SIZE = 80;
	private static final int TEXTURE_SIZE = 40;
	private static final int MARGIN = 4;
	private static final int SPACING = 8;

	protected ColorPicker colorPicker;
	protected ModelPreview modelPreview;
	protected TexturePicker texturePicker;
	protected ItemStack stack;

	private int selectedLayer =  0;

	protected LayerSelector[] layers = new LayerSelector[3];

	protected final XmPaintFinder finder = XmPaint.finder();

	public PaintScreen(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	protected void computeScreenBounds() {
		screenWidth = MARGIN + PREVIEW_SIZE + SPACING + (TEXTURE_SIZE + MARGIN) * 5 + MARGIN + ScreenTheme.current().tabMargin + ScreenTheme.current().tabWidth + MARGIN;
		screenLeft = (width - screenWidth) / 2;
		screenHeight = MARGIN + PREVIEW_SIZE + SPACING + (TEXTURE_SIZE + MARGIN) * 3 + MARGIN;
		screenTop = (height - screenHeight) / 2;
	}

	@Override
	protected void drawControls(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		GuiUtil.drawRect(screenLeft, screenTop, screenLeft + screenWidth, screenTop + screenHeight, 0xFF202020);

		modelPreview.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		colorPicker.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		texturePicker.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		layers[0].drawControl(matrixStack, mouseX, mouseY, partialTicks);
		layers[1].drawControl(matrixStack, mouseX, mouseY, partialTicks);
		layers[2].drawControl(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void addControls() {
		modelPreview = new ModelPreview(this);
		modelPreview.setLeft(screenLeft + MARGIN);
		modelPreview.setTop(screenTop + MARGIN);
		modelPreview.setWidth(PREVIEW_SIZE);
		modelPreview.setHeight(PREVIEW_SIZE);
		modelPreview.setStack(stack);

		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

		colorPicker = new ColorPicker(this);
		colorPicker.setLeft(screenLeft + MARGIN + PREVIEW_SIZE + SPACING);
		colorPicker.setTop(screenTop + MARGIN);
		colorPicker.setWidth(screenWidth - MARGIN - PREVIEW_SIZE - SPACING - MARGIN);
		colorPicker.setHeight(PREVIEW_SIZE);
		colorPicker.onChange(this::updateColor);
		colorPicker.setRgb(modelState.paint(0).textureColor(0));

		texturePicker = new TexturePicker(this);
		texturePicker.setLeft(screenLeft + MARGIN + PREVIEW_SIZE + SPACING);
		texturePicker.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING);
		texturePicker.setWidth(screenWidth - MARGIN - PREVIEW_SIZE - SPACING - MARGIN);
		texturePicker.setHeight(screenHeight - MARGIN - PREVIEW_SIZE - SPACING - MARGIN);
		texturePicker.setItemSize(TEXTURE_SIZE);
		texturePicker.setItemSpacing(MARGIN);
		texturePicker.setItemsPerRow();
		texturePicker.onChanged(this::updateTexture);
		texturePicker.setSelected(modelState.paint(0).texture(0));

		final int depth = modelState.paint(0).textureDepth();

		final LayerSelector layer0 = new LayerSelector(this);
		layer0.setLeft(screenLeft + MARGIN);
		layer0.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING);
		layer0.setWidth(PREVIEW_SIZE);
		layer0.setHeight(TEXTURE_SIZE + MARGIN);
		layer0.setItemSize(TEXTURE_SIZE);
		layer0.setItemSpacing(MARGIN);
		layer0.onSelected(t -> updateLayer(0, t));
		layer0.setTexture(modelState.paint(0).texture(0));
		layer0.setRgb(modelState.paint(0).textureColor(0));
		layer0.setClearable(false);
		layers[0] = layer0;

		final LayerSelector layer1 = new LayerSelector(this);
		layer1.setLeft(screenLeft + MARGIN);
		layer1.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING + TEXTURE_SIZE + MARGIN);
		layer1.setWidth(PREVIEW_SIZE);
		layer1.setHeight(TEXTURE_SIZE + MARGIN);
		layer1.setItemSize(TEXTURE_SIZE);
		layer1.setItemSpacing(MARGIN);
		layer1.onSelected(t -> updateLayer(1, t));
		layers[1] = layer1;

		if (depth > 1) {
			layer1.setTexture(modelState.paint(0).texture(1));
			layer1.setRgb(modelState.paint(0).textureColor(1));
		}

		final LayerSelector layer2 = new LayerSelector(this);
		layer2.setLeft(screenLeft + MARGIN);
		layer2.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING + TEXTURE_SIZE + MARGIN + TEXTURE_SIZE + MARGIN);
		layer2.setWidth(PREVIEW_SIZE);
		layer2.setHeight(TEXTURE_SIZE + MARGIN);
		layer2.setItemSize(TEXTURE_SIZE);
		layer2.setItemSpacing(MARGIN);
		layer2.onSelected(t -> updateLayer(2, t));
		layers[2] = layer2;

		if (depth == 3) {
			layer2.setTexture(modelState.paint(0).texture(2));
			layer2.setRgb(modelState.paint(0).textureColor(2));
		}

		children.add(modelPreview);
		children.add(colorPicker);
		children.add(texturePicker);
		children.add(layer0);
		children.add(layer1);
		children.add(layer2);
	}

	private void updateColor(int rgb) {
		layers[selectedLayer].setRgb(rgb);
		texturePicker.setRgb(rgb);
		finder.clear();
		finder.copy(modelPreview.modelState().paint(0));
		finder.textureColor(selectedLayer, ColorUtil.rbgLinearToSrgb(rgb));
		modelPreview.modelState().paint(0, finder.find());
		modelPreview.setModelDirty();
	}

	private void updateTexture(TextureSet tex) {
		layers[selectedLayer].setTexture(tex);
		updateModelStateTextures();
	}

	private void updateLayer(int index, TextureSet tex) {
		layers[selectedLayer].setSelected(false);
		selectedLayer = index;
		layers[index].setSelected(true);
		colorPicker.setRgb(layers[index].getRgb());
		texturePicker.setRgb(layers[index].getRgb());

		if (tex == null) {
			texturePicker.setSelectedIndex(TexturePicker.NO_SELECTION);
			updateModelStateTextures();
		} else {
			texturePicker.setSelected(tex);
		}
	}

	private void updateModelStateTextures() {
		TextureSet tex0 = layers[0].getTexture();

		if (tex0 == null) {
			tex0 = XmTextures.TILE_NOISE_SUBTLE;
			layers[0].setTexture(tex0);
		}

		final TextureSet tex1 = layers[1].getTexture();
		final TextureSet tex2 = layers[2].getTexture();

		int depth = 1;

		if (tex1 != null) {
			++depth;
		}

		if (tex2 != null) {
			++depth;
		}

		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

		finder.clear();
		finder.copy(modelState.paint(0));
		finder.texture(0, tex0);

		finder.textureDepth(depth);

		if (depth > 1)  {
			finder.texture(1, tex1 == null ? tex2 : tex1);

			if (depth == 3) {
				finder.texture(2, tex2);
			}
		}

		modelState.paint(0, finder.find());
		modelPreview.setModelDirty();
	}
}
