package neverbuy.autobuy.list.item.setting;

import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.function.Supplier;

public class BDSetting extends ItemSetting<BigDecimal> {
   public BDSetting(String name, BigDecimal bigDecimal) {
      super(name, bigDecimal);
   }

   public BDSetting setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
      return this;
   }

   public void configure(JsonObject jsonObject) {
      JsonObject thisObject = new JsonObject();
      thisObject.addProperty("value", (Number)this.getVal());
      jsonObject.add(this.getName(), thisObject);
   }

   public void read(JsonObject jsonObject) {
      JsonObject thisObject = jsonObject.get(this.getName()).getAsJsonObject();
      this.setVal(thisObject.get("value").getAsBigDecimal());
   }
}
