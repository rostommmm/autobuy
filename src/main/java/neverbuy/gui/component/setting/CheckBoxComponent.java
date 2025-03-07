package neverbuy.gui.component.setting;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import neverbuy.autobuy.api.CheckBox;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.gui.component.SettingComponent;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;

public class CheckBoxComponent extends SettingComponent {
   final CheckBox checkBox;
   float offset;
   float yAnimation;
   ResourceLocation CHECKBOX_LOCATION = new ResourceLocation("minecraft", "neverbuy/checkbox.png");

   public CheckBoxComponent(float[] coordinates, CheckBox checkBox) {
      super(coordinates);
      this.checkBox = checkBox;
      this.offset = coordinates[4];
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.yAnimation = RenderUtils.lerp(this.yAnimation, this.posY, 8.0F);
      RoundedUtils.drawRound(this.posX, this.yAnimation + this.offset + this.scroll, this.width, this.height, 4.0F, new Color(2103849));
      Font f = Fonts.Inter.get(17.0F);
      f.draw(this.checkBox.getName(), this.posX + 2.0F, this.yAnimation + this.offset + this.scroll + (this.height - (float)f.getHeight()) / 2.0F, -1);
      float checkBoxX = this.posX + this.width - 20.0F;
      RoundedUtils.drawRound(checkBoxX, this.yAnimation + this.offset + this.scroll + 2.0F, 13.5F, 13.5F, 4.0F, new Color(1117717));
      if ((Boolean)this.checkBox.getValue()) {
         RenderUtils.drawImage(this.CHECKBOX_LOCATION, (double)checkBoxX, (double)(this.yAnimation + this.offset + this.scroll + 2.0F), 13.5D, 13.5D, Color.WHITE.getRGB());
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      float checkBoxX = this.posX + this.width - 20.0F;
      if (mouseX >= (double)checkBoxX && mouseX <= (double)(checkBoxX + 13.5F) && mouseY >= (double)(this.yAnimation + this.offset + this.scroll + 2.0F) && mouseY <= (double)(this.yAnimation + this.offset + this.scroll + 15.5F)) {
         this.checkBox.setValue(!(Boolean)this.checkBox.getValue());
      }

   }

   public void keyPressed(int keyCode) {
   }
}
