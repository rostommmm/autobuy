package neverbuy.util.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import neverbuy.Constants;
import neverbuy.event.EventTick;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.util.animation.Animation;
import neverbuy.util.animation.util.Easings;

public class HUD {
   final Minecraft mc = Minecraft.getInstance();
   final ResourceLocation FPS = new ResourceLocation("minecraft", "neverbuy/hud/fps.png");
   final ResourceLocation WIFI = new ResourceLocation("minecraft", "neverbuy/hud/wifi.png");
   private static final List<HUD.StaffPlayer> staff = new ArrayList();
   private static final Pattern namePattern = Pattern.compile("^\\w{3,16}$");
   private static final Pattern prefixMatches = Pattern.compile(".*(mod|der|adm|help|wne|хелп|адм|поддержка|кура|own|taf|curat|dev|supp|yt|сотруд).*");
   public static List<String> customStaff = new ArrayList();
   private final Animation staffList = (new Animation()).setValue(20.0D);
   private static final Animation widthDynamic = (new Animation()).setValue(100.0D);
   private static float nameWidth = 0.0F;
   private static ITextComponent greaterName = new StringTextComponent("");

   public void renderUserInfo(MatrixStack matrixStack) {
   }

   public void drawWatermark(MatrixStack stack) {
      String text = "Crack Alpha dsc.gg/extz by Fruzz & rlyPasterZ";
      String premium = "Crack";
      float posX = 10.0F;
      float posY = 10.0F;
      Font f = Fonts.VarelaRound.get(18.0F);
      float width = (float)(f.getWidth(text + " " + premium) + 2);
      Color back = new Color(591886);
      Color textColor = new Color(6058857);
      RoundedUtils.drawRound(posX, posY, width + 2.0F, 16.0F, 4.0F, back);
      f.draw(text, posX + 2.0F, posY + 2.0F, textColor.getRGB());
      int startColor = Constants.ALPHA ? (new Color(13831694)).getRGB() : (new Color(16758272)).getRGB();
      int endColor = Constants.ALPHA ? (new Color(14501703)).getRGB() : (new Color(15728384)).getRGB();
      String fps = String.valueOf(Minecraft.getDebugFPS());
      RenderUtils.drawSmoothText(stack, f, premium, posX + 4.0F + (float)f.getWidth(text), posY + 2.0F, startColor, endColor, 1500L);
      RoundedUtils.drawRound(posX + width + 6.0F, posY, (float)(f.getWidth(fps) + 18), 16.0F, 4.0F, back);
      RenderUtils.drawImage(this.FPS, (double)(posX + width + 8.0F), (double)(posY + 2.0F), 12.0D, 12.0D, textColor.getRGB());
      f.draw(fps, posX + width + 21.0F, posY + 3.0F, textColor.getRGB());
      String ping = String.valueOf(this.getPing());
      RoundedUtils.drawRound(posX + (width += (float)(f.getWidth(fps) + 30)), posY, (float)(f.getWidth(ping) + 20), 16.0F, 4.0F, back);
      RenderUtils.drawImage(this.WIFI, (double)(posX + width + 2.0F), (double)(posY + 2.0F), 12.0D, 12.0D, textColor.getRGB());
      f.draw(ping, posX + width + 15.0F, posY + 3.0F, textColor.getRGB());
   }

   public void renderStaffList(MatrixStack matrixStack) {
      this.staffList.update();
      widthDynamic.update();
      float height = this.staffList.getValue().floatValue();
      float width1 = Math.max(nameWidth + 30.0F, 120.0F);
      float offset = height;

      for(Iterator var5 = HUD.staff.iterator(); var5.hasNext(); offset += 15.0F) {
         HUD.StaffPlayer staff = (HUD.StaffPlayer)var5.next();
      }

      widthDynamic.animate((double)width1, 0.5D, Easings.SINE_OUT);
      this.staffList.animate((double)offset, 0.5D, Easings.SINE_OUT);
   }

   private static boolean contains(String name) {
      Iterator var1 = HUD.staff.iterator();

      HUD.StaffPlayer staff;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         staff = (HUD.StaffPlayer)var1.next();
      } while(!staff.name.contains(name));

      return true;
   }

   public void onUpdate(EventTick e) {
      staff.clear();
   }

   private long getPing() {
      assert this.mc.player != null;

      return this.mc.player.connection.getPlayerInfo(this.mc.player.getUniqueID()) == null ? -1L : (long)((NetworkPlayerInfo)Objects.requireNonNull(((ClientPlayNetHandler)Objects.requireNonNull(this.mc.getConnection())).getPlayerInfo(this.mc.player.getUniqueID()))).getResponseTime();
   }

   public static class StaffPlayer {
      ITextComponent prefix;
      public String name;
      HUD.Status status;
      TextFormatting color;

      public StaffPlayer(ITextComponent prefix, String name, HUD.Status status, TextFormatting color) {
         this.prefix = prefix;
         this.name = name;
         this.status = status;
         this.color = color;
      }
   }

   private static enum Status {
      SPEC(new Color(16711680)),
      NONE(new Color(5766912)),
      VANISH(new Color(16429580));

      public final Color color;

      private Status(Color color) {
         this.color = color;
      }

      // $FF: synthetic method
      private static HUD.Status[] $values() {
         return new HUD.Status[]{SPEC, NONE, VANISH};
      }
   }
}
