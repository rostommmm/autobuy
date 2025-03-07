package neverbuy.gui.component.itemsetting;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import neverbuy.autobuy.list.item.setting.BDSetting;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.util.misc.ChatUtil;
import neverbuy.util.textfield.FieldFunction;
import neverbuy.util.textfield.TextField;

public class BDSettingComponent extends ItemSettingComponent {
   private final TextField textField;

   public BDSettingComponent(float[] coordinates, final BDSetting bdSetting) {
      super(coordinates, bdSetting);
      this.offset = coordinates[4];
      this.textField = new TextField("Значение", Fonts.Inter.get(12.0F), this.posX + this.width - 40.0F, this.posY + 2.0F + this.offset, 35.0F, 11.0F, new FieldFunction() {
         public void onChar(String full) {
            if (full.isEmpty()) {
               full = "0";
            }

            if (full.contains("/") || full.contains("+") || full.contains("-") || full.contains("*")) {
               full = "0";
            }

            full = full.replaceAll(",", "");
            BigDecimal bd = new BigDecimal(full);
            bdSetting.setVal(bd);
         }

         public void onEnter(String full) {
            try {
               bdSetting.setVal(BigDecimal.valueOf(BDSettingComponent.this.textField.eval(full)));
               NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
               BDSettingComponent.this.textField.value = numberFormat.format(((BigDecimal)bdSetting.getVal()).toBigInteger());
            } catch (UnsupportedOperationException var3) {
               Minecraft.getInstance().displayGuiScreen((Screen)null);
               ChatUtil.addMessage("Нельзя делить на ноль!");
            }

         }
      });
      NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
      this.textField.value = numberFormat.format(((BigDecimal)bdSetting.getVal()).toBigInteger());
      this.textField.setStartX(this.posX);
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      if (this.itemSetting.isVisible()) {
         Font f = Fonts.Inter.get(12.0F);
         f.draw(this.itemSetting.getName(), this.posX + 1.0F, this.posY + this.offset + 1.0F, -1);
         this.textField.render(matrixStack);
         this.textField.posX = this.posX + this.width - 40.0F;
         this.textField.posY = this.posY + this.offset;
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.itemSetting.isVisible()) {
         this.textField.mouseClicked(mouseX, mouseY, button);
      }
   }

   public void charTyped(char charCode) {
      if (this.itemSetting.isVisible()) {
         if (Character.isDigit(charCode) || charCode == ',' || charCode == '/' || charCode == '*' || charCode == '+' || charCode == '-') {
            this.textField.charTyped(charCode);
         }

      }
   }

   public void keyPressed(int keyCode) {
      if (this.itemSetting.isVisible()) {
         this.textField.keyPressed(keyCode);
      }
   }
}
