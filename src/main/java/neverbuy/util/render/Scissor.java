package neverbuy.util.render;

import com.google.common.collect.Lists;
import java.awt.Rectangle;
import java.util.List;
import net.minecraft.client.MainWindow;
import neverbuy.util.IUtil;
import org.lwjgl.opengl.GL11;

public final class Scissor implements IUtil {
   private static Scissor.State state = new Scissor.State();
   private static final List<Scissor.State> stateStack = Lists.newArrayList();

   public static void push() {
      stateStack.add(state.clone());
      GL11.glPushAttrib(524288);
   }

   public static void pop() {
      state = (Scissor.State)stateStack.remove(stateStack.size() - 1);
      GL11.glPopAttrib();
   }

   public static void unset() {
      GL11.glDisable(3089);
      state.enabled = false;
   }

   public static void setFromComponentCoordinates(int x, int y, int width, int height) {
      MainWindow res = mc.getMainWindow();
      int scaleFactor = 2;
      int screenX = x * scaleFactor;
      int screenY = y * scaleFactor;
      int screenWidth = width * scaleFactor;
      int screenHeight = height * scaleFactor;
      screenY = mc.getMainWindow().getHeight() - screenY - screenHeight;
      set(screenX, screenY, screenWidth, screenHeight);
   }

   public static void setFromComponentCoordinates(double x, double y, double width, double height) {
      MainWindow res = mc.getMainWindow();
      int scaleFactor = (int)res.getGuiScaleFactor();
      int screenX = (int)(x * (double)scaleFactor);
      int screenY = (int)(y * (double)scaleFactor);
      int screenWidth = (int)(width * (double)scaleFactor);
      int screenHeight = (int)(height * (double)scaleFactor);
      screenY = mc.getMainWindow().getHeight() - screenY - screenHeight;
      set(screenX, screenY, screenWidth, screenHeight);
   }

   public static void setFromComponentCoordinates(double x, double y, double width, double height, float scale) {
      MainWindow res = mc.getMainWindow();
      float halfAnimationValueRest = (1.0F - scale) / 2.0F;
      double testX = x + width * (double)halfAnimationValueRest;
      double testY = y + height * (double)halfAnimationValueRest;
      double testW = width * (double)scale;
      double testH = height * (double)scale;
      testX = testX * (double)scale + ((double)mc.getMainWindow().getScaledWidth() - testW) * (double)halfAnimationValueRest;
      float scaleFactor = 2.0F;
      int screenX = (int)(testX * (double)scaleFactor);
      int screenY = (int)(testY * (double)scaleFactor);
      int screenWidth = (int)(testW * (double)scaleFactor);
      int screenHeight = (int)(testH * (double)scaleFactor);
      screenY = mc.getMainWindow().getHeight() - screenY - screenHeight;
      set(screenX, screenY, screenWidth, screenHeight);
   }

   public static void set(int x, int y, int width, int height) {
      Rectangle screen = new Rectangle(0, 0, mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());
      Rectangle current;
      if (state.enabled) {
         current = new Rectangle(state.x, state.y, state.width, state.height);
      } else {
         current = screen;
      }

      Rectangle target = new Rectangle(x + state.transX, y + state.transY, width, height);
      Rectangle result = current.intersection(target);
      result = result.intersection(screen);
      if (result.width < 0) {
         result.width = 0;
      }

      if (result.height < 0) {
         result.height = 0;
      }

      state.enabled = true;
      state.x = result.x;
      state.y = result.y;
      state.width = result.width;
      state.height = result.height;
      GL11.glEnable(3089);
      GL11.glScissor(result.x, result.y, result.width, result.height);
   }

   public static void translate(int x, int y) {
      state.transX = x;
      state.transY = y;
   }

   public static void translateFromComponentCoordinates(int x, int y) {
      MainWindow res = mc.getMainWindow();
      int totalHeight = res.getScaledHeight();
      int scaleFactor = (int)res.getGuiScaleFactor();
      int screenX = x * scaleFactor;
      int screenY = y * scaleFactor;
      screenY = totalHeight * scaleFactor - screenY;
      translate(screenX, screenY);
   }

   private Scissor() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   private static class State implements Cloneable {
      public boolean enabled;
      public int transX;
      public int transY;
      public int x;
      public int y;
      public int width;
      public int height;

      public Scissor.State clone() {
         try {
            return (Scissor.State)super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new AssertionError(var2);
         }
      }
   }
}
