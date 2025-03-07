package neverbuy.gui.component.action;

import com.mojang.blaze3d.matrix.MatrixStack;
import neverbuy.gui.component.Component;

public class ActionButton extends Component {
   Action action;

   public ActionButton(float[] coordinates, Action action) {
      super(coordinates);
      this.action = action;
   }

   public void update(Action action) {
      this.action = action;
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      this.action.render(this, matrixStack, mouseX, mouseY);
   }

   public void mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isHovered(mouseX, mouseY)) {
         this.action.mouseClicked(mouseX, mouseY, button);
      }

   }

   public void keyPressed(int keyCode) {
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return mouseX >= (double)this.posX && mouseX <= (double)(this.posX + this.width) && mouseY >= (double)this.posY && mouseY <= (double)(this.posY + this.height);
   }
}
