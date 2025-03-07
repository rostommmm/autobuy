package neverbuy.telegram.command.impl;

import java.math.BigDecimal;
import neverbuy.Constants;
import neverbuy.autobuy.api.Delay;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Задержка",
   usage = "задержка [название_задержки] [значение]",
   desc = "Изменяет задержки автобая"
)
public class ChangeDelayCommand extends Command {
   public void execute(String[] args) {
      String name = args[1];
      String strValue = args[2];
      if (ChangePriceCommand.isStringOnlyDigits(strValue)) {
         this.sendMessage("Вы ввели не верную задержку!");
      } else {
         Delay delay = Constants.AUTOBUY.getDelayByName(name);
         BigDecimal value = new BigDecimal(strValue);
         if (delay == null) {
            this.sendMessage("Задержка с названием \"" + name + "\" не найдена!");
         } else {
            if (delay.getDelay() == value.floatValue()) {
               this.sendMessage(String.format("Уже установлена задержка %s для \"%s\"", strValue, delay.getName()));
            } else {
               delay.setValue(value);
               this.sendMessage(String.format("Успешно установлена задержка %s для \"%s\"", strValue, delay.getName()));
            }

         }
      }
   }
}
