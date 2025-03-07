package neverbuy.telegram.command.impl;

import neverbuy.Constants;
import neverbuy.autobuy.AutoBuy;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Уведомления",
   desc = "Включает и выключает уведомления в телеграме",
   usage = "уведомления [покупка/продажа/пропуск] [вкл/выкл]"
)
public class NotificationCommand extends Command {
   public void execute(String[] args) {
      AutoBuy autoBuy = Constants.AUTOBUY;
      String n = args[1];
      String action = args[2];
      boolean enabled;
      if (action.equalsIgnoreCase("вкл")) {
         enabled = true;
      } else {
         if (!action.equalsIgnoreCase("выкл")) {
            this.sendMessage(String.format("Напишите \"вкл\" или \"выкл\" вместо \"%s\"", action));
            return;
         }

         enabled = false;
      }

      String act = "успешно " + (enabled ? "включены!" : "выключены!");
      if (n.equalsIgnoreCase("покупка")) {
         autoBuy.buyNotification = enabled;
         this.sendMessage("Уведомления о покупке предметов " + act);
      } else if (n.equalsIgnoreCase("продажа")) {
         autoBuy.sellNotification = enabled;
         this.sendMessage("Уведомления о продаже предметов " + act);
      } else if (n.equalsIgnoreCase("пропуск")) {
         autoBuy.notBuyNotification = enabled;
         this.sendMessage("Уведомления о пропуске предметов " + act);
      } else {
         this.sendMessage(String.format("Не нашёл настройки \"%s\"", n));
      }

   }
}
