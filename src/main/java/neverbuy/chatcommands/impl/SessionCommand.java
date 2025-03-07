package neverbuy.chatcommands.impl;

import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.util.misc.ChatUtil;

@CommandInfo(
   name = "SetUsername",
   desc = "Устанавливает игровой никнейм",
   usage = ".setUsername [nickname]"
)
public class SessionCommand extends ChatCommand {
   public void execute(String[] args) {
      try {
         if (args.length < 2) {
            ChatUtil.addMessage(this.getUsage());
            return;
         }

         this.mc.getSession().setUsername(args[1]);
         ChatUtil.addMessage("Успешно установил ник: " + this.mc.getSession().getUsername());
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
      }

   }
}
