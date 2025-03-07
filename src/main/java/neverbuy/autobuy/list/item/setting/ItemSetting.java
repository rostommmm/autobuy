package neverbuy.autobuy.list.item.setting;

import java.util.function.Supplier;
import neverbuy.IConfigurable;

public abstract class ItemSetting<Val> implements IConfigurable {
   private final String name;
   private Val val;
   protected Supplier<Boolean> visible = () -> {
      return true;
   };

   public ItemSetting(String name, Val val) {
      this.name = name;
      this.val = val;
   }

   public BDSetting getAsBigDecimal() {
      return (BDSetting)this;
   }

   public boolean isVisible() {
      return (Boolean)this.visible.get();
   }

   public BoolSetting getAsBool() {
      return (BoolSetting)this;
   }

   public double getOffset() {
      return 15.0D;
   }

   public String getName() {
      return this.name;
   }

   public Val getVal() {
      return this.val;
   }

   public Supplier<Boolean> getVisible() {
      return this.visible;
   }

   public void setVal(Val val) {
      this.val = val;
   }
}
