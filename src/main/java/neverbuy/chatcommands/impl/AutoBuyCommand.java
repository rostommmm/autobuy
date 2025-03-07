package neverbuy.chatcommands.impl;

import java.io.File;
import neverbuy.ConfigManager;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.util.misc.ChatUtil;

@CommandInfo(
   name = "autoBuy",
   desc = "Сохраняет и загружает конфиг",
   usage = ".autobuy [save/load] config"
)
public class AutoBuyCommand extends ChatCommand {
   public void execute(String[] args) {
      String n;
      File file;
      if (args[1].equalsIgnoreCase("save")) {
         n = args[2];
         file = new File("C:\\NeverBuy\\config\\" + n + ".json");
         if (file.exists()) {
            ChatUtil.addMessage("Конфиг \"" + n + "\" успешно перезаписан!");
         } else {
            ChatUtil.addMessage("Конфиг \"" + n + "\" успешно сохранён!");
         }

         ConfigManager.save(n);
      } else if (args[1].equalsIgnoreCase("load")) {
         n = args[2];
         file = new File("C:\\NeverBuy\\config\\" + n + ".json");
         if (!file.exists()) {
            ChatUtil.addMessage("Конфиг \"" + n + "\" не найден!");
            return;
         }

         ChatUtil.addMessage("Конфиг \"" + n + "\" успешно загружен!");
         ConfigManager.read(n);
      } else if (args[1].equalsIgnoreCase("dir")) {
         try {
            File file2 = new File("C:\\NeverBuy\\config\\");
            Runtime.getRuntime().exec("cmd /c start " + file2.getAbsolutePath());
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }

   }
}
