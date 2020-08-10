package grondag.hs.init;

import static grondag.fermion.color.ColorUtil.NO_COLOR;
import static grondag.fermion.color.ColorUtil.hclToSrgb;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import grondag.fermion.color.Color;

public class HsColors {
	private HsColors() {}

	// UGLY: encapsulate
	public static final Int2IntOpenHashMap RGB_TO_HCL = new Int2IntOpenHashMap();
	public static final Int2IntOpenHashMap HCL_TO_RGB = new Int2IntOpenHashMap();

	public static final int CHROMA_SLICE_SIZE = 2;
	public static final int CHROMA_SLICE_COUNT = 51;

	public static final int LUMA_SLICE_SIZE = 4;
	public static final int LUMA_SLICE_COUNT = 26;

	public static final int ARC_DEGREES = 5;

	public static final int[] HUE_COLORS = new int[360 / ARC_DEGREES + 2];

	public static final int DEFAULT_WHITE_HCL = 96 << 24;
	public static final int DEFAULT_WHITE_RGB = hclToSrgb(0, 0, 96);


	static {
		for (int hIndex = 0; hIndex < HUE_COLORS.length; ++hIndex) {
			HUE_COLORS[hIndex] = Color.fromHCL(hIndex * ARC_DEGREES, Color.HCL_MAX, Color.HCL_MAX).ARGB | 0xFF000000;
		}

		for (int h = 0; h < 360; ++h) {
			for (int lum = 0; lum < LUMA_SLICE_COUNT; ++lum) {
				for (int chr = 0; chr < CHROMA_SLICE_COUNT; ++chr) {
					final int c = chr * CHROMA_SLICE_SIZE;
					final int l = lum * LUMA_SLICE_SIZE;

					final int rgb = hclToSrgb(h, c, l);

					if (rgb != NO_COLOR) {
						final int hcl = h | (c << 16) | (l << 24);

						RGB_TO_HCL.put(rgb, hcl);
						HCL_TO_RGB.put(hcl, rgb);
					}
				}
			}
		}

		assert DEFAULT_WHITE_RGB == HCL_TO_RGB.get(DEFAULT_WHITE_HCL);
	}

}
