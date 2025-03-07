package neverbuy.gui.component.action;

import com.mojang.blaze3d.matrix.MatrixStack;

public interface Action {
   void render(ActionButton var1, MatrixStack var2, int var3, int var4);

   void mouseClicked(double var1, double var3, int var5);
}
