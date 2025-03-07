package neverbuy.gui.component.itemsetting;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import neverbuy.autobuy.list.item.setting.BoolSetting;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;

public class BoolSettingComponent extends ItemSettingComponent {
   ResourceLocation CHECKBOX_LOCATION = new ResourceLocation("minecraft", "neverbuy/checkbox.png");

   public BoolSettingComponent(float[] coordinates, BoolSetting boolSetting) {
      super(coordinates, boolSetting);
      this.offset = coordinates[4];
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      if (this.itemSetting.isVisible()) {
         Font f = Fonts.Inter.get(12.0F);
         f.draw(this.itemSetting.getName(), this.posX + 2.0F, this.posY + this.offset + 2.0F, -1);
         float checkBoxX = this.posX + this.width - 15.0F;
         RoundedUtils.drawRound(checkBoxX, this.posY + this.offset + 0.5F, 10.5F, 10.5F, 4.0F, new Color(1117717));
         if ((Boolean)this.itemSetting.getAsBool().getVal()) {
            RenderUtils.drawImage(this.CHECKBOX_LOCATION, (double)checkBoxX, (double)(this.posY + this.offset + 0.5F), 10.5D, 10.5D, Color.WHITE.getRGB());
         }

      }
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.itemSetting.isVisible()) {
         float checkBoxX = this.posX + this.width - 15.0F;
         if (mouseX >= (double)checkBoxX && mouseY >= (double)(this.posY + this.offset) && mouseX <= (double)(checkBoxX + 10.5F) && mouseY <= (double)(this.posY + this.offset + 10.5F)) {
            this.itemSetting.getAsBool().setVal(!(Boolean)this.itemSetting.getAsBool().getVal());
         }

      }
   }

   public void keyPressed(int keyCode) {
   }
}
