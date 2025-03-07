package neverbuy.autobuy.api;

import com.google.gson.JsonObject;
import java.math.BigDecimal;

public class Delay extends Setting<BigDecimal, Delay> {
   private BigDecimal defaultValue;

   public static Delay newInstance(String name, String configName, float value) {
      Delay delay = new Delay();
      BigDecimal bigDecimal = BigDecimal.valueOf((double)value);
      delay.setValue(bigDecimal);
      delay.configName = configName;
      delay.defaultValue = bigDecimal;
      delay.setName(name);
      return delay;
   }

   public Delay setValue(BigDecimal value) {
      this.value = value;
      return this;
   }

   public Delay setName(String name) {
      this.name = name;
      return this;
   }

   public float getDelay() {
      return ((BigDecimal)this.getValue()).floatValue();
   }

   public void resetToDefault() {
      this.setValue(this.defaultValue);
   }

   public void configure(JsonObject jsonObject) {
      JsonObject delayObject = new JsonObject();
      delayObject.addProperty("value", ((BigDecimal)this.getValue()).floatValue());
      jsonObject.add(this.configName, delayObject);
   }

   public void read(JsonObject jsonObject) {
      System.out.println(jsonObject);
      this.setValue(BigDecimal.valueOf((double)jsonObject.get(this.getConfigName()).getAsJsonObject().get("value").getAsFloat()));
   }

   public BigDecimal getDefaultValue() {
      return this.defaultValue;
   }
}
