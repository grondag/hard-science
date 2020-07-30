package grondag.hs.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL21;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;

import grondag.fermion.gui.ScreenRenderContext;
import grondag.xm.api.texture.TextureSet;

public class TextureUtil {
	public static BufferBuilder setupRendering(ScreenRenderContext renderContext) {
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

		vertexbuffer.begin(GL21.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

		return vertexbuffer;
	}

	public static void tearDownRendering() {
		Tessellator.getInstance().draw();
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.shadeModel(GL21.GL_FLAT);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.disableTexture();
	}

	public static void bufferTexture(BufferBuilder vertexBuffer, double left, double top, int itemSize, int color, TextureSet item) {
		final double bottom = top + itemSize;
		final double right = left + itemSize;
		final Sprite sprite = item.sampleSprite();

		final float u0 = sprite.getMinU();
		final float u1 = u0 + (sprite.getMaxU() - u0) / item.scale().sliceCount;

		final float v0 = sprite.getMinV();
		final float v1 = v0 + (sprite.getMaxV() - v0) / item.scale().sliceCount;
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;

		vertexBuffer.vertex(left, bottom, 100.0D).texture(u0, v1).color(r, g, b, a).next();
		vertexBuffer.vertex(right, bottom, 100.0D).texture(u1, v1).color(r, g, b, a).next();
		vertexBuffer.vertex(right, top, 100.0D).texture(u1, v0).color(r, g, b, a).next();
		vertexBuffer.vertex(left, top, 100.0D).texture(u0, v0).color(r, g, b, a).next();
	}
}
