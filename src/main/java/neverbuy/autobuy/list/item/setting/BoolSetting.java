package neverbuy.autobuy.list.item.setting;

import com.google.gson.JsonObject;
import java.util.function.Supplier;

public class BoolSetting extends ItemSetting<Boolean> {
   public BoolSetting(String name, boolean aBoolean) {
      super(name, aBoolean);
   }

   public BoolSetting setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
      return this;
   }

   public void configure(JsonObject jsonObject) {
      JsonObject thisObject = new JsonObject();
      thisObject.addProperty("value", (Boolean)this.getVal());
      jsonObject.add(this.getName(), thisObject);
   }

   public void read(JsonObject jsonObject) {
      JsonObject thisObject = jsonObject.get(this.getName()).getAsJsonObject();
      this.setVal(thisObject.get("value").getAsBoolean());
   }
}
