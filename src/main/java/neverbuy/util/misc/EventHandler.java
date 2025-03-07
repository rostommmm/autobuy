package neverbuy.util.misc;

import com.google.common.eventbus.Subscribe;
import java.awt.Color;
import neverbuy.Constants;
import neverbuy.event.EventKey;
import neverbuy.event.EventRenderOverlay;
import neverbuy.event.EventTick;
import neverbuy.gui.GuiScreen;
import neverbuy.util.IUtil;
import neverbuy.util.render.ColorHelper;
import neverbuy.util.render.HUD;

public class EventHandler implements IUtil {
   public boolean isAfk;
   private final TimerUtil afkTimer = new TimerUtil();
   private final HUD hud = new HUD();

   @Subscribe
   public void onKey(EventKey e) {
      if (!Constants.DISTRUCTED) {
         if (mc.currentScreen == null && e.getKey() == 345) {
            mc.displayGuiScreen(new GuiScreen());
         }

      }
   }

   @Subscribe
   public void onUpdate(EventTick e) {
      if (mc.player.getMotion().x != 0.0D || mc.player.getMotion().z != 0.0D) {
         this.afkTimer.update();
      }

      this.isAfk = this.afkTimer.hasPassed(300000.0F);
   }

   @Subscribe
   public void onRenderOverlay(EventRenderOverlay e) {
      if (!Constants.DISTRUCTED) {
         this.hud.renderUserInfo(e.getMatrixStack());
         this.hud.drawWatermark(e.getMatrixStack());
      }
   }

   private Color colorFrom(float val) {
      return ColorHelper.getColor((int)val);
   }

   private int gradient(int index, Color... colors) {
      return ColorHelper.gradient(4, index, colors);
   }
}
