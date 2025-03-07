package neverbuy.util.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.awt.Color;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import neverbuy.font.Font;
import neverbuy.util.IUtil;
import org.lwjgl.opengl.GL11;

public final class RenderUtils implements IUtil {
   static Color colorOne = new Color(4408534);
   static Color colorTwo = new Color(410310);

   public static void drawSmoothRect(MatrixStack matrixStack, double left, double top, double right, double bottom, int color) {
      resetColor();
      GL11.glEnable(2848);
      drawRect(matrixStack, left, top, right, bottom, color);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawRect(matrixStack, left * 2.0D - 1.0D, top * 2.0D, left * 2.0D, bottom * 2.0D - 1.0D, color);
      drawRect(matrixStack, left * 2.0D, top * 2.0D - 1.0D, right * 2.0D, top * 2.0D, color);
      drawRect(matrixStack, right * 2.0D, top * 2.0D, right * 2.0D + 1.0D, bottom * 2.0D - 1.0D, color);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      resetColor();
   }

   public static void resetColor() {
      GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static StringTextComponent gradient(String message) {
      StringTextComponent text = new StringTextComponent("");

      for(int i = 0; i < message.length(); ++i) {
        // text.append((new StringTextComponent(String.valueOf(message.charAt(i)))).setStyle(Style.EMPTY.setColor(new net.minecraft.util.text.Color(ColorHelper.gradient(5, i, colorOne, colorTwo)))));
      }

      return text;
   }

   public static void drawSmoothText(MatrixStack matrixStack, Font font, String text, float x, float y, int colorStart, int colorEnd, long animationDuration) {
      float gradientX = x;
      long currentTime = System.currentTimeMillis();

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (c == ' ') {
            gradientX += 4.0F;
         } else {
            float letterWidth = (float)font.getWidth(String.valueOf(c));
            float animationProgress = (float)((currentTime + (long)i * (animationDuration / (long)text.length())) % animationDuration) / (float)animationDuration;
            float progress;
            if (animationProgress < 0.5F) {
               progress = animationProgress * 2.0F;
            } else {
               progress = 1.0F - (animationProgress - 0.5F) * 2.0F;
            }

            float interpolation = MathHelper.clamp(progress, 0.0F, 1.0F);
            int gradientColor = blendColors(colorStart, colorEnd, interpolation);
            font.drawShadow(matrixStack, String.valueOf(c), gradientX, y, gradientColor);
            gradientX += letterWidth;
         }
      }

   }

   public static void drawRect(MatrixStack matrixStack, double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      double finalLeft = left;
      double finalRight = right;
      double finalBottom = bottom;
      double finalTop = top;
      start2Draw(() -> {
         setColor(color);
         BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
         bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
         vertex(bufferBuilder, matrixStack, finalLeft, finalBottom).endVertex();
         vertex(bufferBuilder, matrixStack, finalRight, finalBottom).endVertex();
         vertex(bufferBuilder, matrixStack, finalRight, finalTop).endVertex();
         vertex(bufferBuilder, matrixStack, finalLeft, finalTop).endVertex();
         bufferBuilder.finishDrawing();
         WorldVertexBufferUploader.draw(bufferBuilder);
      });
   }

   public static void drawCircle(MatrixStack matrixStack, float x, float y, float start, float end, float radius, boolean filled, Color color) {
      start2Draw(() -> {
         setColor(color.getRGB());
         GL11.glLineWidth(1.0F);
         GL11.glEnable(2848);
         GL11.glHint(3154, 4354);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferBuilder = tessellator.getBuffer();
         bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
         startRenderCircle(matrixStack, x, y, radius, start, end);
         tessellator.draw();
         bufferBuilder.begin(filled ? 6 : 3, DefaultVertexFormats.POSITION);
         startRenderCircle(matrixStack, x, y, radius, start, end);
         tessellator.draw();
         GL11.glHint(3154, 4352);
         GL11.glDisable(2848);
      });
   }

   private static void startRenderCircle(MatrixStack matrixStack, float x, float y, float radius, float start, float end) {
      BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();

      for(float i = end; i >= start; --i) {
         double cos = Math.cos(Math.toRadians((double)i)) * (double)radius;
         double sin = Math.sin(Math.toRadians((double)i)) * (double)radius;
         vertex(bufferBuilder, matrixStack, (double)x + cos, (double)y + sin).endVertex();
      }

   }

   public static IVertexBuilder vertex(BufferBuilder builder, MatrixStack matrixStack, double x, double y) {
      return builder.pos(matrixStack.getLast().getMatrix(), (float)x, (float)y, 0.0F);
   }

   public static void start2Draw(Runnable runnable) {
      boolean isEnabled = GL11.glIsEnabled(3042);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      runnable.run();
      if (!isEnabled) {
         GL11.glDisable(3042);
      }

      GL11.glEnable(3553);
      GL11.glEnable(3008);
   }

   public static void drawImage(MatrixStack matrixStack, ResourceLocation res, int x, int y, int width, int height, int tWidth, int tHeight) {
      GlStateManager.pushMatrix();
      mc.getTextureManager().bindTexture(res);
      AbstractGui.blit(matrixStack, x, y, width, height, 0.0F, 0.0F, tWidth, tHeight, tWidth, tHeight);
      GlStateManager.popMatrix();
   }

   public static int blendColors(int colorStart, int colorEnd, float ratio) {
      int alpha = (int)((float)RenderUtils.PackedColor.getAlpha(colorStart) * (1.0F - ratio) + (float)RenderUtils.PackedColor.getAlpha(colorEnd) * ratio);
      int red = (int)((float)RenderUtils.PackedColor.getRed(colorStart) * (1.0F - ratio) + (float)RenderUtils.PackedColor.getRed(colorEnd) * ratio);
      int green = (int)((float)RenderUtils.PackedColor.getGreen(colorStart) * (1.0F - ratio) + (float)RenderUtils.PackedColor.getGreen(colorEnd) * ratio);
      int blue = (int)((float)RenderUtils.PackedColor.getBlue(colorStart) * (1.0F - ratio) + (float)RenderUtils.PackedColor.getBlue(colorEnd) * ratio);
      return RenderUtils.PackedColor.packColor(alpha, red, green, blue);
   }

   public static void setColor(int color) {
      GL11.glColor4ub((byte)(color >> 16 & 255), (byte)(color >> 8 & 255), (byte)(color & 255), (byte)(color >> 24 & 255));
   }

   public static float fast(float end, float start, float multiple) {
      return (1.0F - MathHelper.clamp((float)(deltaTime() * (double)multiple), 0.0F, 1.0F)) * end + MathHelper.clamp((float)(deltaTime() * (double)multiple), 0.0F, 1.0F) * start;
   }

   public static double deltaTime() {
      return Minecraft.getDebugFPS() > 0 ? 1.0D / (double)Minecraft.getDebugFPS() : 1.0D;
   }

   public static float lerp(float end, float start, float multiple) {
      return (float)((double)end + (double)(start - end) * MathHelper.clamp(deltaTime() * (double)multiple, 0.0D, 1.0D));
   }

   public static double Interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public static void drawOneSideRound(MatrixStack matrixStack, float x, float y, float width, float height, float radius, Color color) {
      RoundedUtils.drawRound(x - 0.5F, y - 0.5F, width + 0.75F, height, radius, color);
      drawRect(matrixStack, (double)x - 0.35D, (double)(y + 10.0F), (double)(x + width) + 0.5D, (double)(y + height + 0.5F), color.getRGB());
   }

   public static void drawImage(ResourceLocation image, double x, double y, double width, double height, int color) {
      RenderSystem.enableBlend();
      resetColor();
      RenderSystem.blendFuncSeparate(770, 771, 1, 0);
      RenderSystem.enableDepthTest();
      mc.getTextureManager().bindTexture(image);
      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
      bufferBuilder.begin(9, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
      bufferBuilder.pos(x, y + height, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >>> 24).tex(0.0F, 0.99F).lightmap(0, 240).endVertex();
      bufferBuilder.pos(x + width, y + height, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >>> 24).tex(1.0F, 0.99F).lightmap(0, 240).endVertex();
      bufferBuilder.pos(x + width, y, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >>> 24).tex(1.0F, 0.0F).lightmap(0, 240).endVertex();
      bufferBuilder.pos(x, y, 0.0D).color(color >> 16 & 255, color >> 8 & 255, color & 255, color >>> 24).tex(0.0F, 0.0F).lightmap(0, 240).endVertex();
      tessellator.draw();
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
   }

   public static void setupOverlayRendering(int scale) {
      MainWindow mainWindow = mc.getMainWindow();
      mainWindow.setGuiScale((double)scale);
      GlStateManager.clear(256, false);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, (double)mainWindow.getScaledWidth(), (double)mainWindow.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
   }

   public static void setupOverlayRendering() {
      MainWindow mainWindow = mc.getMainWindow();
      int i = mainWindow.calcGuiScale(mc.gameSettings.guiScale, mc.getForceUnicodeFont());
      mainWindow.setGuiScale((double)i);
      GlStateManager.clear(256, false);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, (double)mainWindow.getScaledWidth(), (double)mainWindow.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
   }

   public static void renderItem(ItemStack itemStack, int x, int y) {
      RenderSystem.enableDepthTest();
      mc.getItemRenderer().renderItemIntoGUI(itemStack, x, y);
      mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, itemStack, x, y);
      RenderSystem.disableDepthTest();
   }

   private RenderUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   private static class PackedColor {
      public static int getAlpha(int packedColor) {
         return packedColor >>> 24;
      }

      public static int getRed(int packedColor) {
         return packedColor >> 16 & 255;
      }

      public static int getGreen(int packedColor) {
         return packedColor >> 8 & 255;
      }

      public static int getBlue(int packedColor) {
         return packedColor & 255;
      }

      public static int packColor(int alpha, int red, int green, int blue) {
         return alpha << 24 | red << 16 | green << 8 | blue;
      }

      public static int blendColors(int packedColourOne, int packedColorTwo) {
         return packColor(getAlpha(packedColourOne) * getAlpha(packedColorTwo) / 255, getRed(packedColourOne) * getRed(packedColorTwo) / 255, getGreen(packedColourOne) * getGreen(packedColorTwo) / 255, getBlue(packedColourOne) * getBlue(packedColorTwo) / 255);
      }
   }
}
