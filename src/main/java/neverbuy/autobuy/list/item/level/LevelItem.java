package neverbuy.autobuy.list.item.level;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import neverbuy.autobuy.list.item.ClientItem;
import neverbuy.autobuy.list.item.setting.BDSetting;
import neverbuy.autobuy.list.item.setting.BoolSetting;
import neverbuy.autobuy.list.item.setting.ItemSetting;

public class LevelItem extends ClientItem {
   private LevelCheck levelCheck;
   private int level;
   private List<BoolSetting> buy = new ArrayList();
   private List<BDSetting> buyPrices = new ArrayList();
   private List<BoolSetting> sell = new ArrayList();
   private List<BDSetting> sellPrices = new ArrayList();

   public LevelItem() {
      Iterator var1 = this.getSettings().iterator();

      while(var1.hasNext()) {
         ItemSetting<?> setting = (ItemSetting)var1.next();
         if (setting instanceof BoolSetting) {
            setting.getAsBool().setVal(false);
            setting.getAsBool().setVisible(() -> {
               return false;
            });
         } else if (setting instanceof BDSetting) {
            setting.getAsBigDecimal().setVal(BigDecimal.ZERO);
            setting.getAsBigDecimal().setVisible(() -> {
               return false;
            });
         }
      }

      for(int i = 0; i < 3; ++i) {
         this.buy.add(new BoolSetting("Покупать", true));
         int finalI = i;
         this.buyPrices.add((new BDSetting("Цена покупки", new BigDecimal(100))).setVisible(() -> {
            return (Boolean)((BoolSetting)this.buy.get(finalI)).getVal();
         }));
         this.sell.add(new BoolSetting("Продавать", true));
         int finalI1 = i;
         this.sellPrices.add((new BDSetting("Цена продажи", new BigDecimal(100))).setVisible(() -> {
            return (Boolean)((BoolSetting)this.sell.get(finalI1)).getVal();
         }));
      }

   }

   public String getName() {
      String var10000 = super.getName();
      return var10000 + " " + this.level + " ур.";
   }

   public String getName(int lvl) {
      String var10000 = super.getName();
      return var10000 + " " + lvl + " ур.";
   }

   public LevelItem lcheck(LevelCheck check) {
      this.levelCheck = check;
      return this;
   }

   public LevelItem name(String name) {
      super.name(name);
      return this;
   }

   public LevelItem displayName(String name) {
      this.configName = name;
      return this;
   }

   public LevelItem withLevel(int level) {
      this.level = level;
      return this;
   }

   public LevelItem stack(ItemStack stack) {
      super.stack(stack);
      return this;
   }

   public LevelItem item(Item item) {
      super.item(item);
      return this;
   }

   public void configure(JsonObject jsonObject) {
      try {
         JsonObject object = new JsonObject();
         JsonObject prices = new JsonObject();

         for(int i = 1; i <= 3; ++i) {
            JsonObject level = new JsonObject();
            level.addProperty("buy", this.getPriceFromLevel(i));
            level.addProperty("sell", this.getSellPriceFromLevel(i));
            prices.add("level-" + i, level);
         }

         object.add("prices", prices);
         object.addProperty("buy", this.isBuy());
         object.addProperty("sell", this.isSell());
         object.addProperty("client", true);
         jsonObject.add(this.configName, object);
      } catch (Exception var6) {
         var6.printStackTrace(System.err);
      }

   }

   public void read(JsonObject jsonObject) {
      try {
         if (jsonObject.has("item")) {
            return;
         }

         JsonObject prices = jsonObject.get("prices").getAsJsonObject();
         Iterator var3 = prices.entrySet().iterator();

         while(var3.hasNext()) {
            Entry<String, JsonElement> elementEntry = (Entry)var3.next();
            String key = (String)elementEntry.getKey();
            JsonElement value = (JsonElement)elementEntry.getValue();
            BigDecimal buyPrice = value.getAsJsonObject().get("buy").getAsBigDecimal();
            BigDecimal sellPrice = value.getAsJsonObject().get("sell").getAsBigDecimal();
            int level = Integer.parseInt(key.replaceAll("level-", ""));
            this.setPrice(level, buyPrice);
            this.setSellPrice(level, sellPrice);
         }

         boolean buy = jsonObject.get("buy").getAsBoolean();
         boolean sell = jsonObject.get("sell").getAsBoolean();
         this.buying(buy);
         this.selling(sell);
      } catch (Exception var10) {
         var10.printStackTrace(System.err);
      }

   }

   private boolean containsAttribute(Multimap<Attribute, AttributeModifier> attributes, Attribute attribute, double value, Operation operation) {
      List<AttributeModifier> matchingModifiers = new ArrayList();
      Iterator var7 = attributes.keySet().iterator();

      while(true) {
         Attribute attribute1;
         do {
            if (!var7.hasNext()) {
               return !matchingModifiers.isEmpty();
            }

            attribute1 = (Attribute)var7.next();
         } while(attribute1 != attribute);

         Iterator var9 = attributes.get(attribute).iterator();

         while(var9.hasNext()) {
            AttributeModifier modifier = (AttributeModifier)var9.next();
            if (modifier.getOperation() == operation && modifier.getAmount() == value) {
               matchingModifiers.add(modifier);
            }
         }
      }
   }

   public boolean containsAllAttributes(List<Attribute> list, List<AttributeModifier> modifiers, ItemStack stack, boolean withSize) {
      Multimap<Attribute, AttributeModifier> attributes = stack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
      Multimap<Attribute, AttributeModifier> att = this.attributesFromLists(list, modifiers);
      if (att.isEmpty()) {
         return false;
      } else if (withSize && att.size() != attributes.size()) {
         return false;
      } else {
         Iterator var7 = att.entries().iterator();

         Attribute key;
         AttributeModifier value;
         do {
            if (!var7.hasNext()) {
               return true;
            }

            Entry<Attribute, AttributeModifier> entry = (Entry)var7.next();
            key = (Attribute)entry.getKey();
            value = (AttributeModifier)entry.getValue();
         } while(this.containsAttribute(attributes, key, value.getAmount(), value.getOperation()));

         return false;
      }
   }

   public List<ItemSetting<?>> getSettingsFromLevel(int level) {
      --level;
      return Arrays.asList((ItemSetting)this.buy.get(level), (ItemSetting)this.buyPrices.get(level), (ItemSetting)this.sell.get(level), (ItemSetting)this.sellPrices.get(level));
   }

   private Multimap<Attribute, AttributeModifier> attributesFromLists(List<Attribute> list, List<AttributeModifier> modifiers) {
      Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         Attribute attribute = (Attribute)var4.next();
         AttributeModifier modifier = (AttributeModifier)modifiers.get(list.indexOf(attribute));
         attributes.put(attribute, modifier);
      }

      return attributes;
   }

   public void setBuy(int index, boolean value) {
      --index;
      ((BoolSetting)this.buy.get(index)).setVal(value);
   }

   public void setSell(int index, boolean value) {
      --index;
      ((BoolSetting)this.buy.get(index)).setVal(value);
   }

   public void setPrice(int index, BigDecimal price) {
      --index;
      ((BDSetting)this.buyPrices.get(index)).setVal(price);
   }

   public void setSellPrice(int index, BigDecimal price) {
      --index;
      ((BDSetting)this.sellPrices.get(index)).setVal(price);
   }

   public BigDecimal getPriceFromLevel(int level) {
      --level;
      return (BigDecimal)((BDSetting)this.buyPrices.get(level)).getVal();
   }

   public BigDecimal getSellPriceFromLevel(int level) {
      --level;
      return (BigDecimal)((BDSetting)this.sellPrices.get(level)).getVal();
   }

   public LevelCheck getLevelCheck() {
      return this.levelCheck;
   }

   public int getLevel() {
      return this.level;
   }

   public List<BoolSetting> getBuy() {
      return this.buy;
   }

   public List<BDSetting> getBuyPrices() {
      return this.buyPrices;
   }

   public List<BoolSetting> getSell() {
      return this.sell;
   }

   public List<BDSetting> getSellPrices() {
      return this.sellPrices;
   }

   public void setLevelCheck(LevelCheck levelCheck) {
      this.levelCheck = levelCheck;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setBuy(List<BoolSetting> buy) {
      this.buy = buy;
   }

   public void setBuyPrices(List<BDSetting> buyPrices) {
      this.buyPrices = buyPrices;
   }

   public void setSell(List<BoolSetting> sell) {
      this.sell = sell;
   }

   public void setSellPrices(List<BDSetting> sellPrices) {
      this.sellPrices = sellPrices;
   }
}
