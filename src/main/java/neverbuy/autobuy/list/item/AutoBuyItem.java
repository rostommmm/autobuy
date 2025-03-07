package neverbuy.autobuy.list.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import neverbuy.Constants;
import neverbuy.IConfigurable;
import neverbuy.autobuy.list.item.setting.BDSetting;
import neverbuy.autobuy.list.item.setting.BoolSetting;
import neverbuy.autobuy.list.item.setting.ItemSetting;
import neverbuy.util.misc.ChatUtil;

public abstract class AutoBuyItem<T> implements IConfigurable {
   protected String name;
   protected Item item;
   protected ItemStack stack;
   protected boolean containsStack;
   private final List<Integer> minPrices = new ArrayList();
   private final List<ItemSetting<?>> settings = new ArrayList();
   public boolean added;
   public boolean isConstant = this instanceof ClientItem;

   public AutoBuyItem() {
      this.settings.add(new BoolSetting("Покупать", true));
      this.settings.add((new BDSetting("Цена покупки", new BigDecimal(100))).setVisible(() -> {
         return (Boolean)((ItemSetting)this.settings.get(0)).getAsBool().getVal();
      }));
      this.settings.add(new BoolSetting("Продавать", true));
      this.settings.add((new BDSetting("Цена продажи", new BigDecimal(100))).setVisible(() -> {
         return (Boolean)((ItemSetting)this.settings.get(2)).getAsBool().getVal();
      }));
      if (Constants.ALPHA) {
         this.settings.add(new BoolSetting("Учитывать в авто-ценах", true));
         this.settings.add((new BDSetting("Наценка в покупку", new BigDecimal(20))).setVisible(() -> {
            return (Boolean)((ItemSetting)this.settings.get(4)).getAsBool().getVal();
         }));
         this.settings.add((new BDSetting("Наценка в продажу", new BigDecimal(20))).setVisible(() -> {
            return (Boolean)((ItemSetting)this.settings.get(4)).getAsBool().getVal();
         }));
         this.settings.add((new BDSetting("Количество", new BigDecimal(15))).setVisible(() -> {
            return (Boolean)((ItemSetting)this.settings.get(4)).getAsBool().getVal();
         }));
      }

   }

   public abstract T name(String var1);

   public abstract T item(Item var1);

   public abstract T price(BigDecimal var1);

   public abstract T stack(ItemStack var1);

   public abstract T sell(BigDecimal var1);

   public abstract void buying(boolean var1);

   public abstract void selling(boolean var1);

   public boolean isArmor() {
      return this.item instanceof ArmorItem;
   }

   public ItemSetting<?> getSettingByName(String name) {
      Iterator var2 = this.settings.iterator();

      ItemSetting setting;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         setting = (ItemSetting)var2.next();
      } while(!setting.getName().equalsIgnoreCase(name));

      return setting;
   }

   public void putPrice(int price) {
      if (this.minPrices.size() < this.getCount()) {
         if (!this.added) {
            this.minPrices.add(price);
         }
      } else {
         int sum = 0;

         int p;
         for(Iterator var3 = this.minPrices.iterator(); var3.hasNext(); sum += p) {
            p = (Integer)var3.next();
         }

         int average = sum / this.minPrices.size();
         p = average * ((BigDecimal)this.getSettingByName("Наценка в покупку").getAsBigDecimal().getVal()).intValue() / 100;
         int markupSell = average * ((BigDecimal)this.getSettingByName("Наценка в продажу").getAsBigDecimal().getVal()).intValue() / 100;
         BigDecimal buyDecimal = BigDecimal.valueOf((long)(average - p));
         BigDecimal sellDecimal = BigDecimal.valueOf((long)(average + markupSell));
         this.price(buyDecimal);
         this.sell(sellDecimal);
         this.minPrices.clear();
         String var10000 = this.name;
         ChatUtil.addMessage("Конечная цена предмета \"" + var10000 + "\": " + buyDecimal.intValue());
         var10000 = this.name;
         ChatUtil.addMessage("Конечная цена продажи предмета \"" + var10000 + "\": " + sellDecimal.intValue());
      }

   }

   public float getBuyPrice() {
      return ((BigDecimal)this.getSettingByName("Цена покупки").getAsBigDecimal().getVal()).floatValue();
   }

   public float getSellPrice() {
      return ((BigDecimal)this.getSettingByName("Цена продажи").getAsBigDecimal().getVal()).floatValue();
   }

   public boolean isBuy() {
      return (Boolean)this.getSettingByName("Покупать").getAsBool().getVal();
   }

   public boolean isSell() {
      return (Boolean)this.getSettingByName("Продавать").getAsBool().getVal();
   }

   public boolean isUseAutoCost() {
      return (Boolean)this.getSettingByName("Учитывать в авто-ценах").getAsBool().getVal();
   }

   private int getCount() {
      return ((BigDecimal)this.getSettingByName("Количество").getAsBigDecimal().getVal()).intValue();
   }

   public String getName() {
      return this.name;
   }

   public Item getItem() {
      return this.item;
   }

   public ItemStack getStack() {
      return this.stack;
   }

   public boolean isContainsStack() {
      return this.containsStack;
   }

   public List<Integer> getMinPrices() {
      return this.minPrices;
   }

   public List<ItemSetting<?>> getSettings() {
      return this.settings;
   }

   public boolean isAdded() {
      return this.added;
   }

   public boolean isConstant() {
      return this.isConstant;
   }
}
