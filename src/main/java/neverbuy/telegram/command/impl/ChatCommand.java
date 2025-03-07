package neverbuy.telegram.command.impl;

import java.util.Arrays;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Чат",
   desc = "Пишет в чат майнкрафт",
   usage = "чат [сообщение]"
)
public class ChatCommand extends Command {
   public void execute(String[] args) {
      if (args.length == 1) {
         this.sendMessage("Напишите сообщение.");
      } else if (this.mc.player != null) {
         String msg = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 1, args.length));
         this.mc.player.sendChatMessage(msg);
      }
   }
}
