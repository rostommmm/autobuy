package neverbuy.telegram.command.impl;

import java.math.BigDecimal;
import neverbuy.Constants;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Изменить",
   desc = "Изменяет цену предмета",
   usage = "изменить [продажа/покупка] [название_предмета] [цена]"
)
public class ChangePriceCommand extends Command {
   public void execute(String[] args) {
      String name = args[2];
      String price = args[3];
      AutoBuyItem<?> autoBuyItem = Constants.LIST.getAnyItem(name);
      if (isStringOnlyDigits(price)) {
         this.sendMessage("Вы ввели не верную цену!");
      } else if (autoBuyItem == null) {
         this.sendMessage("Предмет \"" + name + "\" не найден!");
      } else {
         BigDecimal newPrice = new BigDecimal(price);
         String var6 = args[1].toLowerCase();
         byte var7 = -1;
         switch(var6.hashCode()) {
         case 408177485:
            if (var6.equals("покупка")) {
               var7 = 0;
            }
            break;
         case 468668467:
            if (var6.equals("продажа")) {
               var7 = 1;
            }
         }

         String var10001;
         switch(var7) {
         case 0:
            autoBuyItem.price(newPrice);
            var10001 = autoBuyItem.getName();
            this.sendMessage("Вы успешно установили цену покупки предмета \"" + var10001 + "\" на " + newPrice);
            break;
         case 1:
            autoBuyItem.sell(newPrice);
            var10001 = autoBuyItem.getName();
            this.sendMessage("Вы успешно установили цену продажи предмета \"" + var10001 + "\" на " + newPrice);
         }

      }
   }

   public static boolean isStringOnlyDigits(String str) {
      char[] var1 = str.toCharArray();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         char codePoint = var1[var3];
         if (!Character.isDigit(codePoint)) {
            return true;
         }
      }

      return false;
   }
}
