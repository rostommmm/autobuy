package neverbuy;

import automyst.nedan.discord.rpc.Discord;
import com.google.common.eventbus.EventBus;
import net.minecraft.util.Util;
import neverbuy.autobuy.AutoBuy;
import neverbuy.autobuy.list.AutoBuyList;
import neverbuy.chatcommands.ChatCommandManager;
import neverbuy.proxy.Connection;
import neverbuy.telegram.TelegramApi;
import neverbuy.telegram.command.CommandManager;
import neverbuy.util.misc.EventHandler;

public class Client {
   private static Client Instance;

   public void initialize() {
      Instance = this;
      String name = "Cracked By Fruzz & rlyPasterZ, Пончик еблан";
      int uid = 1;
      String date = "17.02.2025";
      String role = "https://dsg.gg/extz";
      Constants.ALPHA = true;
      Constants.USER = new BetaUser(name, uid);
      Thread shutDownThread = new Thread(() -> {
         ConfigManager.save("autobuy");
         ConfigManager.saveTelegram();
      });
      Runtime.getRuntime().addShutdownHook(shutDownThread);
      this.initConstants();
      Constants.EVENT_BUS.register(Constants.AUTOBUY);
      Constants.EVENT_BUS.register(Constants.EVENT_HANDLER);
      Constants.EVENT_BUS.register(Constants.CHAT_COMMAND_MANAGER);
     // Discord discord = new Discord();
      //discord.rpc();
      ConfigManager.read("autobuy");
      ConfigManager.readTelegram();
      Util.getOSType().openURI("https://dsc.gg/extz");
      Util.getOSType().openURI("https://t.me/Enigma_MC");
      Util.getOSType().openURI("https://t.me/getfruzz");
   }

   private void initConstants() {
      Constants.EVENT_BUS = new EventBus();
      Constants.TELEGRAM = new TelegramApi();
      Constants.COMMAND_MANAGER = new CommandManager();
      Constants.CHAT_COMMAND_MANAGER = new ChatCommandManager();
      Constants.LIST = new AutoBuyList();
      Constants.AUTOBUY = new AutoBuy();
      Constants.EVENT_HANDLER = new EventHandler();
      Constants.PROXY = new Connection();
   }

   public static Client getInstance() {
      return Instance;
   }
}
