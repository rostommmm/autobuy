package neverbuy.event;

import com.mojang.blaze3d.matrix.MatrixStack;

public class EventRenderOverlay {
   private MatrixStack matrixStack;

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public EventRenderOverlay(MatrixStack matrixStack) {
      this.matrixStack = matrixStack;
   }
}
