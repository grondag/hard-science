package grondag.hs.client.gui;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

import grondag.fermion.gui.AbstractSimpleScreen;
import grondag.fermion.gui.GuiUtil;
import grondag.fermion.gui.ScreenTheme;
import grondag.fermion.gui.control.Button;
import grondag.fermion.gui.control.Slider;
import grondag.fermion.gui.control.Toggle;
import grondag.hs.init.HsColors;
import grondag.hs.packet.c2s.UpdateStackPaintC2S;
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
	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = 20;

	protected ColorPicker colorPicker;
	protected ModelPreview modelPreview;
	protected TexturePicker texturePicker;
	protected Toggle aoToggle;
	protected Toggle diffuseToggle;
	protected Toggle emissiveToggle;
	protected Slider alphaSlider;

	protected final ItemStack stack;
	protected final Hand hand;

	private int selectedLayer =  0;

	protected LayerSelector[] layers = new LayerSelector[3];

	protected final XmPaintFinder finder = XmPaint.finder();

	public PaintScreen(ItemStack stack, Hand hand) {
		this.stack = stack;
		this.hand = hand;
	}

	@Override
	protected void computeScreenBounds() {
		screenWidth = MARGIN + PREVIEW_SIZE + SPACING + (TEXTURE_SIZE + MARGIN) * 5 + MARGIN + ScreenTheme.current().tabMargin + ScreenTheme.current().tabWidth + MARGIN;
		screenLeft = (width - screenWidth) / 2;
		screenHeight = MARGIN + PREVIEW_SIZE + SPACING + (TEXTURE_SIZE + MARGIN) * 3 + MARGIN + BUTTON_HEIGHT + MARGIN;
		screenTop = (height - screenHeight) / 2;
	}

	@Override
	public void renderBackground(MatrixStack matrices) {
		super.renderBackground(matrices);
		GuiUtil.drawRect(screenLeft, screenTop, screenLeft + screenWidth, screenTop + screenHeight, 0xFF202020);
	}

	@Override
	public void addControls() {
		modelPreview = new ModelPreview(this);
		modelPreview.setLeft(screenLeft + MARGIN);
		modelPreview.setTop(screenTop + MARGIN);
		modelPreview.setWidth(PREVIEW_SIZE);
		modelPreview.setHeight(PREVIEW_SIZE);
		modelPreview.setStack(stack);
		children.add(modelPreview);

		// NB: need to come before color picker in child list to get mouse events
		aoToggle = new Toggle(this);
		// TODO: localize, also below
		aoToggle.setLabel(new LiteralText("AO"));
		aoToggle.setLeft(screenLeft + screenWidth - MARGIN - 65);
		aoToggle.setTop(screenTop + MARGIN + BUTTON_HEIGHT);
		aoToggle.setHeight(12);
		aoToggle.setWidth(30);
		aoToggle.onChanged(this::updateAo);
		children.add(aoToggle);

		diffuseToggle = new Toggle(this);
		diffuseToggle.setLabel(new LiteralText("Diffuse"));
		diffuseToggle.setLeft(screenLeft + screenWidth - MARGIN - 65);
		diffuseToggle.setTop(screenTop + MARGIN + BUTTON_HEIGHT + BUTTON_HEIGHT);
		diffuseToggle.setHeight(12);
		diffuseToggle.setWidth(30);
		diffuseToggle.onChanged(this::updateDiffuse);
		children.add(diffuseToggle);

		emissiveToggle = new Toggle(this);
		emissiveToggle.setLabel(new LiteralText("Emissive"));
		emissiveToggle.setLeft(screenLeft + screenWidth - MARGIN - 65);
		emissiveToggle.setTop(screenTop + MARGIN + BUTTON_HEIGHT + BUTTON_HEIGHT + BUTTON_HEIGHT);
		emissiveToggle.setHeight(12);
		emissiveToggle.setWidth(30);
		emissiveToggle.onChanged(this::updateEmissive);
		children.add(emissiveToggle);

		colorPicker = new ColorPicker(this);
		colorPicker.setLeft(screenLeft + MARGIN + PREVIEW_SIZE + SPACING);
		colorPicker.setTop(screenTop + MARGIN);
		colorPicker.setWidth(screenWidth - MARGIN - PREVIEW_SIZE - SPACING - MARGIN);
		colorPicker.setHeight(PREVIEW_SIZE);
		colorPicker.onChange(this::updateColor);
		children.add(colorPicker);

		texturePicker = new TexturePicker(this);
		texturePicker.setLeft(screenLeft + MARGIN + PREVIEW_SIZE + SPACING);
		texturePicker.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING);
		texturePicker.setWidth(screenWidth - MARGIN - PREVIEW_SIZE - SPACING - MARGIN);
		texturePicker.setHeight(screenHeight - MARGIN - PREVIEW_SIZE - SPACING - MARGIN - theme.singleLineWidgetHeight - MARGIN);
		texturePicker.setItemSize(TEXTURE_SIZE);
		texturePicker.setItemSpacing(MARGIN);
		texturePicker.setItemsPerRow();
		texturePicker.onChanged(this::updateTexture);
		children.add(texturePicker);

		final LayerSelector layer0 = new LayerSelector(this);
		layer0.setLeft(screenLeft + MARGIN);
		layer0.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING);
		layer0.setWidth(PREVIEW_SIZE);
		layer0.setHeight(TEXTURE_SIZE + MARGIN);
		layer0.setItemSize(TEXTURE_SIZE);
		layer0.setItemSpacing(MARGIN);
		layer0.onAction(a -> updateLayer(0, a));
		layer0.setClearable(false);
		layer0.setSelected(true);
		layers[0] = layer0;
		children.add(layer0);

		final LayerSelector layer1 = new LayerSelector(this);
		layer1.setLeft(screenLeft + MARGIN);
		layer1.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING + TEXTURE_SIZE + MARGIN);
		layer1.setWidth(PREVIEW_SIZE);
		layer1.setHeight(TEXTURE_SIZE + MARGIN);
		layer1.setItemSize(TEXTURE_SIZE);
		layer1.setItemSpacing(MARGIN);
		layer1.onAction(a -> updateLayer(1, a));
		layers[1] = layer1;
		children.add(layer1);


		final LayerSelector layer2 = new LayerSelector(this);
		layer2.setLeft(screenLeft + MARGIN);
		layer2.setTop(screenTop + MARGIN + PREVIEW_SIZE + SPACING + TEXTURE_SIZE + MARGIN + TEXTURE_SIZE + MARGIN);
		layer2.setWidth(PREVIEW_SIZE);
		layer2.setHeight(TEXTURE_SIZE + MARGIN);
		layer2.setItemSize(TEXTURE_SIZE);
		layer2.setItemSpacing(MARGIN);
		layer2.onAction(a -> updateLayer(2, a));
		layers[2] = layer2;
		children.add(layer2);

		final int buttonX = screenLeft + screenWidth - MARGIN - BUTTON_WIDTH;
		final int buttonY = screenTop + screenHeight - MARGIN - BUTTON_HEIGHT;

		final Button done = new Button(this, buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT, new TranslatableText("gui.done")) {
			@Override
			public void onClick(double d, double e) {
				UpdateStackPaintC2S.send(modelPreview.modelState(), hand);
				PaintScreen.this.onClose();
			}
		};

		this.addButton(done);

		alphaSlider = new Slider(this, 256, new LiteralText("Alpha"), 0.15f);
		alphaSlider.setLeft(screenLeft + MARGIN);
		alphaSlider.setTop(buttonY + MARGIN);
		alphaSlider.setHeight(12);
		alphaSlider.setWidth(256);
		alphaSlider.onChanged(this::updateAlpha);
		children.add(alphaSlider);

		readMaterial();
	}

	private void updateColor(int rgb) {
		final XmPaint paint = modelPreview.modelState().paint(0);
		finder.clear();
		finder.copy(paint);
		finder.textureColor(selectedLayer, (paint.textureColor(selectedLayer) & 0xFF000000) | (rgb & 0xFFFFFF));
		modelPreview.modelState().paint(0, finder.find());
		modelPreview.setModelDirty();
		readMaterial();
	}

	private void updateTexture(TextureSet tex) {
		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();
		finder.clear();
		finder.copy(modelState.paint(0));
		finder.texture(selectedLayer, tex);
		modelState.paint(0, finder.find());
		modelPreview.setModelDirty();
		readMaterial();
	}

	private void updateLayer(int index, LayerSelector.Action action) {
		switch(action) {
		case CLEAR: {
			final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

			if (index > 0 && index == modelState.paint(0).textureDepth() - 1) {
				finder.clear();
				finder.copy(modelState.paint(0));
				finder.textureDepth(index);
				modelState.paint(0, finder.find());
				modelPreview.setModelDirty();
			}

			if (index == selectedLayer) {
				layers[index].setSelected(false);
				--selectedLayer;
				layers[selectedLayer].setSelected(true);
			}

			readMaterial();
			break;
		}

		case CREATE: {
			final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

			layers[selectedLayer].setSelected(false);
			selectedLayer = index;
			layers[index].setSelected(true);
			finder.clear();
			finder.copy(modelState.paint(0));
			finder.textureDepth(index + 1);
			finder.texture(index, XmTextures.TILE_NOISE_SUBTLE);
			finder.textureColor(index, HsColors.DEFAULT_WHITE_RGB);
			modelState.paint(0, finder.find());
			modelPreview.setModelDirty();
			readMaterial();
			break;
		}

		default:
		case SELECT:
			layers[selectedLayer].setSelected(false);
			selectedLayer = index;
			layers[index].setSelected(true);
			readMaterial();
			break;
		}
	}

	private void readMaterial() {
		final XmPaint paint = modelPreview.modelState().paint(0);
		final int depth = paint.textureDepth();

		for (int i = 0; i < depth; ++i) {
			layers[i].setTexture(paint.texture(i));
			layers[i].setRgb(paint.textureColor(i));
		}

		layers[1].setClearable(depth == 2);
		layers[2].setVisible(depth > 1);
		layers[2].setClearable(depth == 3);

		if (selectedLayer < depth) {
			colorPicker.setRgb(paint.textureColor(selectedLayer) | 0xFF000000);
			texturePicker.setRgb(paint.textureColor(selectedLayer));
			aoToggle.setOn(!paint.disableAo(selectedLayer));
			diffuseToggle.setOn(!paint.disableDiffuse(selectedLayer));
			emissiveToggle.setOn(paint.emissive(selectedLayer));
			alphaSlider.setSelectedIndex(paint.textureColor(selectedLayer) >>> 24);
		} else {
			texturePicker.setRgb(-1);
			colorPicker.setRgb(-1);
			aoToggle.setOn(true);
			diffuseToggle.setOn(true);
			emissiveToggle.setOn(false);
			alphaSlider.setSelectedIndex(255);
			assert false : "Invalid state in PaintScreen - selected layer index greater than depth";
		}
	}

	private void updateAo(boolean hasAo) {
		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

		if (selectedLayer < modelState.paint(0).textureDepth()) {
			finder.clear();
			finder.copy(modelState.paint(0));
			finder.disableAo(selectedLayer, !hasAo);
			modelState.paint(0, finder.find());
			modelPreview.setModelDirty();
			readMaterial();
		}
	}

	private void updateDiffuse(boolean hasDiffuse) {
		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

		if (selectedLayer < modelState.paint(0).textureDepth()) {
			finder.clear();
			finder.copy(modelState.paint(0));
			finder.disableDiffuse(selectedLayer, !hasDiffuse);
			modelState.paint(0, finder.find());
			modelPreview.setModelDirty();
			readMaterial();
		}
	}

	private void updateEmissive(boolean isEmissive) {
		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

		if (selectedLayer < modelState.paint(0).textureDepth()) {
			finder.clear();
			finder.copy(modelState.paint(0));
			finder.emissive(selectedLayer, isEmissive);
			modelState.paint(0, finder.find());
			modelPreview.setModelDirty();
			readMaterial();
		}
	}

	private void updateAlpha(int alpha) {
		final AbstractPrimitiveModelState<?, ?, ?> modelState = modelPreview.modelState();

		if (selectedLayer < modelState.paint(0).textureDepth()) {
			finder.clear();
			finder.copy(modelState.paint(0));
			final int oldColor = modelState.paint(0).textureColor(selectedLayer);
			finder.textureColor(selectedLayer, ((alpha & 0xFF) << 24) | (oldColor & 0xFFFFFF));
			modelState.paint(0, finder.find());
			modelPreview.setModelDirty();
			readMaterial();
		}
	}
}
