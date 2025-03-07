package neverbuy.telegram.command.impl;

import neverbuy.Constants;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Баланс",
   desc = "Отправляет ваш баланс в чат телеграм",
   usage = "баланс"
)
public class BalanceCommand extends Command {
   public void execute(String[] args) {
      this.sendMessage("Текущий баланс: " + Constants.AUTOBUY.getBalance().toString());
   }
}
