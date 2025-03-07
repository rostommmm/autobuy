package neverbuy.gui.component.setting;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.Objects;
import neverbuy.autobuy.api.FieldSetting;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.gui.component.SettingComponent;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;
import neverbuy.util.textfield.TextField;

public class FieldSettingComponent extends SettingComponent {
   private final TextField textField;
   private final FieldSetting fieldSetting;
   private float yAnimation;
   private float offset;

   public FieldSettingComponent(float[] coordinates, FieldSetting fieldSetting) {
      super(coordinates);
      this.fieldSetting = fieldSetting;
      this.offset = coordinates[4];
      Font var10004 = Fonts.Inter.get(15.0F);
      float var10005 = this.posX + this.width - 85.0F;
      float var10006 = this.yAnimation + this.offset + 3.0F;
      FieldSetting var10009 = this.fieldSetting;
      Objects.requireNonNull(var10009);
      this.textField = new TextField("Значение", var10004, var10005, var10006, 80.0F, 12.5F, var10009::setValue);
      this.textField.value = (String)fieldSetting.getValue();
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.yAnimation = RenderUtils.lerp(this.yAnimation, this.posY, 8.0F);
      this.textField.posY = this.yAnimation + this.offset + 3.0F + this.scroll;
      RoundedUtils.drawRound(this.posX, this.yAnimation + this.offset + this.scroll, this.width, this.height, 4.0F, new Color(2103849));
      Font f = Fonts.Inter.get(17.0F);
      f.draw(this.fieldSetting.getName(), this.posX + 2.0F, this.yAnimation + this.offset + this.scroll + (this.height - (float)f.getHeight()) / 2.0F, -1);
      this.textField.render(matrixStack);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      this.textField.mouseClicked(mouseX, mouseY, button);
   }

   public void keyPressed(int keyCode) {
      this.textField.keyPressed(keyCode);
   }

   public void charTyped(char codePoint) {
      this.textField.charTyped(codePoint);
   }
}
