package neverbuy.chatcommands.impl;

import com.google.common.eventbus.Subscribe;
import java.util.concurrent.TimeUnit;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.event.EventTick;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.util.misc.ChatUtil;

@CommandInfo(
   name = "afk",
   desc = "Входит в режим афк",
   usage = ".afk"
)
public class AFKCommand extends ChatCommand {
   private boolean activeAfk;
   private long afkTime = -1L;
   private float lastYaw;
   private float lastPitch;

   public void execute(String[] args) {
      this.activeAfk = true;
      ChatUtil.addMessage("Вы успешно вошли в режим афк!");
      this.mc.player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, Integer.MAX_VALUE, 255));
      this.mc.player.rotationPitch = 90.0F;
      this.lastPitch = this.mc.player.rotationPitch;
      this.lastYaw = this.mc.player.rotationYaw;
      this.afkTime = System.currentTimeMillis();
   }

   @Subscribe
   public void onTick(EventTick ignored) {
      if (this.activeAfk) {
         assert this.mc.player != null;

         Vector3d motion = this.mc.player.getMotion();
         if (motion.x != 0.0D || motion.z != 0.0D || this.lastYaw != this.mc.player.rotationYaw || this.lastPitch != this.mc.player.rotationPitch) {
            this.activeAfk = false;
            long current = System.currentTimeMillis();
            long elapsedMillis = current - this.afkTime;
            long hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60L;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60L;
            ChatUtil.addMessage(String.format("Вы успешно вышли из режима АФК! Вы простояли %d часов, %d минут, %d секунд!", hours, minutes, seconds));
            this.mc.player.removeActivePotionEffect(Effects.BLINDNESS);
         }
      }

   }
}
