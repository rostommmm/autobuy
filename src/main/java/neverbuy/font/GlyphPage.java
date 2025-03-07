package neverbuy.font;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.BufferUtils;

public class GlyphPage {
   public static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя";
   private int imgSize;
   private int maxFontHeight = -1;
   private final java.awt.Font font;
   private final boolean antiAliasing;
   private final boolean fractionalMetrics;
   private final HashMap<Character, GlyphPage.Glyph> glyphCharacterMap = new HashMap();
   private BufferedImage bufferedImage;
   private DynamicTexture loadedTexture;

   public GlyphPage(java.awt.Font font, boolean antiAliasing, boolean fractionalMetrics) {
      this.font = font;
      this.antiAliasing = antiAliasing;
      this.fractionalMetrics = fractionalMetrics;
   }

   public void generateGlyphPage() {
      double maxWidth = -1.0D;
      double maxHeight = -1.0D;
      AffineTransform affineTransform = new AffineTransform();
      FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, this.antiAliasing, this.fractionalMetrics);

      for(int i = 0; i < "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя".length(); ++i) {
         char ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя".charAt(i);
         Rectangle2D bounds = this.font.getStringBounds(Character.toString(ch), fontRenderContext);
         if (maxWidth < bounds.getWidth()) {
            maxWidth = bounds.getWidth();
         }

         if (maxHeight < bounds.getHeight()) {
            maxHeight = bounds.getHeight();
         }
      }

      maxWidth += 2.0D;
      maxHeight += 2.0D;
      this.imgSize = (int)Math.ceil(Math.max(Math.ceil(Math.sqrt(maxWidth * maxWidth * (double)"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя".length()) / maxWidth), Math.ceil(Math.sqrt(maxHeight * maxHeight * (double)"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя".length()) / maxHeight)) * Math.max(maxWidth, maxHeight)) + 1;
      this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 2);
      Graphics2D g = this.bufferedImage.createGraphics();
      g.setFont(this.font);
      g.setColor(new Color(255, 255, 255, 0));
      g.fillRect(0, 0, this.imgSize, this.imgSize);
      g.setColor(Color.white);
      g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      FontMetrics fontMetrics = g.getFontMetrics();
      int currentCharHeight = 0;
      int posX = 0;
      int posY = 1;

      for(int i = 0; i < "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя".length(); ++i) {
         char ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,/\\:;?!@\"'%^&*()[]<>|{}=+-_~©`#№$АБВГДЕЁЖЗИЙКЛМНОПРСТОУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстоуфхцчшщъыьэюя".charAt(i);
         GlyphPage.Glyph glyph = new GlyphPage.Glyph();
         Rectangle2D bounds = fontMetrics.getStringBounds(Character.toString(ch), g);
         glyph.width = bounds.getBounds().width + 8;
         glyph.height = bounds.getBounds().height;
         if (posX + glyph.width >= this.imgSize) {
            posX = 0;
            posY += currentCharHeight;
            currentCharHeight = 0;
         }

         glyph.x = posX;
         glyph.y = posY;
         if (glyph.height > this.maxFontHeight) {
            this.maxFontHeight = glyph.height;
         }

         if (glyph.height > currentCharHeight) {
            currentCharHeight = glyph.height;
         }

         g.drawString(Character.toString(ch), posX + 2, posY + fontMetrics.getAscent());
         posX += glyph.width;
         this.glyphCharacterMap.put(ch, glyph);
      }

   }

   public void setupTexture() {
      try {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write(this.bufferedImage, "png", baos);
         byte[] bytes = baos.toByteArray();
         ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
         data.flip();
         this.loadedTexture = new DynamicTexture(NativeImage.read(data));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void bindTexture() {
      RenderSystem.bindTexture(this.loadedTexture.getGlTextureId());
   }

   public void unbindTexture() {
      RenderSystem.bindTexture(0);
   }

   public float drawChar(MatrixStack stack, char ch, float x, float y, float red, float blue, float green, float alpha) {
      GlyphPage.Glyph glyph = (GlyphPage.Glyph)this.glyphCharacterMap.get(ch);
      if (glyph == null) {
         return 0.0F;
      } else {
         float pageX = (float)glyph.x / (float)this.imgSize;
         float pageY = (float)glyph.y / (float)this.imgSize;
         float pageWidth = (float)glyph.width / (float)this.imgSize;
         float pageHeight = (float)glyph.height / (float)this.imgSize;
         float width = (float)glyph.width;
         float height = (float)glyph.height;
         BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
         bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
         bufferBuilder.pos(stack.getLast().getMatrix(), x, y + height, 0.0F).color(red, green, blue, alpha).tex(pageX, pageY + pageHeight).endVertex();
         bufferBuilder.pos(stack.getLast().getMatrix(), x + width, y + height, 0.0F).color(red, green, blue, alpha).tex(pageX + pageWidth, pageY + pageHeight).endVertex();
         bufferBuilder.pos(stack.getLast().getMatrix(), x + width, y, 0.0F).color(red, green, blue, alpha).tex(pageX + pageWidth, pageY).endVertex();
         bufferBuilder.pos(stack.getLast().getMatrix(), x, y, 0.0F).color(red, green, blue, alpha).tex(pageX, pageY).endVertex();
         Tessellator.getInstance().draw();
         return width - 8.0F;
      }
   }

   public float getWidth(char ch) {
      GlyphPage.Glyph glyph = (GlyphPage.Glyph)this.glyphCharacterMap.get(ch);
      return glyph == null ? 0.0F : (float)glyph.width;
   }

   public boolean isAntiAliasingEnabled() {
      return this.antiAliasing;
   }

   public boolean isFractionalMetricsEnabled() {
      return this.fractionalMetrics;
   }

   public int getMaxFontHeight() {
      return this.maxFontHeight;
   }

   static class Glyph {
      private int x;
      private int y;
      private int width;
      private int height;

      Glyph(int x, int y, int width, int height) {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
      }

      Glyph() {
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int getWidth() {
         return this.width;
      }

      public int getHeight() {
         return this.height;
      }
   }
}
