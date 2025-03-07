package neverbuy.telegram.command.impl;

import neverbuy.Constants;
import neverbuy.autobuy.api.CheckBox;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Настройка",
   usage = "настройка [название_настройки] [вкл/выкл]",
   desc = "Изменяет настройки автобая"
)
public class ChangeOptionCommand extends Command {
   public void execute(String[] args) {
      String name = args[1];
      String action = args[2];
      CheckBox checkbox = Constants.AUTOBUY.getCheckboxByName(name);
      if (checkbox == null) {
         this.sendMessage("Настройка \"" + name + "\" не найдена!");
      } else {
         String var5 = action.toLowerCase();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case 1066739:
            if (var5.equals("вкл")) {
               var6 = 0;
            }
            break;
         case 33086298:
            if (var5.equals("выкл")) {
               var6 = 1;
            }
         }

         switch(var6) {
         case 0:
            if ((Boolean)checkbox.getValue()) {
               this.sendMessage("Настройка \"" + checkbox.getName() + "\" уже включена!");
               return;
            }

            checkbox.setValue(true);
            this.sendMessage("Настройка \"" + checkbox.getName() + "\" успешно включена!");
            break;
         case 1:
            if (!(Boolean)checkbox.getValue()) {
               this.sendMessage("Настройка \"" + checkbox.getName() + "\" уже выключена!");
               return;
            }

            checkbox.setValue(false);
            this.sendMessage("Настройка \"" + checkbox.getName() + "\" успешно выключена!");
         }

      }
   }
}
