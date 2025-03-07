package neverbuy.telegram;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import neverbuy.ConfigManager;
import neverbuy.Constants;
import neverbuy.IConfigurable;
import neverbuy.autobuy.api.FieldSetting;
import neverbuy.telegram.connection.Packet;
import neverbuy.telegram.connection.PacketThread;
import neverbuy.telegram.connection.Returnable;
import neverbuy.util.misc.ChatUtil;

public class TelegramApi implements IConfigurable {
   final FieldSetting token = FieldSetting.newInstance("Токен", "token", "");
   final FieldSetting chatId = FieldSetting.newInstance("Чат-айди", "chatId", "");
   private int lastMessageId;
   private String lastFileId;

   public TelegramApi() {
      ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
      schedule.scheduleAtFixedRate(() -> {
         try {
            if (!this.isDefined()) {
               return;
            }

            JsonArray array = this.getUpdates();
            Iterator var2 = array.iterator();

            while(var2.hasNext()) {
               JsonElement element = (JsonElement)var2.next();
               JsonObject object1 = element.getAsJsonObject().get("message").getAsJsonObject();
               this.lastMessageId = element.getAsJsonObject().get("update_id").getAsInt();
               if (!object1.has("document")) {
                  String id = object1.get("from").getAsJsonObject().get("id").getAsString();
                  if (!id.equals(this.getChatId())) {
                     this.sendMessage("Ах ты мамкин хацкер", id);
                  } else {
                     String m = this.convert(element);
                     if (m.equalsIgnoreCase("/start")) {
                        this.sendMessage("Привет! Ты скачал NeverBuy и хочешь настроить телеграм. Чтобы посмотреть все команды напиши - Помощь");
                     }

                     Constants.COMMAND_MANAGER.reply(m);
                  }
               } else {
                  JsonObject document;
                  if (object1.has("caption") && object1.get("caption").getAsString().equalsIgnoreCase("конфиг загрузить") && (document = object1.getAsJsonObject("document")).get("file_name").getAsString().endsWith(".json")) {
                     this.lastFileId = document.get("file_id").getAsString();
                     this.onFile();
                  }
               }
            }
         } catch (Exception var8) {
         }

      }, 0L, 5L, TimeUnit.SECONDS);
   }

   public boolean isDefined() {
      return this.getToken().length() == 46 && this.getChatId().length() == 10;
   }

   public String getToken() {
      return (String)this.token.getValue();
   }

   public String getChatId() {
      return (String)this.chatId.getValue();
   }

   private JsonArray getUpdates() {
      AtomicReference<JsonArray> jsonArray = new AtomicReference(new JsonArray());
      Packet packet = Packet.newInstance("api.telegram.org", (connection) -> {
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         StringBuilder response = new StringBuilder();

         String inputLine;
         while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
         }

         in.close();
         JsonObject object = (new JsonParser()).parse(response.toString()).getAsJsonObject();
         jsonArray.set(object.getAsJsonArray("result"));
      }, String.format("bot%s", this.getToken()), "getUpdates?offset=" + (this.lastMessageId + 1));
      packet.send();
      return (JsonArray)jsonArray.get();
   }

   private String convert(JsonElement element) {
      return element.getAsJsonObject().get("message").getAsJsonObject().get("text").getAsString();
   }

   public void sendMessage(String msg, String chatId) {
      try {
         String encodedMessage = URLEncoder.encode(msg, StandardCharsets.UTF_8);
         new PacketThread(Packet.newInstance("api.telegram.org", (connection) -> {
            System.out.println("Message successfully send!");
         }, String.format("bot%s", this.getToken()), "sendMessage?chat_id=" + chatId + "&text=" + encodedMessage));
      } catch (Exception var4) {
         var4.printStackTrace(System.err);
      }

   }

   public void sendMessage(String msg) {
      this.sendMessage(msg, this.getChatId());
   }

   public void sendPhoto(String imgPath, String caption) {
      try {
         String encodedCaption = URLEncoder.encode(caption, StandardCharsets.UTF_8);
         Returnable var10001 = (connection) -> {
            File imgFile = new File(imgPath);
            String boundary = Long.toHexString(System.currentTimeMillis());
            String CRLF = "\r\n";
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream output = connection.getOutputStream();

            try {
               PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);

               try {
                  FileInputStream inputStream = new FileInputStream(imgFile);

                  try {
                     writer.append("--").append(boundary).append(CRLF);
                     writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"").append(imgFile.getName()).append("\"").append(CRLF);
                     writer.append("Content-Type: image/jpeg").append(CRLF);
                     writer.append(CRLF).flush();
                     byte[] buffer = new byte[4096];

                     while(true) {
                        int bytesRead;
                        if ((bytesRead = inputStream.read(buffer)) == -1) {
                           writer.append(CRLF).flush();
                           writer.append("--").append(boundary).append("--").append(CRLF);
                           break;
                        }

                        output.write(buffer, 0, bytesRead);
                     }
                  } catch (Throwable var13) {
                     try {
                        inputStream.close();
                     } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                     }

                     throw var13;
                  }

                  inputStream.close();
               } catch (Throwable var14) {
                  try {
                     writer.close();
                  } catch (Throwable var11) {
                     var14.addSuppressed(var11);
                  }

                  throw var14;
               }

               writer.close();
            } catch (Throwable var15) {
               if (output != null) {
                  try {
                     output.close();
                  } catch (Throwable var10) {
                     var15.addSuppressed(var10);
                  }
               }

               throw var15;
            }

            if (output != null) {
               output.close();
            }

         };
         String[] var10002 = new String[]{String.format("bot%s", this.getToken()), null};
         String var10005 = this.getChatId();
         var10002[1] = "sendPhoto?chat_id=" + var10005 + "&caption=" + encodedCaption;
         Packet packet = Packet.newInstance("api.telegram.org", var10001, var10002).setPost(true);
         packet.send();
      } catch (Exception var5) {
         var5.printStackTrace(System.err);
      }

   }

   public void sendDocument() {
      try {
         String caption = "Текущий конфиг:";
         String encodedCaption = URLEncoder.encode(caption, StandardCharsets.UTF_8);
         Returnable var10001 = (connection) -> {
            ConfigManager.save("telegramSend");
            Thread.sleep(300L);
            File docFile = new File("./config/telegramSend.json");
            String boundary = Long.toHexString(System.currentTimeMillis());
            String CRLF = "\r\n";
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream output = connection.getOutputStream();

            try {
               PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8), true);

               try {
                  FileInputStream inputStream = new FileInputStream(docFile);

                  try {
                     writer.append("--").append(boundary).append(CRLF);
                     writer.append("Content-Disposition: form-data; name=\"document\"; filename=\"").append(docFile.getName()).append("\"").append(CRLF);
                     writer.append("Content-Type: application/pdf").append(CRLF);
                     writer.append(CRLF).flush();
                     byte[] buffer = new byte[4096];

                     while(true) {
                        int bytesRead;
                        if ((bytesRead = inputStream.read(buffer)) == -1) {
                           output.flush();
                           writer.append(CRLF).flush();
                           writer.append("--").append(boundary).append("--").append(CRLF);
                           break;
                        }

                        output.write(buffer, 0, bytesRead);
                     }
                  } catch (Throwable var12) {
                     try {
                        inputStream.close();
                     } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                     }

                     throw var12;
                  }

                  inputStream.close();
               } catch (Throwable var13) {
                  try {
                     writer.close();
                  } catch (Throwable var10) {
                     var13.addSuppressed(var10);
                  }

                  throw var13;
               }

               writer.close();
            } catch (Throwable var14) {
               if (output != null) {
                  try {
                     output.close();
                  } catch (Throwable var9) {
                     var14.addSuppressed(var9);
                  }
               }

               throw var14;
            }

            if (output != null) {
               output.close();
            }

            boolean ignored = docFile.delete();
         };
         String[] var10002 = new String[]{String.format("bot%s", this.getToken()), null};
         String var10005 = this.getChatId();
         var10002[1] = "sendDocument?chat_id=" + var10005 + "&caption=" + encodedCaption;
         Packet packet = Packet.newInstance("api.telegram.org", var10001, var10002).setPost(true);
         packet.send();
      } catch (Exception var4) {
         var4.printStackTrace(System.err);
      }

   }

   public void onFile() {
      try {
         Packet packet = Packet.newInstance("api.telegram.org", (connection) -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            try {
               StringBuilder responseBuilder = new StringBuilder();

               while(true) {
                  String line;
                  if ((line = reader.readLine()) == null) {
                     JsonObject fileObject = (new JsonParser()).parse(responseBuilder.toString()).getAsJsonObject().getAsJsonObject("result");
                     String filePath = fileObject.get("file_path").getAsString();
                     String fileUrl = "https://api.telegram.org/file/bot" + this.token + "/" + filePath;
                     URL fileURL = new URL(fileUrl);
                     BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileURL.openStream()));

                     try {
                        StringBuilder fileContentBuilder = new StringBuilder();

                        while(true) {
                           String fileLine;
                           if ((fileLine = fileReader.readLine()) == null) {
                              String fileContent = fileContentBuilder.toString();
                              this.loadConfig(fileContent);
                              break;
                           }

                           fileContentBuilder.append(fileLine).append("\n");
                        }
                     } catch (Throwable var15) {
                        try {
                           fileReader.close();
                        } catch (Throwable var14) {
                           var15.addSuppressed(var14);
                        }

                        throw var15;
                     }

                     fileReader.close();
                     break;
                  }

                  responseBuilder.append(line);
               }
            } catch (Throwable var16) {
               try {
                  reader.close();
               } catch (Throwable var13) {
                  var16.addSuppressed(var13);
               }

               throw var16;
            }

            reader.close();
         }, String.format("bot%s", this.getToken()), "getFile?file_id=" + this.lastFileId);
         packet.send();
      } catch (Exception var2) {
         var2.printStackTrace(System.err);
      }

   }

   private void loadConfig(String config) {
      ConfigManager.readFromString(config);
      ChatUtil.addMessage("Конфиг успешно загружен!");
      this.sendMessage("Конфиг успешно загружен!");
   }

   public List<FieldSetting> settings() {
      return Arrays.asList(this.token, this.chatId);
   }

   public void configure(JsonObject jsonObject) {
      JsonObject telegramObject = new JsonObject();
      telegramObject.addProperty("token", this.getToken());
      telegramObject.addProperty("chatId", this.getChatId());
      jsonObject.add("telegram", telegramObject);
   }

   public void read(JsonObject jsonObject) {
      JsonObject telegramObject = jsonObject.getAsJsonObject("telegram");
      String token = telegramObject.get("token").getAsString();
      String chatId = telegramObject.get("chatId").getAsString();
      this.token.setValue(token);
      this.chatId.setValue(chatId);
   }
}
