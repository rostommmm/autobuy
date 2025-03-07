package neverbuy.event;

import com.mojang.blaze3d.matrix.MatrixStack;

public class EventRenderScreen {
   private MatrixStack matrixStack;
   private Object screen;

   public EventRenderScreen(MatrixStack matrixStack, Object screen) {
      this.matrixStack = matrixStack;
      this.screen = screen;
   }

   public MatrixStack getMatrixStack() {
      return this.matrixStack;
   }

   public Object getScreen() {
      return this.screen;
   }
}
