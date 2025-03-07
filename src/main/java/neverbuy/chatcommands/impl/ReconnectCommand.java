package neverbuy.chatcommands.impl;

import neverbuy.Constants;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.telegram.connection.ThreadUtil;

@CommandInfo(
   name = "rct",
   usage = ".rct",
   desc = "Автоматически перезаходит на сервер"
)
public class ReconnectCommand extends ChatCommand {
   public void execute(String[] args) {
      try {
         ThreadUtil.runInOtherThread(() -> {
            assert this.mc.player != null;

            String an = Constants.getCurrentAnarchy();
            this.mc.player.sendChatMessage("/hub");
            short DELAY = 500;

            try {
               Thread.sleep((long)DELAY);
            } catch (InterruptedException var4) {
               throw new RuntimeException(var4);
            }

            this.mc.player.sendChatMessage("/an" + an);
         });
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
      }

   }
}
