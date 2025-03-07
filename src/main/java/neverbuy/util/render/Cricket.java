package neverbuy.util.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector4f;

public class Cricket {/*
   private Vector2f position;
   private final Vector2f velocity;
   private List<Cricket.CricketTrail> trails = new ArrayList();

   public Cricket(Vector2f pos) {
      ThreadLocalRandom localRandom = ThreadLocalRandom.current();
      this.position = pos;
      this.velocity = new Vector2f(localRandom.nextFloat(-0.5F, 0.5F), localRandom.nextFloat(-0.5F, 0.5F));
   }

   public void update() {
      Vector2f localPos = this.position.add(this.velocity);
      MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
      Vector2f var10000;
      if (localPos.x <= 0.0F) {
         var10000 = this.velocity;
         var10000.x += 5.0F;
      } else if (localPos.y <= 0.0F) {
         var10000 = this.velocity;
         var10000.y += 5.0F;
      } else if (localPos.x >= (float)mainWindow.getScaledWidth()) {
         var10000 = this.velocity;
         var10000.x -= 5.0F;
      } else if (localPos.y >= (float)mainWindow.getScaledHeight()) {
         var10000 = this.velocity;
         var10000.y -= 5.0F;
      }

      if (this.velocity.x >= 0.0F) {
         var10000 = this.velocity;
         var10000.x += 0.005F;
      } else {
         var10000 = this.velocity;
         var10000.x -= 0.005F;
      }

      if (this.velocity.y >= 0.0F) {
         var10000 = this.velocity;
         var10000.y += 0.005F;
      } else {
         var10000 = this.velocity;
         var10000.y -= 0.005F;
      }

      this.position = this.position.add(this.velocity);
   }

   public void render(MatrixStack stack) {
      this.update();
      this.trails.removeIf((cricketTrail) -> {
         return System.currentTimeMillis() - cricketTrail.time >= 500L;
      });
      this.trails.add(new Cricket.CricketTrail(this, 0.0F));

      Cricket.CricketTrail trail;
      for(Iterator var2 = this.trails.iterator(); var2.hasNext(); trail.render(stack)) {
         trail = (Cricket.CricketTrail)var2.next();
         if (trail.isCollided()) {
            Vector2f var10000 = this.velocity;
            var10000.x += trail.parent.velocity.x - this.velocity.x;
            var10000 = this.velocity;
            var10000.y += trail.parent.velocity.y - this.velocity.y;
         }
      }

   }

   private static class CricketTrail {
      private Vector2f pos;
      private float offset;
      private long time;
      private Cricket parent;

      public CricketTrail(Cricket parent, float offset) {
         this.pos = parent.position;
         this.offset = offset;
         this.time = System.currentTimeMillis();
         this.parent = parent;
      }

      public void render(MatrixStack matrixStack) {
         Vector4f colors = new Vector4f((float)ColorHelper.gradient(4, 0, new Color(65489), new Color(16384255)), (float)ColorHelper.gradient(4, 90, new Color(65489), new Color(16384255)), (float)ColorHelper.gradient(4, 180, new Color(65489), new Color(16384255)), (float)ColorHelper.gradient(4, 270, new Color(65489), new Color(16384255)));
         int l = this.parent.trails.indexOf(this);
         RoundedUtils.drawRoundedGradientBlurredRect((double)(this.pos.x - this.offset), (double)this.pos.y, 2.5D, 2.5D, 2.0D, (float)l / 2.0F, this.colorFrom(colors.getX()), this.colorFrom(colors.getY()), this.colorFrom(colors.getZ()), this.colorFrom(colors.getW()));
      }

      public boolean isCollided() {
         Iterator var1 = this.parent.trails.iterator();

         while(var1.hasNext()) {
            Cricket.CricketTrail cricket = (Cricket.CricketTrail)var1.next();
            if (cricket != this) {
               int l = this.parent.trails.indexOf(cricket);
               if (this.getPos(this) >= this.getPos(cricket) && this.getPos(this) <= this.getWidth(cricket) && this.pos.y >= cricket.pos.y && this.pos.y <= cricket.pos.y + 2.5F + (float)l / 2.0F) {
                  return true;
               }
            }
         }

         return false;
      }

      private float getPos(Cricket.CricketTrail trail) {
         return trail.pos.x;
      }

      private float getWidth(Cricket.CricketTrail trail) {
         int l = this.parent.trails.indexOf(trail);
         return trail.pos.x + 2.5F + (float)l / 2.0F;
      }

      private Color colorFrom(float val) {
         return ColorHelper.getColor((int)val);
      }
   }*/
}
