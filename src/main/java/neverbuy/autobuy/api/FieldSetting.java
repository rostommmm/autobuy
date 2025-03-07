package neverbuy.autobuy.api;

import com.google.gson.JsonObject;

public class FieldSetting extends Setting<String, FieldSetting> {
   private String defaultValue;

   public FieldSetting setValue(String value) {
      this.value = value;
      return this;
   }

   public FieldSetting setName(String name) {
      this.name = name;
      return this;
   }

   public static FieldSetting newInstance(String name, String configName, String value) {
      FieldSetting checkBox = new FieldSetting();
      checkBox.setName(name);
      checkBox.setValue(value);
      checkBox.configName = configName;
      checkBox.defaultValue = value;
      return checkBox;
   }

   public void configure(JsonObject jsonObject) {
      JsonObject object = new JsonObject();
      object.addProperty("value", (String)this.value);
      jsonObject.add(this.getConfigName(), object);
   }

   public void read(JsonObject jsonObject) {
      JsonObject thisObject = jsonObject.get(this.getConfigName()).getAsJsonObject();
      this.setValue(thisObject.get("value").getAsString());
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }
}
