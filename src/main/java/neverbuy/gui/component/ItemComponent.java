package neverbuy.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.item.ItemStack;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;

public class ItemComponent extends Component {
   public final AutoBuyItem<?> autoBuyItem;
   public final float[] offsets;
   float yAnimation;
   public ItemBoard itemBoard;

   public ItemComponent(float[] coordinates, AutoBuyItem<?> autoBuyItem) {
      super(coordinates);
      this.autoBuyItem = autoBuyItem;
      this.offsets = new float[]{coordinates[4], coordinates[5], 0.0F};
      this.itemBoard = new ItemBoard(autoBuyItem, new float[]{guiScreen.posX + guiScreen.width + 10.0F, this.posY, 125.0F, 130.0F});
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.yAnimation = RenderUtils.lerp(this.yAnimation, this.posY, 8.0F);
      float offsetX = this.offsets[0];
      float offsetY = this.offsets[1];
      float scroll = this.offsets[2];
      RoundedUtils.drawRound(this.posX + offsetX, this.yAnimation + offsetY + scroll, this.width, this.height, 4.0F, new Color(2236962));
      ItemStack stack = this.autoBuyItem.isContainsStack() ? this.autoBuyItem.getStack() : this.autoBuyItem.getItem().getDefaultInstance();
      RenderUtils.renderItem(stack, (int)(this.posX + offsetX + 1.0F), (int)(this.yAnimation + offsetY + 1.0F + scroll));
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isHovered(mouseX, mouseY) && !this.itemBoard.opened) {
         this.openBoard();
      }

   }

   public void openBoard() {
      this.itemBoard.open();
      guiScreen.boards.add(this.itemBoard);
   }

   public void keyPressed(int keyCode) {
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return mouseX >= (double)(this.posX + this.offsets[0]) && mouseY >= (double)(this.posY + this.offsets[1] + this.offsets[2]) && mouseX <= (double)(this.posX + this.width + this.offsets[0]) && mouseY <= (double)(this.posY + this.offsets[1] + this.height + this.offsets[2]);
   }
}
