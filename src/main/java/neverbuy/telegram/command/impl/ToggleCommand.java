package neverbuy.telegram.command.impl;

import neverbuy.Constants;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.api.CommandInfo;

@CommandInfo(
   name = "Автобай",
   usage = "автобай [вкл/выкл]",
   desc = "Включает и выключает автобай!"
)
public class ToggleCommand extends Command {
   public void execute(String[] args) {
      Constants.AUTOBUY.setEnabled(args[1].equalsIgnoreCase("вкл"));
   }
}
