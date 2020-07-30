package grondag.hs.client.gui;

import java.util.ArrayList;
import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL21;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;

import grondag.fermion.gui.ScreenRenderContext;
import grondag.fermion.gui.control.TabBar;
import grondag.xm.api.texture.TextureGroup;
import grondag.xm.api.texture.TextureRenderIntent;
import grondag.xm.api.texture.TextureSet;
import grondag.xm.api.texture.TextureSetRegistry;

public class TexturePicker extends TabBar<TextureSet> {
	protected Consumer<TextureSet> onChanged = t -> {};

	public TexturePicker(ScreenRenderContext renderContext) {
		super(renderContext, new ArrayList<TextureSet>());
		itemSize = 40;
		itemSpacing = 4;
		computeSpacing();
		setItemsPerRow(5);

		TextureSetRegistry.instance().forEach(t -> {
			if (t.renderIntent() != TextureRenderIntent.OVERLAY_ONLY && (t.textureGroupFlags() & TextureGroup.HIDDEN) == 0 && t.used()) {
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
		renderContext.minecraft().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
		renderContext.minecraft().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).setFilter(false, false);
		RenderSystem.enableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(GL21.GL_SMOOTH);
		RenderSystem.color4f(1, 1, 1, 1);

		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder vertexbuffer = tessellator.getBuffer();

		vertexbuffer.begin(GL21.GL_QUADS, VertexFormats.POSITION_TEXTURE);
	}

	@Override
	protected void tearDownItemRendering() {
		Tessellator.getInstance().draw();
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.shadeModel(GL21.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.disableTexture();
	}

	@Override
	protected void drawItem(MatrixStack matrixStack, TextureSet item, MinecraftClient mc, ItemRenderer itemRender, double left, double top, float partialTicks, boolean isHighlighted) {
		final BufferBuilder vertexbuffer = Tessellator.getInstance().getBuffer();

		final double bottom = top + itemSize;
		final double right = left + itemSize;
		final Sprite sprite = item.sampleSprite();

		final float u0 = sprite.getMinU();
		final float u1 = u0 + (sprite.getMaxU() - u0) / item.scale().sliceCount;

		final float v0 = sprite.getMinV();
		final float v1 = v0 + (sprite.getMaxV() - v0) / item.scale().sliceCount;

		vertexbuffer.vertex(left, bottom, 100.0D).texture(u0, v1).next();
		vertexbuffer.vertex(right, bottom, 100.0D).texture(u1, v1).next();
		vertexbuffer.vertex(right, top, 100.0D).texture(u1, v0).next();
		vertexbuffer.vertex(left, top, 100.0D).texture(u0, v0).next();
	}

	@Override
	public void setSelectedIndex(int index) {
		super.setSelectedIndex(index);

		if (index != NO_SELECTION && items != null) {
			onChanged.accept(items.get(index));
		}
	}
}
