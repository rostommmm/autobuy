package neverbuy;

import com.google.common.eventbus.EventBus;
import java.io.IOException;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import neverbuy.autobuy.AutoBuy;
import neverbuy.autobuy.list.AutoBuyList;
import neverbuy.chatcommands.ChatCommandManager;
import neverbuy.proxy.Connection;
import neverbuy.telegram.TelegramApi;
import neverbuy.telegram.command.CommandManager;
import neverbuy.util.misc.EventHandler;

public final class Constants {
   public static String VERSION = "2.0";
   public static EventBus EVENT_BUS;
   public static TelegramApi TELEGRAM;
   public static CommandManager COMMAND_MANAGER;
   public static ChatCommandManager CHAT_COMMAND_MANAGER;
   public static boolean TELEGRAM_SCREEN;
   public static boolean DISTRUCTED = false;
   public static boolean DEBUG = false;
   public static boolean WHITETHEME = true;
   public static boolean ALPHA;
   public static BetaUser USER;
   public static EventHandler EVENT_HANDLER;
   public static AutoBuyList LIST;
   public static AutoBuy AUTOBUY;
   public static Connection PROXY;

   public static boolean isOnFunTime() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.getCurrentServerData() == null) {
         return false;
      } else {
         return mc.isSingleplayer() ? false : ((ServerData)Objects.requireNonNull(mc.getCurrentServerData())).serverIP.toLowerCase().contains("funtime");
      }
   }

   public static void copyToClipboard(String text) {
      try {
         String[] cmd = new String[]{"cmd", "/c", "echo", text, "|", "clip"};
         Runtime.getRuntime().exec(cmd);
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String getCurrentAnarchy() {
      Minecraft mc = Minecraft.getInstance();
      String[] split = mc.ingameGUI.getTabList().header.getString().split("Режим: ");
      return split.length < 2 ? "none" : split[1].replaceAll("Анархия-", "");
   }

   private Constants() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
