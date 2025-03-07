package neverbuy.chatcommands.impl;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Collectors;
import neverbuy.ConfigManager;
import neverbuy.Constants;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.telegram.command.api.CommandInfo;
import neverbuy.telegram.connection.Packet;
import neverbuy.util.misc.ChatUtil;
import neverbuy.util.misc.TimerUtil;

@CommandInfo(
   name = "cloud",
   desc = "Скачивает конфиги с облака и загружает",
   usage = ".cloud [save/load] key"
)
public class CloudCommand extends ChatCommand {
   private final Random RANDOM = new Random();
   private final TimerUtil saveTimer = new TimerUtil();

   public void execute(String[] args) {
      Packet packet;
      if (args[1].equalsIgnoreCase("save")) {
         if (this.saveTimer.hasPassed(5000.0F, true)) {
            packet = Packet.newInstance("http://fi1.bot-hosting.net:6568", (connection) -> {
               String key = this.getRandomString();
               JsonObject json = new JsonObject();
               connection.setRequestMethod("POST");
               connection.setDoOutput(true);
               connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               json.addProperty("key", key);
               json.addProperty("config", ConfigManager.get());
               String dataToSend = json.toString();
               OutputStream os = connection.getOutputStream();

               try {
                  byte[] input = dataToSend.getBytes(StandardCharsets.UTF_8);
                  os.write(input, 0, input.length);
               } catch (Throwable var9) {
                  if (os != null) {
                     try {
                        os.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (os != null) {
                  os.close();
               }

               connection.disconnect();
               Constants.copyToClipboard(key);
               ChatUtil.addMessage("Конфиг сохранён на ключ: " + key + " (Скопирован)");
            }, "sendConfig").setPost(true);
            packet.send();
         } else {
            ChatUtil.addMessage("Нельзя так быстро сохранять конфиги!");
         }
      } else if (args[1].equalsIgnoreCase("load")) {
         packet = Packet.newInstance("http://fi1.bot-hosting.net:6568", (connection) -> {
            JsonObject json = new JsonObject();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            json.addProperty("key", args[2]);
            String dataToSend = json.toString();
            OutputStream os = connection.getOutputStream();

            try {
               byte[] input = dataToSend.getBytes(StandardCharsets.UTF_8);
               os.write(input, 0, input.length);
            } catch (Throwable var10) {
               if (os != null) {
                  try {
                     os.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (os != null) {
               os.close();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            try {
               String response = (String)br.lines().collect(Collectors.joining(System.lineSeparator()));
               connection.disconnect();
               ChatUtil.addMessage("Конфиг успешно загружен!");
               ConfigManager.readFromString(response);
            } catch (Throwable var9) {
               try {
                  br.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            br.close();
         }, "getConfig").setPost(true);
         packet.send();
      }

   }

   private String getRandomString() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i <= 15; ++i) {
         String ALL_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
         int randomIndex = this.RANDOM.nextInt(ALL_CHARS.length());
         char randomChar = ALL_CHARS.charAt(randomIndex);
         sb.append(randomChar);
      }

      return sb.toString();
   }
}
