package grondag.hs.client.gui;

import net.minecraft.client.util.math.MatrixStack;

import grondag.fermion.gui.AbstractSimpleScreen;
import grondag.fermion.gui.GuiUtil;

public class PaintScreen extends AbstractSimpleScreen {
	protected ColorPicker colorPicker;

	@Override
	protected void computeScreenBounds() {
		screenHeight = height - 40;
		screenTop = 20;
		screenWidth = width - 40;
		screenLeft = 20;
	}

	@Override
	protected void drawControls(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		colorPicker.drawControl(matrixStack, mouseX, mouseY, partialTicks);

		GuiUtil.drawRect(screenLeft + 200, screenTop, screenLeft + 100, screenTop + 100, colorPicker.getRgb());
	}

	@Override
	public void addControls() {
		colorPicker = new ColorPicker(this);

		colorPicker.setLeft(screenLeft);
		colorPicker.setTop(screenTop);
		colorPicker.setWidth(100);
		colorPicker.setHeight(100);

		children.add(colorPicker);
	}
}
