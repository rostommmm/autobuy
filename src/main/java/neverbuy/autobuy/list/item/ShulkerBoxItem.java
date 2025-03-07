package neverbuy.autobuy.list.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;

public class ShulkerBoxItem extends ClientItem {
   private final List<String> tags = new ArrayList();

   public ShulkerBoxItem name(String name) {
      this.name = name;
      return this;
   }

   public ShulkerBoxItem displayName(String name) {
      super.displayName(name);
      return this;
   }

   public ShulkerBoxItem item(Item item) {
      this.item = item;
      return this;
   }

   public ShulkerBoxItem price(BigDecimal price) {
      this.getSettingByName("Цена покупки").getAsBigDecimal().setVal(price);
      return this;
   }

   public ShulkerBoxItem check(Check check) {
      this.check = check;
      return this;
   }

   public void addTag(String tag) {
      this.tags.add(tag);
   }

   public boolean containsTag(CompoundNBT tag) {
      if (tag == null) {
         return false;
      } else {
         Iterator var2 = this.tags.iterator();

         String str;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            str = (String)var2.next();
         } while(!tag.contains(str));

         return true;
      }
   }
}
