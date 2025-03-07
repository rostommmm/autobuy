package neverbuy.chatcommands;

import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import neverbuy.Constants;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.chatcommands.impl.AFKCommand;
import neverbuy.chatcommands.impl.AutoBuyCommand;
import neverbuy.chatcommands.impl.CloudCommand;
import neverbuy.chatcommands.impl.DistructCommand;
import neverbuy.chatcommands.impl.ReconnectCommand;
import neverbuy.chatcommands.impl.SessionCommand;
import neverbuy.event.EventChat;
import neverbuy.util.misc.ChatUtil;

public class ChatCommandManager {
   private final List<ChatCommand> chatCommands = new ArrayList();

   public ChatCommandManager() {
      this.chatCommands.add(new AutoBuyCommand());
      this.chatCommands.add(new ReconnectCommand());
      this.chatCommands.add(new DistructCommand());
      this.chatCommands.add(new CloudCommand());
      this.chatCommands.add(new SessionCommand());
      if (Constants.ALPHA) {
         AFKCommand afkCommand = new AFKCommand();
         Constants.EVENT_BUS.register(afkCommand);
         this.chatCommands.add(afkCommand);
      }

   }

   @Subscribe
   public void onChat(EventChat e) {
      if (!Constants.DISTRUCTED) {
         if (e.getMsg().startsWith(".")) {
            e.cancel();
            String var2 = e.getMsg().substring(1);
            String[] strings = var2.split(" ");
            Iterator var4 = this.chatCommands.iterator();

            while(var4.hasNext()) {
               ChatCommand command = (ChatCommand)var4.next();
               if (command.getName().equalsIgnoreCase(strings[0])) {
                  if (!(command instanceof CloudCommand) && !(command instanceof AutoBuyCommand) && strings.length < command.getArgs()) {
                     int var10000 = command.getArgs();
                     ChatUtil.addMessage("Недостаточно аргументов. Ожидается: " + var10000 + ". Получено: " + strings.length);
                     return;
                  }

                  command.execute(strings);
               }
            }

         }
      }
   }

   public List<ChatCommand> getChatCommands() {
      return this.chatCommands;
   }
}
