package neverbuy.telegram.command.impl;

import java.math.BigDecimal;
import neverbuy.Constants;
import neverbuy.autobuy.AutoBuy;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Статистика",
   usage = "статистика [покупка/продажа/прибыль/общ]",
   desc = "Показывает статистику"
)
public class StatsCommand extends Command {
   public void execute(String[] args) {
      AutoBuy autoBuy = Constants.AUTOBUY;
      if (args[1].equalsIgnoreCase("покупка")) {
         this.sendMessage("Статистика покупки: " + autoBuy.getBuyBalance());
      } else if (args[1].equalsIgnoreCase("продажа")) {
         this.sendMessage("Статистика покупки: " + autoBuy.getSellBalance());
      } else if (args[1].equalsIgnoreCase("прибыль")) {
         double var10001 = (double)(autoBuy.getSellBalance().floatValue() - autoBuy.getBuyBalance().floatValue());
         this.sendMessage("Статистика прибыли: " + BigDecimal.valueOf(var10001));
      } else if (args[1].startsWith("общ")) {
         String msg = "Общая статистика:\n";
         msg = msg + "Покупки: " + autoBuy.getBuyBalance() + "\n";
         msg = msg + "Продажи: " + autoBuy.getSellBalance() + "\n";
         msg = msg + "Прибыль: " + BigDecimal.valueOf((double)(autoBuy.getSellBalance().floatValue() - autoBuy.getBuyBalance().floatValue())) + "\n";
         this.sendMessage(msg);
      }

   }
}
