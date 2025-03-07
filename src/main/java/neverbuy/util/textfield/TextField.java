package neverbuy.util.textfield;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import neverbuy.font.Font;
import neverbuy.util.misc.ICalculator;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;

public class TextField implements ICalculator {
   public float posX;
   public float posY;
   public float width;
   public float height;
   public String value = "";
   public String title;
   public boolean focused;
   public Font f;
   public FieldFunction fieldFunction;
   float typeX;
   float textX;

   public TextField(String title, Font f, float posX, float posY, float width, float height, FieldFunction fieldFunction) {
      this.posX = posX;
      this.posY = posY;
      this.width = width;
      this.height = height;
      this.title = title;
      this.f = f;
      this.fieldFunction = fieldFunction;
   }

   public void setStartX(float startX) {
      this.typeX = startX;
      this.textX = startX;
   }

   public void render(MatrixStack matrixStack) {
      RoundedUtils.drawRound(this.textX, this.posY, this.focused && !this.value.isEmpty() ? (float)(this.f.getWidth(this.value) + 2) : this.width, this.height, 4.0F, new Color(1117717));
      if (!this.focused || this.value.isEmpty()) {
         this.f.draw(this.title, this.textX, this.posY + (this.height - (float)this.f.getHeight()) / 2.0F, -1);
      }

      if (this.focused && !this.value.isEmpty()) {
         this.typeX = RenderUtils.lerp(this.typeX, this.posX + this.width / 2.0F, 12.0F);
         this.textX = RenderUtils.lerp(this.textX, this.posX + (this.width - (float)this.f.getWidth(this.value)) / 2.0F, 12.0F);
      } else {
         this.typeX = this.posX + 2.0F;
         this.textX = RenderUtils.lerp(this.textX, this.posX + 2.0F, 20.0F);
      }

      if (this.focused && !this.value.isEmpty()) {
         this.f.draw(this.value, this.textX, this.posY + (this.height - (float)this.f.getHeight()) / 2.0F, -1);
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      this.focused = this.isHovered(mouseX, mouseY);
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return mouseX >= (double)this.posX && mouseX <= (double)(this.posX + this.width) && mouseY >= (double)this.posY && mouseY <= (double)(this.posY + this.height);
   }

   public void charTyped(char codePoint) {
      if (this.focused) {
         this.value = this.value + codePoint;
         this.fieldFunction.onChar(this.value);
      }

   }

   public void keyPressed(int keyCode) {
      if (this.focused) {
         if (keyCode == 259) {
            if (Screen.hasControlDown()) {
               this.value = "";
               this.fieldFunction.onChar(this.value);
               return;
            }

            if (this.value != null && !this.value.isEmpty()) {
               this.value = this.value.substring(0, this.value.length() - 1);
            }
         } else if (keyCode == 257) {
            this.fieldFunction.onEnter(this.value);
         } else if (Screen.isPaste(keyCode)) {
            Minecraft mc = Minecraft.getInstance();
            String clipboard = mc.keyboardListener.getClipboardString();
            this.value = this.value + clipboard;
            this.fieldFunction.onChar(this.value);
         }
      }

   }
}
