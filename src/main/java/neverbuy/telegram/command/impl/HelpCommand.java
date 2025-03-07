package neverbuy.telegram.command.impl;

import java.util.Iterator;
import neverbuy.Constants;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Помощь",
   usage = "помощь",
   desc = "Отправляет это сообщение"
)
public class HelpCommand extends Command {
   public void execute(String[] args) {
      StringBuilder msg = new StringBuilder("Команды телеграм:");
      Iterator var3 = Constants.COMMAND_MANAGER.getCommands().iterator();

      while(var3.hasNext()) {
         Command command = (Command)var3.next();
         msg.append("\n\nНазвание: ").append(command.getName()).append("\nИспользование: ").append(command.getUsage()).append("\nИнформация: ").append(command.getDesc());
      }

      this.sendMessage(msg.toString());
   }
}
