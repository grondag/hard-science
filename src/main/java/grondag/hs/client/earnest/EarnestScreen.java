package grondag.hs.client.earnest;

import net.minecraft.client.util.math.MatrixStack;

import grondag.fermion.gui.AbstractSimpleScreen;
import grondag.fermion.gui.GuiUtil;
import grondag.fonthack.FontHackClient;
import grondag.hs.client.gui.ActionControl;
import grondag.hs.client.gui.JournalControl;
import grondag.hs.packet.c2s.EarnestDialogC2S;

public class EarnestScreen extends AbstractSimpleScreen {
	private JournalControl journal;
	private ActionControl actions;

	@Override
	public void init() {
		textRenderer = FontHackClient.getTextRenderer(FontHackClient.READING_FONT);
		super.init();
	}

	@Override
	protected void computeScreenBounds() {
		screenHeight = height;
		screenTop = 0;
		screenWidth = width;
		screenLeft = 0;
	}

	@Override
	protected void drawControls(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		journal.drawControl(matrixStack, mouseX, mouseY, partialTicks);
		actions.drawControl(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void addControls() {
		final int boxWidth = Math.max(100, width / 3);
		final int boxHeight = Math.max(120, height / 2);
		final int halfBoxHeight = boxHeight / 2;
		final int off = Math.max(20, width / 8);
		final int cx = screenWidth() / 2;
		final int cy = screenHeight() / 2;

		journal = new JournalControl(this, FontHackClient.READING_FONT);
		journal.setLeft(cx - off - boxWidth);
		journal.setTop(cy - halfBoxHeight);
		journal.setWidth(boxWidth);
		journal.setHeight(boxHeight);

		actions = new ActionControl(this, FontHackClient.READING_FONT);
		actions.setLeft(cx + off);
		actions.setTop(cy - halfBoxHeight);
		actions.setWidth(boxWidth);
		actions.setHeight(boxHeight);

		children.add(journal);
		children.add(actions);
	}


	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// TODO Auto-generated method stub
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		super.onClose();
	}

	@Override
	public void removed() {
		EarnestDialogC2S.END.send(EarnestClientState.earnest, 0);
		super.removed();
	}

	@Override
	public void renderBackground(MatrixStack matrices) {

		final int color = theme.screenBackground;

		if (color != 0) {
			final int m = theme.externalMargin;
			GuiUtil.drawRect(journal.getLeft() - m, journal.getTop() - m, journal.getRight() + m, journal.getBottom() + m, color);
			GuiUtil.drawRect(actions.getLeft() - m, actions.getTop() - m, actions.getRight() + m, actions.getBottom() + m, color);
		}
		//super.renderBackground(matrices);
	}

	@Override
	public void renderBackground(MatrixStack matrices, int vOffset) {
		// NOOP
		//super.renderBackground(matrices, vOffset);
	}

	@Override
	public void renderBackgroundTexture(int vOffset) {
		// NOOP
		// super.renderBackgroundTexture(vOffset);
	}
}
