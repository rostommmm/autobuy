package neverbuy.telegram.command.impl;

import net.minecraft.util.ScreenShotHelper;
import neverbuy.Constants;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.util.misc.ChatUtil;

@CommandInfo(
   name = "Скрин",
   usage = "скрин",
   desc = "Скриншотит игру и отправляет в чат телеграма"
)
public class ScreenshotCommand extends Command {
   public void execute(String[] args) {
      Constants.TELEGRAM_SCREEN = true;
      ScreenShotHelper.saveScreenshot(this.mc.gameDir, this.mc.getMainWindow().getFramebufferWidth(), this.mc.getMainWindow().getFramebufferHeight(), this.mc.getFramebuffer(), (p_lambda$onKeyEvent$3_1_) -> {
         ChatUtil.addMessage("Успешно заскринил!");
      });
   }
}
