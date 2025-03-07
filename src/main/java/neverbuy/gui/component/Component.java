package neverbuy.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import neverbuy.gui.GuiScreen;

public abstract class Component {
   public float posX;
   public float posY;
   public float width;
   public float height;
   public static GuiScreen guiScreen;

   public Component(float[] coordinates) {
      this.posX = coordinates[0];
      this.posY = coordinates[1];
      this.width = coordinates[2];
      this.height = coordinates[3];
   }

   public abstract void render(MatrixStack var1, int var2, int var3);

   public abstract void mouseClicked(double var1, double var3, int var5);

   public abstract void keyPressed(int var1);
}
