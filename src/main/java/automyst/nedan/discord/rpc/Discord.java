package automyst.nedan.discord.rpc;

import automyst.nedan.discord.DiscordEventHandlers;
import automyst.nedan.discord.DiscordRPC;
import automyst.nedan.discord.DiscordRichPresence;
import neverbuy.Constants;
import neverbuy.util.IUtil;

public class Discord implements IUtil {
   private final DiscordRPC ds;
   private final DiscordRichPresence rich;

   public Discord() {
      this.ds = DiscordRPC.INSTANCE;
      this.rich = new DiscordRichPresence();
   }

   public void rpc() {
      DiscordEventHandlers handlers = new DiscordEventHandlers();
      this.ds.Discord_Initialize(Constants.ALPHA ? "1259828051789873192" : "1259828203338465291", handlers, true, "123123");
      this.rich.startTimestamp = System.currentTimeMillis() / 1000L;
      this.rich.largeImageText = "Version: " + Constants.VERSION;
      this.ds.Discord_UpdatePresence(this.rich);
      Thread thread = new Thread(() -> {
         while(!Thread.currentThread().isInterrupted()) {
            this.ds.Discord_RunCallbacks();
            DiscordRichPresence var10000 = this.rich;
            String var10001 = Constants.USER.getName();
            var10000.details = "User: " + var10001;
            var10000 = this.rich;
            int var4 = Constants.USER.getUid();
            var10000.state = "UID: " + var4;
            this.rich.button_label_1 = "Получить кряк!";
            this.rich.button_url_1 = "https://discord.gg/G4w4awfJ";
            String string = this.rich.largeImageKey = Constants.ALPHA ? "https://i.imgur.com/0AsU0HP.gif" : "https://i.imgur.com/LSBj3NU.gif";
            if (Constants.isOnFunTime()) {
               this.rich.smallImageKey = "https://i.imgur.com/tH2fucq.png";
               this.rich.smallImageText = "сосет фрузу";
            } else {
               this.rich.smallImageText = "";
               this.rich.smallImageKey = "";
            }

            this.ds.Discord_UpdatePresence(this.rich);

            try {
               Thread.sleep(2500L);
            } catch (InterruptedException var3) {
               var3.printStackTrace(System.err);
            }
         }

      }, "discord-rpc");
      thread.start();
   }
}
