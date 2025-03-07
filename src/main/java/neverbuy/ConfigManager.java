package neverbuy;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import neverbuy.autobuy.api.CheckBox;
import neverbuy.autobuy.api.Delay;
import neverbuy.autobuy.api.Setting;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.autobuy.list.item.CustomItem;

public final class ConfigManager {
   public static final String PATH = "C:\\NeverBuy\\config\\";

   public static void save(String path) {
      try {
         String fullPath = "C:\\NeverBuy\\config\\" + path + ".json";
         FileWriter writer = new FileWriter(fullPath);
         JsonObject mainObject = new JsonObject();
         JsonObject autoBuyObject = new JsonObject();
         JsonObject itemsObject = new JsonObject();
         JsonObject settings = new JsonObject();
         Iterator var7 = Constants.LIST.items.iterator();

         while(var7.hasNext()) {
            AutoBuyItem<?> item = (AutoBuyItem)var7.next();
            item.configure(itemsObject);
         }

         var7 = Constants.AUTOBUY.all().iterator();

         while(var7.hasNext()) {
            Setting<?, ?> setting = (Setting)var7.next();
            setting.configure(settings);
         }

         autoBuyObject.add("items", itemsObject);
         autoBuyObject.add("settings", settings);
         mainObject.add("autobuy", autoBuyObject);
         writer.write(mainObject.toString());
         writer.close();
      } catch (Exception var9) {
         var9.printStackTrace(System.err);
      }

   }

   public static void saveTelegram() {
      try {
         String fullPath = "C:\\NeverBuy\\config\\telegram.json";
         FileWriter writer = new FileWriter(fullPath);
         JsonObject telegramObject = new JsonObject();
         Constants.TELEGRAM.configure(telegramObject);
         writer.write(telegramObject.toString());
         writer.close();
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
      }

   }

   public static void readTelegram() {
      try {
         File file = new File("C:\\NeverBuy\\config\\telegram.json");
         if (!file.exists()) {
            return;
         }

         FileReader reader = new FileReader(file);
         JsonObject object = (new JsonParser()).parse(reader).getAsJsonObject();
         Constants.TELEGRAM.read(object);
         reader.close();
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
      }

   }

   public static void read(String path) {
      try {
         File file = new File("C:\\NeverBuy\\config\\" + path + ".json");
         if (!file.exists()) {
            return;
         }

         FileReader reader = new FileReader(file);
         JsonObject mainObject = (new JsonParser()).parse(reader).getAsJsonObject();
         JsonObject autoBuyObject = mainObject.get("autobuy").getAsJsonObject();
         JsonObject itemsObject = autoBuyObject.get("items").getAsJsonObject();
         JsonObject settingsObject = autoBuyObject.get("settings").getAsJsonObject();
         Constants.LIST.reload();
         Iterator var7 = itemsObject.entrySet().iterator();

         while(var7.hasNext()) {
            Entry<String, JsonElement> entry = (Entry)var7.next();
            String key = (String)entry.getKey();
            JsonObject o = ((JsonElement)entry.getValue()).getAsJsonObject();
            if (o.get("client").getAsBoolean()) {
               AutoBuyItem<?> item = Constants.LIST.getAsDisplay(key);
               item.read(o);
            } else {
               CustomItem customItem = new CustomItem();
               customItem.read(o);
               Constants.LIST.items.add(customItem);
            }
         }

         var7 = Constants.AUTOBUY.all().iterator();

         while(var7.hasNext()) {
            Setting<?, ?> setting = (Setting)var7.next();
            setting.read(settingsObject);
         }

         reader.close();
      } catch (Exception var12) {
         var12.printStackTrace(System.err);
      }

   }

   public static String get() {
      try {
         JsonObject mainObject = new JsonObject();
         JsonObject autoBuyObject = new JsonObject();
         JsonObject itemsObject = new JsonObject();
         JsonObject delaysObject = new JsonObject();
         JsonObject settings = new JsonObject();
         Iterator var5 = Constants.LIST.items.iterator();

         while(var5.hasNext()) {
            AutoBuyItem<?> item = (AutoBuyItem)var5.next();
            item.configure(itemsObject);
         }

         var5 = Constants.AUTOBUY.getAllDelays().iterator();

         while(var5.hasNext()) {
            Delay delay = (Delay)var5.next();
            delay.configure(delaysObject);
         }

         var5 = Constants.AUTOBUY.getAllCheckBoxes().iterator();

         while(var5.hasNext()) {
            CheckBox checkBox = (CheckBox)var5.next();
            checkBox.configure(settings);
         }

         autoBuyObject.add("items", itemsObject);
         autoBuyObject.add("delays", delaysObject);
         autoBuyObject.add("settings", settings);
         mainObject.add("autobuy", autoBuyObject);
         return mainObject.toString();
      } catch (Exception var7) {
         var7.printStackTrace(System.err);
         return "";
      }
   }

   public static void readFromString(String config) {
      JsonObject mainObject = (new JsonParser()).parse(config).getAsJsonObject();
      JsonObject autoBuyObject = mainObject.get("autobuy").getAsJsonObject();
      JsonObject itemsObject = autoBuyObject.get("items").getAsJsonObject();
      JsonObject settingsObject = autoBuyObject.get("settings").getAsJsonObject();
      Constants.LIST.reload();
      Iterator var5 = itemsObject.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<String, JsonElement> entry = (Entry)var5.next();
         String key = (String)entry.getKey();
         JsonObject o = ((JsonElement)entry.getValue()).getAsJsonObject();
         if (o.get("client").getAsBoolean()) {
            AutoBuyItem<?> item = Constants.LIST.getAsDisplay(key);
            item.read(o);
         } else {
            CustomItem customItem = new CustomItem();
            customItem.read(o);
            Constants.LIST.items.add(customItem);
         }
      }

      var5 = Constants.AUTOBUY.all().iterator();

      while(var5.hasNext()) {
         Setting<?, ?> setting = (Setting)var5.next();
         setting.read(settingsObject);
      }

   }

   public static List<File> getAllConfigs() {
      File dir = new File("C:\\NeverBuy\\config\\");
      List<File> files = new ArrayList();
      if (dir.exists() && dir.isDirectory()) {
         File[] filesArray = dir.listFiles();
         if (filesArray != null) {
            files = new ArrayList(Arrays.asList(filesArray));
         }
      }

      if (files.isEmpty()) {
         return Lists.newArrayList();
      } else {
         files.removeIf((file) -> {
            try {
               if (file.getName().endsWith(".json")) {
                  FileReader reader = new FileReader(file);
                  JsonObject parser = (new JsonParser()).parse(reader).getAsJsonObject();
                  reader.close();
                  return !parser.has("autobuy") && !parser.has("telegram");
               }
            } catch (Exception var3) {
               var3.printStackTrace(System.err);
            }

            return false;
         });
         return files;
      }
   }

   private ConfigManager() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
