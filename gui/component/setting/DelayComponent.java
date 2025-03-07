package neverbuy.gui.component.setting;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import neverbuy.autobuy.api.Delay;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.gui.component.SettingComponent;
import neverbuy.util.misc.ChatUtil;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;
import neverbuy.util.textfield.FieldFunction;
import neverbuy.util.textfield.TextField;

public class DelayComponent extends SettingComponent {
   final Delay delay;
   float offset;
   float yAnimation;
   TextField textField;

   public DelayComponent(float[] coordinates, final Delay delay) {
      super(coordinates);
      this.delay = delay;
      this.offset = coordinates[4];
      this.textField = new TextField("Значение", Fonts.Inter.get(15.0F), this.posX + this.width - 85.0F, this.yAnimation + this.offset + 3.0F, 80.0F, 12.5F, new FieldFunction() {
         public void onChar(String full) {
            if (full.isEmpty()) {
               full = "0";
            }

            if (full.contains("/") || full.contains("+") || full.contains("-") || full.contains("*")) {
               full = "0";
            }

            full = full.replaceAll(",", "");
            BigDecimal bd = new BigDecimal(full);
            delay.setValue(bd);
         }

         public void onEnter(String full) {
            try {
               delay.setValue(BigDecimal.valueOf(DelayComponent.this.textField.eval(full)));
               NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
               DelayComponent.this.textField.value = numberFormat.format(((BigDecimal)delay.getValue()).toBigInteger());
            } catch (UnsupportedOperationException var3) {
               Minecraft.getInstance().displayGuiScreen((Screen)null);
               ChatUtil.addMessage("Нельзя делить на ноль!");
            }

         }
      });
      this.textField.setStartX(this.textField.posX);
      NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
      this.textField.value = numberFormat.format(((BigDecimal)this.delay.getValue()).toBigInteger());
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.yAnimation = RenderUtils.lerp(this.yAnimation, this.posY, 8.0F);
      this.textField.posY = this.yAnimation + this.offset + 3.0F + this.scroll;
      RoundedUtils.drawRound(this.posX, this.yAnimation + this.offset + this.scroll, this.width, this.height, 4.0F, new Color(2103849));
      Font f = Fonts.Inter.get(17.0F);
      f.draw(this.delay.getName(), this.posX + 2.0F, this.yAnimation + this.offset + this.scroll + (this.height - (float)f.getHeight()) / 2.0F, -1);
      this.textField.render(matrixStack);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      this.textField.mouseClicked(mouseX, mouseY, button);
   }

   public void keyPressed(int keyCode) {
      this.textField.keyPressed(keyCode);
   }

   public void charTyped(char charCode) {
      if (Character.isDigit(charCode) || charCode == ',' || charCode == '/' || charCode == '*' || charCode == '+' || charCode == '-') {
         this.textField.charTyped(charCode);
      }

   }

   public boolean isHovered(double mouseX, double mouseY) {
      Font f = Fonts.Inter.get(17.0F);
      String value = ((BigDecimal)this.delay.getValue()).toBigInteger().toString();
      float valueX = this.posX + this.width - 10.0F - (float)f.getWidth(value);
      return mouseX >= (double)valueX && mouseX <= (double)(valueX + (float)f.getWidth(value) + 4.0F) && mouseY >= (double)(this.yAnimation + this.offset + this.scroll + 3.0F) && mouseY <= (double)(this.yAnimation + this.offset + this.scroll + 7.0F);
   }
}
