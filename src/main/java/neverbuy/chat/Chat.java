package neverbuy.chat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import neverbuy.Constants;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.telegram.connection.Packet;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;
import neverbuy.util.render.Scissor;
import neverbuy.util.textfield.FieldFunction;
import neverbuy.util.textfield.TextField;

public class Chat {
   private final List<Message> messages = new ArrayList();
   public float scroll;
   public float scrollAnimation;
   public TextField textField;
   private boolean resize;

   public Chat() {
      ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
   }

   public void onResize() {
      this.resize = true;
   }

   public void sendMessage(String msg) {
      Packet packet = Packet.newInstance("http://fi1.bot-hosting.net:6568", (connection) -> {
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);
         connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
         JsonObject jsonObject = new JsonObject();
         jsonObject.addProperty("owner", Constants.USER.getName());
         jsonObject.addProperty("message", msg);
         LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
         String formattedDateTime = now.format(formatter);
         jsonObject.addProperty("date", formattedDateTime);
         String dataToSend = jsonObject.toString();
         OutputStream os = connection.getOutputStream();

         try {
            byte[] input = dataToSend.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
         } catch (Throwable var11) {
            if (os != null) {
               try {
                  os.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }
            }

            throw var11;
         }

         if (os != null) {
            os.close();
         }

         connection.disconnect();
      }, "addMessage").setPost(true);
      packet.send();
   }

   public void getMessages() {
      Packet packet = Packet.newInstance("http://fi1.bot-hosting.net:6568", (connection) -> {
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);
         connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
         BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

         try {
            String response = (String)br.lines().collect(Collectors.joining(System.lineSeparator()));
            connection.disconnect();
            JsonObject jsonObject = (new JsonParser()).parse(response).getAsJsonObject();
            if (jsonObject.has("messages")) {
               JsonArray elements = jsonObject.get("messages").getAsJsonArray();
               Iterator var6 = elements.iterator();

               while(var6.hasNext()) {
                  JsonElement jsonElement = (JsonElement)var6.next();
                  JsonObject object = jsonElement.getAsJsonObject();
                  String owner = object.get("owner").getAsString();
                  String msg = object.get("message").getAsString();
                  String dateString = object.get("date").getAsString();
                  Message message = new Message(owner, msg, dateString);
                  if (!this.messages.contains(message)) {
                     this.messages.add(message);
                  }
               }
            }
         } catch (Throwable var14) {
            try {
               br.close();
            } catch (Throwable var13) {
               var14.addSuppressed(var13);
            }

            throw var14;
         }

         br.close();
      }, "getMessages").setPost(true);
      packet.send();
   }

   public void render(MatrixStack matrixStack) {
      try {
         this.scrollAnimation = RenderUtils.lerp(this.scrollAnimation, this.scroll, 8.0F);
         Minecraft mc = Minecraft.getInstance();
         MainWindow mainWindow = mc.getMainWindow();
         float width = 130.0F;
         float height = 200.0F;
         float posX = (float)(mainWindow.getScaledWidth() - 150);
         float posY = ((float)mainWindow.getScaledHeight() - height) / 2.0F - 20.0F;
         if (this.textField == null || this.resize) {
            this.textField = new TextField("Напишите сообщение...", Fonts.Inter.get(14.0F), posX + 6.0F, posY + height - 25.0F, width - 12.0F, 17.0F, new FieldFunction() {
               public void onChar(String full) {
               }

               public void onEnter(String full) {
                  Chat.this.sendMessage(full);
                  LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
                  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss");
                  String formattedDateTime = now.format(formatter);
                  Chat.this.messages.add(new Message(Constants.USER.getName(), full, formattedDateTime));
               }
            });
            this.resize = false;
         }

         Color colorOne = new Color(2434341);
         Color colorTwo = new Color(1381653);
         RoundedUtils.drawRoundGradient((double)posX, (double)posY, (double)width, (double)height, 8.0D, colorOne, colorTwo, colorTwo, colorOne);
         Color inColorOne = new Color(-2144457170, true);
         Color inColorTwo = new Color(-2145509858, true);
         RoundedUtils.drawRoundGradient((double)(posX + 5.0F), (double)(posY + 20.0F), (double)(width - 10.0F), (double)(height - 50.0F), 4.0D, inColorOne, inColorTwo, inColorTwo, inColorOne);
         Font f = Fonts.mntsb.get(17.0F);
         Font f1 = Fonts.Inter.get(12.0F);
         f.draw("Онлайн чат", posX + (width - (float)f.getWidth("Онлайн чат")) / 2.0F, posY + 4.0F, -1);
         float mesY = posY + height - 60.0F;
         this.textField.render(matrixStack);
         Scissor.push();
         Scissor.setFromComponentCoordinates((double)posX, (double)(posY + 20.0F), (double)width, (double)(height - 50.0F));

         float tHeight;
         float nextTHeight;
         int scale;
         for(Iterator var15 = this.messages.iterator(); var15.hasNext(); mesY -= tHeight + 5.0F - (nextTHeight - (float)(15 * scale))) {
            Message message = (Message)var15.next();
            List<String> msg = this.wrapText(message.getMessage(), f1, width - 35.0F);
            List<String> strings = new ArrayList();
            tHeight = 15.0F;
            nextTHeight = 0.0F;
            scale = 0;
            Iterator var22 = msg.iterator();

            String formattedDate;
            while(var22.hasNext()) {
               formattedDate = (String)var22.next();
               tHeight += (float)(f1.getHeight() + 5);
               strings.add(formattedDate);
            }

            int messageIndex = this.messages.indexOf(message);
            if (messageIndex < this.messages.size() - 1) {
               nextTHeight = 5.0F;

               for(Iterator var29 = this.wrapText(((Message)this.messages.get(messageIndex + 1)).getMessage(), f1, width - 35.0F).iterator(); var29.hasNext(); ++scale) {
                  String s = (String)var29.next();
                  nextTHeight += (float)f.getHeight();
               }
            }

            RoundedUtils.drawRoundGradient((double)(posX + 7.0F), (double)(mesY + this.scrollAnimation), (double)(width - 14.0F), (double)tHeight, 4.0D, colorTwo, colorOne, colorOne, colorTwo);
            f1.draw(message.getOwner(), posX + 9.0F, mesY + 3.0F + this.scrollAnimation, -1);
            formattedDate = String.format("%02d.%02d.%s-%02d:%02d", message.getDateTime().getDayOfMonth(), message.getDateTime().getMonthValue(), String.valueOf(message.getDateTime().getYear()).substring(2), message.getDateTime().getHour(), message.getDateTime().getMinute());
            f1.draw(formattedDate, posX + width - 5.0F - (float)(f1.getWidth(formattedDate) + 8), mesY + 3.0F + this.scrollAnimation, -1);
            float textOffset = 15.0F;

            for(Iterator var25 = strings.iterator(); var25.hasNext(); textOffset += (float)f.getHeight()) {
               String s = (String)var25.next();
               f1.draw(s, posX + 9.0F, mesY + textOffset + this.scrollAnimation, -1);
            }
         }

         Scissor.pop();
      } catch (Exception var27) {
         var27.printStackTrace(System.out);
      }

   }

   private List<String> wrapText(String text, Font metrics, float maxWidth) {
      List<String> lines = new ArrayList();
      StringBuilder currentLine = new StringBuilder();
      float width = 0.0F;

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         float charWidth = (float)metrics.getWidth(String.valueOf(c));
         if (width + charWidth > maxWidth) {
            lines.add(currentLine.toString());
            currentLine = new StringBuilder();
            width = 0.0F;
         }

         currentLine.append(c);
         width += charWidth;
      }

      if (!currentLine.isEmpty()) {
         lines.add(currentLine.toString());
      }

      return lines;
   }
}
