package grondag.hs.client.gui;

public class ColorUtil {
	private static final double K = 903.3;
	private static final double E = 0.008856;

	//NB: using standard illuminant E and outputting linear RGB - color correction to be applied in rendering
	private static final double ILLUMINANT = 100.0;

	public static final int NO_COLOR = 0;

	public static int labToRgb(double l, double a, double b) {
		final double y0 = (l + 16) / 116;
		final double z0 = y0 - b / 200;
		final double x0 = a / 500 + y0;

		final double x3 = x0 * x0 * x0;
		final double x1 = x3 > E ? x3 : (116 * x0 - 16) / K;

		final double y1 = l > K * E ? y0 * y0 * y0 : l / K;

		final double z3 = z0 * z0 * z0;

		final double z1 = z3 > E ? z3 : (116 * z0 - 16) / K;

		return xyzToRgb(x1 * ILLUMINANT, y1 * ILLUMINANT, z1 * ILLUMINANT);
	}

	public static int hclToRgb(double hue, double chroma, double luminance) {
		return labToRgb(luminance, Math.cos(Math.toRadians(hue)) * chroma, Math.sin(Math.toRadians(hue)) * chroma);
	}

	/**
	 * If color is visible, alpha btye = 255
	 */
	public static int xyzToRgb(double x, double y, double z) {
		if (!(x >= 0 && x <= ILLUMINANT && y >= 0 && y <= ILLUMINANT && z >= 0 && z <= ILLUMINANT)) {
			return NO_COLOR;
		} else {
			// Convert to sRGB
			final double x0 = x / 100;
			final double y0 = y / 100;
			final double z0 = z / 100;

			// CIE RGB inverse matrix, per http://www.brucelindbloom.com
			final double r0 = x0 *  2.3706743 + y0 * -0.9000405 + z0 * -0.4706338;
			final double g0 = x0 * -0.5138850 + y0 *  1.4253036 + z0 *  0.0885814;
			final double b0 = x0 *  0.0052982 + y0 * -0.0146949 + z0 *  1.0093968;

			final int red = (int) Math.round(r0 * 255);
			final int green = (int) Math.round(g0 * 255);
			final int blue = (int) Math.round(b0 * 255);

			if ((red & 0xFF) == red && (green & 0xFF) == green && (blue & 0xFF) == blue) {
				return 0xFF000000 | (red << 16) | (green << 8) | blue;
			} else {
				return NO_COLOR;
			}
		}
	}

	public static int rbgLinearToSrgb(int linearRgb) {
		final int a = linearRgb & 0xFF000000;
		double r = ((linearRgb >> 16) & 0xFF) / 255.0;
		double g = ((linearRgb >> 8) & 0xFF) / 255.0;
		double b = (linearRgb & 0xFF) / 255.0;

		r = r <= 0.0031308 ? 12.92 * r : Math.pow(1.055 * r, 1/2.4) - 0.055;
		g = g <= 0.0031308 ? 12.92 * g : Math.pow(1.055 * g, 1/2.4) - 0.055;
		b = b <= 0.0031308 ? 12.92 * b : Math.pow(1.055 * b, 1/2.4) - 0.055;

		return  a  | ((int) Math.round(r * 255) << 16) | ((int) Math.round(g * 255) << 8) | (int) Math.round(b * 255);
	}
}
