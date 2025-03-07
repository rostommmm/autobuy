package neverbuy.chatcommands.impl;

import automyst.nedan.discord.DiscordRPC;
import java.io.File;
import net.optifine.shaders.Shaders;
import neverbuy.Constants;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.telegram.connection.ThreadUtil;
import neverbuy.util.misc.ChatUtil;

@CommandInfo(
   name = "distruct",
   usage = ".distruct [setup/cancel]",
   desc = "Подготавливает игру для прорверки"
)
public class DistructCommand extends ChatCommand {
   private boolean canceled;

   public void execute(String[] args) {
      if (args[1].equalsIgnoreCase("setup")) {
         this.canceled = false;
         ThreadUtil.runInOtherThread(() -> {
            ChatUtil.addMessage("Софт уничтожится через 10 секунд");
            ChatUtil.addMessage("Чтобы отменить напиши .distruct cancel");

            try {
               Thread.sleep(10000L);
            } catch (InterruptedException var2) {
               throw new RuntimeException(var2);
            }

            if (!this.canceled) {
               this.onDistruct();
            }

         });
      } else if (args[1].equalsIgnoreCase("cancel")) {
         this.canceled = true;
         ChatUtil.addMessage("Успешно остановил уничтожение!");
      }

   }

   private void onDistruct() {
      this.mc.ingameGUI.getChatGUI().clearChatMessages(true);
      Constants.DISTRUCTED = true;
      DiscordRPC.INSTANCE.Discord_Shutdown();
      File file = new File(System.getenv("appdata") + "\\.minecraft");
      mc.gameDir = file;
      mc.fileResourcepacks = new File(file.getAbsolutePath() + "\\resourcepacks");
      ChatUtil.addMessage(this.mc.fileResourcepacks);
      Shaders.shaderPacksDir = new File(file.getAbsolutePath() + "\\shaderpacks");
   }
}
