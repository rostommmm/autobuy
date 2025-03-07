package neverbuy.util.render;

import java.awt.Color;

public final class ColorHelper {
   public static Color injectAlpha(Color color, int alpha) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
   }

   public static Color injectAlpha(Color color, float alpha) {
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255.0F));
   }

   public static Color getColor(int color) {
      int r = color >> 16 & 255;
      int g = color >> 8 & 255;
      int b = color & 255;
      int a = color >> 24 & 255;
      return new Color(r, g, b, a);
   }

   public static float[] getColorComps(Color color) {
      return new float[]{(float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F};
   }

   public static int gradient(int speed, int index, Color... colors) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      int colorIndex = (int)((float)angle / 360.0F * (float)colors.length);
      if (colorIndex == colors.length) {
         --colorIndex;
      }

      int color1 = colors[colorIndex].getRGB();
      int color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1].getRGB();
      return interpolateColor(color1, color2, (float)angle / 360.0F * (float)colors.length - (float)colorIndex);
   }

   public static int interpolateColor(int color1, int color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      int red1 = getRed(color1);
      int green1 = getGreen(color1);
      int blue1 = getBlue(color1);
      int alpha1 = getAlpha(color1);
      int red2 = getRed(color2);
      int green2 = getGreen(color2);
      int blue2 = getBlue(color2);
      int alpha2 = getAlpha(color2);
      int interpolatedRed = interpolateInt(red1, red2, (double)amount);
      int interpolatedGreen = interpolateInt(green1, green2, (double)amount);
      int interpolatedBlue = interpolateInt(blue1, blue2, (double)amount);
      int interpolatedAlpha = interpolateInt(alpha1, alpha2, (double)amount);
      return interpolatedAlpha << 24 | interpolatedRed << 16 | interpolatedGreen << 8 | interpolatedBlue;
   }

   private static int getRed(int hex) {
      return hex >> 16 & 255;
   }

   private static int getGreen(int hex) {
      return hex >> 8 & 255;
   }

   private static int getBlue(int hex) {
      return hex & 255;
   }

   private static int getAlpha(int hex) {
      return hex >> 24 & 255;
   }

   private static Double interpolate(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }

   private static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   public static Color rainbow(int delay, float saturation, float brightness) {
      double rainbow = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 16.0D);
      rainbow %= 360.0D;
      return Color.getHSBColor((float)(rainbow / 360.0D), saturation, brightness);
   }

   private ColorHelper() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
