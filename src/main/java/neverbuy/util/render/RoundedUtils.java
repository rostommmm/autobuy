package neverbuy.util.render;

import com.mojang.blaze3d.platform.GlStateManager;
import java.awt.Color;
import net.minecraft.client.MainWindow;
import neverbuy.util.IUtil;
import org.lwjgl.opengl.GL11;

public class RoundedUtils implements IUtil {
   private static final ShaderUtil roundedShader = new ShaderUtil("rounded");
   private static final ShaderUtil roundedGradient = new ShaderUtil("rounded_gradient");
   private static final ShaderUtil roundedOutline = new ShaderUtil("rounded_outline");
   private static final ShaderUtil roundedBlurredGradient = new ShaderUtil("rounded_blurred_gradient");

   public static void drawRoundedRectOutline(double x, double y, double width, double height, double radius, float thickness, Color color) {
      float[] c = ColorHelper.getColorComps(color);
      GlStateManager.disableTexture();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedOutline.init();
      roundedOutline.setUniformf("size", (float)width * 2.0F, (float)height * 2.0F);
      roundedOutline.setUniformf("round", (float)radius * 2.0F);
      roundedOutline.setUniformf("thickness", thickness);
      roundedOutline.setUniformf("color", c[0], c[1], c[2], c[3]);
      ShaderUtil.drawQuads(x, y, width, height);
      roundedOutline.unload();
      GlStateManager.enableTexture();
      GlStateManager.disableBlend();
      RenderUtils.resetColor();
   }

   public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
      drawRound(x, y, width, height, radius, false, color);
   }

   public static void drawRoundedGradientBlurredRect(double x, double y, double width, double height, double roundR, float blurR, Color... colors) {
      float[] c = ColorHelper.getColorComps(colors[0]);
      float[] c1 = ColorHelper.getColorComps(colors[1]);
      float[] c2 = ColorHelper.getColorComps(colors[2]);
      float[] c3 = ColorHelper.getColorComps(colors[3]);
      GlStateManager.disableTexture();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GL11.glEnable(3008);
      GL11.glAlphaFunc(516, 1.0E-4F);
      roundedBlurredGradient.init();
      roundedBlurredGradient.setUniformf("size", (float)(width + (double)(2.0F * blurR)), (float)(height + (double)(2.0F * blurR)));
      roundedBlurredGradient.setUniformf("softness", blurR);
      roundedBlurredGradient.setUniformf("radius", (float)roundR);
      roundedBlurredGradient.setUniformf("color1", c[0], c[1], c[2], c[3]);
      roundedBlurredGradient.setUniformf("color2", c1[0], c1[1], c1[2], c1[3]);
      roundedBlurredGradient.setUniformf("color3", c2[0], c2[1], c2[2], c2[3]);
      roundedBlurredGradient.setUniformf("color4", c3[0], c3[1], c3[2], c3[3]);
      ShaderUtil.drawQuads(x - (double)blurR, y - (double)blurR, width + (double)(blurR * 2.0F), height + (double)(blurR * 2.0F));
      roundedBlurredGradient.unload();
      GL11.glDisable(3008);
      GlStateManager.enableTexture();
      GlStateManager.disableBlend();
      RenderUtils.resetColor();
   }

   private static void drawRound(float x, float y, float width, float height, float radius, boolean blur, Color color) {
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedShader.init();
      setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
      roundedShader.setUniformi("blur", blur ? 1 : 0);
      roundedShader.setUniformf("color", (float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
      ShaderUtil.drawQuads(x - 1.0F, y - 1.0F, width + 2.0F, height + 2.0F);
      roundedShader.unload();
      GlStateManager.disableBlend();
   }

   public static void drawRoundGradient(double x, double y, double width, double height, double radius, Color... colors) {
      float[] c = ColorHelper.getColorComps(colors[0]);
      float[] c1 = ColorHelper.getColorComps(colors[1]);
      float[] c2 = ColorHelper.getColorComps(colors[2]);
      float[] c3 = ColorHelper.getColorComps(colors[3]);
      GlStateManager.disableTexture();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      roundedGradient.init();
      roundedGradient.setUniformf("size", (float)width * 2.0F, (float)height * 2.0F);
      roundedGradient.setUniformf("round", (float)radius * 2.0F);
      roundedGradient.setUniformf("color1", c[0], c[1], c[2], c[3]);
      roundedGradient.setUniformf("color2", c1[0], c1[1], c1[2], c1[3]);
      roundedGradient.setUniformf("color3", c2[0], c2[1], c2[2], c2[3]);
      roundedGradient.setUniformf("color4", c3[0], c3[1], c3[2], c3[3]);
      ShaderUtil.drawQuads(x, y, width, height);
      roundedGradient.unload();
      GlStateManager.enableTexture();
      GlStateManager.disableBlend();
      RenderUtils.resetColor();
   }

   private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
      MainWindow sr = mc.getMainWindow();
      roundedTexturedShader.setUniformf("location", (float)((double)x * sr.getGuiScaleFactor()), (float)((double)sr.getHeight() - (double)height * sr.getGuiScaleFactor() - (double)y * sr.getGuiScaleFactor()));
      roundedTexturedShader.setUniformf("rectSize", (float)((double)width * sr.getGuiScaleFactor()), (float)((double)height * sr.getGuiScaleFactor()));
      roundedTexturedShader.setUniformf("radius", (float)((double)radius * sr.getGuiScaleFactor()));
   }
}
