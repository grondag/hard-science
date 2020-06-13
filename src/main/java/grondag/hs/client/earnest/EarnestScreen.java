package grondag.hs.client.earnest;

import net.minecraft.client.util.math.MatrixStack;

import grondag.fermion.gui.AbstractSimpleScreen;
import grondag.fonthack.FontHackClient;
import grondag.hs.HardScience;
import grondag.mcmd.MarkdownControl;
import grondag.mcmd.MarkdownLoader;

public class EarnestScreen extends AbstractSimpleScreen {
	MarkdownControl mdLeft;
	MarkdownControl mdRight;

	@Override
	public void init() {
		textRenderer = FontHackClient.getTextRenderer(FontHackClient.READING_FONT);
		super.init();
	}

	@Override
	protected void drawControls(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		mdLeft.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		mdRight.drawControl(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void addControls() {
		mdLeft = new MarkdownControl(this, MarkdownLoader.get(HardScience.REG.id("sb_scale")), FontHackClient.READING_FONT);
		mdLeft.setLeft(screenLeft() + theme.externalMargin);
		mdLeft.setTop(screenTop() + theme.externalMargin);
		mdLeft.setWidth(screenWidth() - theme.externalMargin * 2);
		mdLeft.setHeight(screenHeight() - theme.externalMargin * 2);

		children.add(mdLeft);
	}
}
