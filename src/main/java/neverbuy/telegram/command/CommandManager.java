package neverbuy.telegram.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import neverbuy.Constants;
import neverbuy.telegram.TelegramApi;
import neverbuy.telegram.command.api.Command;
import neverbuy.telegram.command.impl.BalanceCommand;
import neverbuy.telegram.command.impl.ChangeDelayCommand;
import neverbuy.telegram.command.impl.ChangeOptionCommand;
import neverbuy.telegram.command.impl.ChangePriceCommand;
import neverbuy.telegram.command.impl.ChatCommand;
import neverbuy.telegram.command.impl.HelpCommand;
import neverbuy.telegram.command.impl.NotificationCommand;
import neverbuy.telegram.command.impl.ScreenshotCommand;
import neverbuy.telegram.command.impl.SendConfigCommand;
import neverbuy.telegram.command.impl.StatsCommand;
import neverbuy.telegram.command.impl.ToggleCommand;

public class CommandManager {
   private final List<Command> commands = new ArrayList();

   public CommandManager() {
      this.commands.add(new ScreenshotCommand());
      this.commands.add(new ChatCommand());
      this.commands.add(new SendConfigCommand());
      this.commands.add(new HelpCommand());
      this.commands.add(new ChangePriceCommand());
      this.commands.add(new ChangeDelayCommand());
      this.commands.add(new ChangeOptionCommand());
      this.commands.add(new BalanceCommand());
      this.commands.add(new StatsCommand());
      this.commands.add(new ToggleCommand());
      this.commands.add(new NotificationCommand());
   }

   public void reply(String msg) {
      try {
         String[] args = msg.split(" ");
         Iterator var3 = this.commands.iterator();

         while(var3.hasNext()) {
            Command command = (Command)var3.next();
            if (command.getName().equalsIgnoreCase(args[0])) {
               if (!(command instanceof ChatCommand) && args.length < command.getArgs()) {
                  TelegramApi var10000 = Constants.TELEGRAM;
                  int var10001 = command.getArgs();
                  var10000.sendMessage("Недостаточно аргументов. Ожидается: " + var10001 + ". Получено: " + args.length);
                  return;
               }

               command.execute(args);
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace(System.err);
      }

   }

   public List<Command> getCommands() {
      return this.commands;
   }
}
