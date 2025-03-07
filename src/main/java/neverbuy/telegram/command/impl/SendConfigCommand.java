package neverbuy.telegram.command.impl;

import neverbuy.Constants;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Конфиг",
   desc = "Загружает или отправляет конфиг",
   usage = "конфиг [получить/загрузить]"
)
public class SendConfigCommand extends Command {
   public void execute(String[] args) {
      if (args[1].equalsIgnoreCase("получить")) {
         Constants.TELEGRAM.sendDocument();
      }

   }
}
