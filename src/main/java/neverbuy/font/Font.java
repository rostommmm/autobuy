package neverbuy.font;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.NBTTextComponent;
import net.minecraft.util.text.ScoreTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import neverbuy.util.render.RenderUtils;

public class Font {
   private float posX;
   private float posY;
   private final int[] colorCode = new int[32];
   private boolean boldStyle;
   private boolean italicStyle;
   private boolean underlineStyle;
   private boolean strikethroughStyle;
   private final GlyphPage regularGlyphPage;
   private final GlyphPage boldGlyphPage;
   private final GlyphPage italicGlyphPage;
   private final GlyphPage boldItalicGlyphPage;

   public Font(GlyphPage regularGlyphPage, GlyphPage boldGlyphPage, GlyphPage italicGlyphPage, GlyphPage boldItalicGlyphPage) {
      this.regularGlyphPage = regularGlyphPage;
      this.boldGlyphPage = boldGlyphPage;
      this.italicGlyphPage = italicGlyphPage;
      this.boldItalicGlyphPage = boldItalicGlyphPage;

      for(int i = 0; i < 32; ++i) {
         int j = (i >> 3 & 1) * 85;
         int k = (i >> 2 & 1) * 170 + j;
         int l = (i >> 1 & 1) * 170 + j;
         int i1 = (i & 1) * 170 + j;
         if (i == 6) {
            k += 85;
         }

         if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
         }

         this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
      }

   }

   public static Font create(String file, float size, boolean bold, boolean italic, boolean boldItalic) {
      java.awt.Font font = null;

      try {
         InputStream in = (InputStream)Preconditions.checkNotNull(Font.class.getResourceAsStream("/assets/minecraft/neverbuy/font/" + file), "Font resource is null");

         try {
            font = java.awt.Font.createFont(0, in).deriveFont(0, size);
         } finally {
            if (Collections.singletonList(in).get(0) != null) {
               in.close();
            }

         }
      } catch (Exception var43) {
         var43.printStackTrace();
      }

      GlyphPage regularPage = new GlyphPage(font, true, true);
      regularPage.generateGlyphPage();
      regularPage.setupTexture();
      GlyphPage boldPage = regularPage;
      GlyphPage italicPage = regularPage;
      GlyphPage boldItalicPage = regularPage;

      try {
         InputStream in;
         if (bold) {
            in = (InputStream)Preconditions.checkNotNull(Font.class.getResourceAsStream("/assets/minecraft/neverbuy/font/" + file), "Font resource is null");

            try {
               boldPage = new GlyphPage(java.awt.Font.createFont(0, in).deriveFont(1, size), true, true);
               boldPage.generateGlyphPage();
               boldPage.setupTexture();
            } finally {
               if (Collections.singletonList(in).get(0) != null) {
                  in.close();
               }

            }
         }

         if (italic) {
            in = (InputStream)Preconditions.checkNotNull(Font.class.getResourceAsStream("/assets/minecraft/neverbuy/font/" + file), "Font resource is null");

            try {
               italicPage = new GlyphPage(java.awt.Font.createFont(0, in).deriveFont(2, size), true, true);
               italicPage.generateGlyphPage();
               italicPage.setupTexture();
            } finally {
               if (Collections.singletonList(in).get(0) != null) {
                  in.close();
               }

            }
         }

         if (boldItalic) {
            in = (InputStream)Preconditions.checkNotNull(Font.class.getResourceAsStream("/assets/minecraft/neverbuy/font/" + file), "Font resource is null");

            try {
               boldItalicPage = new GlyphPage(java.awt.Font.createFont(0, in).deriveFont(3, size), true, true);
               boldItalicPage.generateGlyphPage();
               boldItalicPage.setupTexture();
            } finally {
               if (Collections.singletonList(in).get(0) != null) {
                  in.close();
               }

            }
         }
      } catch (Exception var41) {
         var41.printStackTrace();
      }

      return new Font(regularPage, boldPage, italicPage, boldItalicPage);
   }

   public int drawShadow(String text, float x, float y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, x, y, color, true);
   }

   public int drawShadow(MatrixStack matrices, String text, float x, float y, int color) {
      return this.draw(matrices, text, x, y, color, true);
   }

   public int drawShadow(String text, double x, double y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, (float)x, (float)y, color, true);
   }

   public int draw(String text, float x, float y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, x, y, color, false);
   }

   public int draw(MatrixStack matrices, String text, float x, float y, int color) {
      return this.draw(matrices, text, x, y, color, false);
   }

   public int draw(String text, double x, double y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, (float)x, (float)y, color, false);
   }

   public int draw(MatrixStack matrices, String text, double x, double y, int color) {
      return this.draw(matrices, text, (float)x, (float)y, color, false);
   }

   public int drawCenterXY(String text, double x, double y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, (float)x - (float)this.getWidth(text) / 2.0F, (float)y - (float)this.getHeight() / 2.0F, color, false);
   }

   public int drawCenterX(String text, double x, double y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, (float)x - (float)this.getWidth(text) / 2.0F, (float)y, color, false);
   }

   public int drawCenteredStringWithShadow(String text, double x, double y, int color) {
      MatrixStack matrices = new MatrixStack();
      return this.draw(matrices, text, (float)x - (float)this.getWidth(text) / 2.0F, (float)y - (float)this.getHeight() / 2.0F, color, true);
   }

   public int drawCenteredStringWithShadow(MatrixStack matrices, String text, double x, double y, int color) {
      return this.draw(matrices, text, (float)x - (float)this.getWidth(text) / 2.0F, (float)y - (float)this.getHeight() / 2.0F, color, true);
   }

   public int drawCenteredString(MatrixStack matrixStack, String text, double x, double y, int color) {
      return this.draw(matrixStack, text, (float)x - (float)this.getWidth(text) / 2.0F, (float)y - (float)this.getHeight() / 2.0F, color, false);
   }

   public float getMiddleOfBox(float boxHeight) {
      return boxHeight / 2.0F - (float)this.getHeight() / 2.0F;
   }

   public int draw(MatrixStack matrices, String text, float x, float y, int color, boolean dropShadow) {
      y = (float)((long)y);
      this.resetStyles();
      int i;
      if (dropShadow) {
         i = this.renderString(matrices, text, x + 1.0F, y + 1.0F, color, true);
         i = Math.max(i, this.renderString(matrices, text, x, y, color, false));
      } else {
         i = this.renderString(matrices, text, x, y, color, false);
      }

      return i;
   }

   public int draw(MatrixStack matrices, ITextComponent texComponent, float x, float y, int color, boolean dropShadow) {
      y = (float)((long)y);
      this.resetStyles();
      String text = getPlainText(texComponent);
      int i;
      if (dropShadow) {
         i = this.renderString(matrices, text, x + 1.0F, y + 1.0F, color, true);
         i = Math.max(i, this.renderString(matrices, text, x, y, color, false));
      } else {
         i = this.renderString(matrices, text, x, y, color, false);
      }

      return i;
   }

   private int renderString(MatrixStack matrices, String text, float x, float y, int color, boolean dropShadow) {
      if (text == null) {
         return 0;
      } else {
         if ((color & -67108864) == 0) {
            color |= -16777216;
         }

         if (dropShadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
         }

         this.posX = x * 2.0F;
         this.posY = y * 2.0F;
         this.renderStringAtPos(matrices, text, dropShadow, color);
         return (int)(this.posX / 4.0F);
      }
   }

   private void renderStringAtPos(MatrixStack matrices, ITextComponent text, boolean shadow, int color) {
      this.renderStringAtPos(matrices, getPlainText(text), shadow, color);
   }

   private void renderStringAtPos(MatrixStack matrices, String text, boolean shadow, int color) {
      GlyphPage glyphPage = this.getCurrentGlyphPage();
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float g = (float)(color >> 16 & 255) / 255.0F;
      float h = (float)(color >> 8 & 255) / 255.0F;
      float k = (float)(color & 255) / 255.0F;
      matrices.push();
      matrices.scale(0.5F, 0.5F, 0.5F);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 771);
      RenderSystem.enableTexture();
      glyphPage.bindTexture();

      for(int i = 0; i < text.length(); ++i) {
         char c0 = text.charAt(i);
         if (c0 == 167 && i + 1 < text.length()) {
            int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
            if (i1 < 16) {
               this.boldStyle = false;
               this.strikethroughStyle = false;
               this.underlineStyle = false;
               this.italicStyle = false;
               if (i1 < 0) {
                  i1 = 15;
               }

               if (shadow) {
                  i1 += 16;
               }

               int j1 = this.colorCode[i1];
               g = (float)(j1 >> 16 & 255) / 255.0F;
               h = (float)(j1 >> 8 & 255) / 255.0F;
               k = (float)(j1 & 255) / 255.0F;
            } else if (i1 != 16) {
               if (i1 == 17) {
                  this.boldStyle = true;
               } else if (i1 == 18) {
                  this.strikethroughStyle = true;
               } else if (i1 == 19) {
                  this.underlineStyle = true;
               } else if (i1 == 20) {
                  this.italicStyle = true;
               } else {
                  this.boldStyle = false;
                  this.strikethroughStyle = false;
                  this.underlineStyle = false;
                  this.italicStyle = false;
               }
            }

            ++i;
         } else {
            glyphPage = this.getCurrentGlyphPage();
            glyphPage.bindTexture();
            float f = glyphPage.drawChar(matrices, c0, this.posX, this.posY, g, k, h, alpha);
            RenderSystem.texParameter(3553, 10240, 9729);
            this.doDraw(matrices, f, glyphPage);
         }
      }

      RenderUtils.resetColor();
      glyphPage.unbindTexture();
      matrices.pop();
   }

   public static String getPlainText(ITextComponent textComponent) {
      StringBuilder builder = new StringBuilder();
      Iterator var2 = textComponent.getSiblings().iterator();

      while(var2.hasNext()) {
         ITextComponent component = (ITextComponent)var2.next();
         if (component instanceof StringTextComponent) {
            builder.append(((StringTextComponent)component).getText());
         } else if (component instanceof TranslationTextComponent) {
            builder.append(((TranslationTextComponent)component).getKey());
         } else if (component instanceof ScoreTextComponent) {
            builder.append(((ScoreTextComponent)component).getName());
         } else if (component instanceof KeybindTextComponent) {
            builder.append(((KeybindTextComponent)component).getKeybind());
         } else if (component instanceof NBTTextComponent) {
            builder.append(((NBTTextComponent)component).field_218679_c);
         } else {
            builder.append(component.getString());
         }
      }

      return builder.toString();
   }

   private void doDraw(MatrixStack matrices, float f, GlyphPage glyphPage) {
      BufferBuilder bufferBuilder;
      if (this.strikethroughStyle) {
         bufferBuilder = Tessellator.getInstance().getBuffer();
         RenderSystem.disableTexture();
         bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
         bufferBuilder.pos((double)this.posX, (double)(this.posY + (float)(glyphPage.getMaxFontHeight() / 2)), 0.0D).endVertex();
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX + f, this.posY + (float)(glyphPage.getMaxFontHeight() / 2), 0.0F).endVertex();
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX + f, this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0F, 0.0F).endVertex();
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX, this.posY + (float)(glyphPage.getMaxFontHeight() / 2) - 1.0F, 0.0F).endVertex();
         bufferBuilder.finishDrawing();
         Tessellator.getInstance().draw();
         RenderSystem.enableTexture();
      }

      if (this.underlineStyle) {
         bufferBuilder = Tessellator.getInstance().getBuffer();
         RenderSystem.disableTexture();
         bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
         int l = this.underlineStyle ? -1 : 0;
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX + (float)l, this.posY + (float)glyphPage.getMaxFontHeight(), 0.0F).endVertex();
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX + f, this.posY + (float)glyphPage.getMaxFontHeight(), 0.0F).endVertex();
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX + f, this.posY + (float)glyphPage.getMaxFontHeight() - 1.0F, 0.0F).endVertex();
         bufferBuilder.pos(matrices.getLast().getMatrix(), this.posX + (float)l, this.posY + (float)glyphPage.getMaxFontHeight() - 1.0F, 0.0F).endVertex();
         bufferBuilder.finishDrawing();
         Tessellator.getInstance().draw();
         RenderSystem.enableTexture();
      }

      this.posX += f;
   }

   private GlyphPage getCurrentGlyphPage() {
      if (this.boldStyle && this.italicStyle) {
         return this.boldItalicGlyphPage;
      } else if (this.boldStyle) {
         return this.boldGlyphPage;
      } else {
         return this.italicStyle ? this.italicGlyphPage : this.regularGlyphPage;
      }
   }

   private void resetStyles() {
      this.boldStyle = false;
      this.italicStyle = false;
      this.underlineStyle = false;
      this.strikethroughStyle = false;
   }

   public int getHeight() {
      return this.regularGlyphPage.getMaxFontHeight() / 2;
   }

   public int getWidth(String str) {
      String text = this.removeColorCodes(str);
      if (text == null) {
         return 0;
      } else {
         int width = 0;
         int size = text.length();
         boolean on = false;

         for(int i = 0; i < size; ++i) {
            char character = text.charAt(i);
            if (character == 167) {
               on = true;
            } else if (on && character >= '0' && character <= 'r') {
               int colorIndex = "0123456789abcdefklmnor".indexOf(character);
               if (colorIndex < 16) {
                  this.boldStyle = false;
                  this.italicStyle = false;
               } else if (colorIndex == 17) {
                  this.boldStyle = true;
               } else if (colorIndex == 20) {
                  this.italicStyle = true;
               } else if (colorIndex == 21) {
                  this.boldStyle = false;
                  this.italicStyle = false;
               }

               ++i;
               on = false;
            } else {
               if (on) {
                  --i;
               }

               character = text.charAt(i);
               GlyphPage currentPage = this.getCurrentGlyphPage();
               width = (int)((float)width + (currentPage.getWidth(character) - 8.0F));
            }
         }

         return width / 2;
      }
   }

   public String removeColorCodes(String text) {
      String str = text;
      String[] colorcodes = new String[]{"4", "c", "6", "e", "2", "a", "b", "3", "1", "9", "d", "5", "f", "7", "8", "0", "k", "m", "o", "l", "n", "r"};
      String[] var4 = colorcodes;
      int var5 = colorcodes.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String c = var4[var6];
         str = str.replace("§" + c, "");
      }

      return str.trim();
   }

   public String trimStringToWidth(String text, int width) {
      return this.trimStringToWidth(text, width, false);
   }

   public String trimStringToWidth(String str, int maxWidth, boolean reverse) {
      StringBuilder stringbuilder = new StringBuilder();
      boolean on = false;
      String text = this.removeColorCodes(str);
      int j = reverse ? text.length() - 1 : 0;
      int k = reverse ? -1 : 1;
      int width = 0;

      for(int i = j; i >= 0 && i < text.length() && i < maxWidth; i += k) {
         char character = text.charAt(i);
         if (character == 167) {
            on = true;
         } else if (on && character >= '0' && character <= 'r') {
            int colorIndex = "0123456789abcdefklmnor".indexOf(character);
            if (colorIndex < 16) {
               this.boldStyle = false;
               this.italicStyle = false;
            } else if (colorIndex == 17) {
               this.boldStyle = true;
            } else if (colorIndex == 20) {
               this.italicStyle = true;
            } else if (colorIndex == 21) {
               this.boldStyle = false;
               this.italicStyle = false;
            }

            ++i;
            on = false;
         } else {
            if (on) {
               --i;
            }

            character = text.charAt(i);
            GlyphPage currentPage = this.getCurrentGlyphPage();
            width = (int)((float)width + (currentPage.getWidth(character) - 8.0F) / 2.0F);
         }

         if (i > width) {
            break;
         }

         if (reverse) {
            stringbuilder.insert(0, character);
         } else {
            stringbuilder.append(character);
         }
      }

      return stringbuilder.toString();
   }
}
