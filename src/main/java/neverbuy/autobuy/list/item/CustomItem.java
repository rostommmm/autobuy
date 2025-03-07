package neverbuy.autobuy.list.item;

import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import neverbuy.autobuy.list.item.setting.ItemSetting;

public class CustomItem extends AutoBuyItem<CustomItem> {
   public CustomItem name(String name) {
      this.name = name;
      return this;
   }

   public CustomItem item(Item item) {
      this.item = item;
      return this;
   }

   public CustomItem price(BigDecimal price) {
      this.getSettingByName("Цена покупки").getAsBigDecimal().setVal(price);
      return this;
   }

   public CustomItem stack(ItemStack stack) {
      this.stack = stack;
      this.containsStack = true;
      return null;
   }

   public CustomItem sell(BigDecimal price) {
      this.getSettingByName("Цена продажи").getAsBigDecimal().setVal(price);
      return this;
   }

   public void buying(boolean buy) {
      this.getSettingByName("Покупать").getAsBool().setVal(buy);
   }

   public void selling(boolean selling) {
      this.getSettingByName("Продавать").getAsBool().setVal(selling);
   }

   public void configure(JsonObject jsonObject) {
      JsonObject object = new JsonObject();
      Optional<RegistryKey<Item>> registryKey = Registry.ITEM.getOptionalKey(this.getItem());
      if (!registryKey.isEmpty()) {
         Iterator var4 = this.getSettings().iterator();

         while(var4.hasNext()) {
            ItemSetting<?> setting = (ItemSetting)var4.next();
            setting.configure(object);
         }

         object.addProperty("item", ((RegistryKey)registryKey.get()).getLocation().toString());
         object.addProperty("client", false);
         jsonObject.add(this.name, object);
      }
   }

   public void read(JsonObject jsonObject) {
      Iterator var2 = this.getSettings().iterator();

      while(var2.hasNext()) {
         ItemSetting<?> setting = (ItemSetting)var2.next();
         setting.read(jsonObject);
      }

      if (jsonObject.has("item")) {
         String itemStr = jsonObject.get("item").getAsString();
         Item item = (Item)Registry.ITEM.getOrDefault(new ResourceLocation(itemStr.toLowerCase()));
         this.item(item);
      }

   }
}
