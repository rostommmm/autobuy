package neverbuy.autobuy.api;

import com.google.gson.JsonObject;

public class CheckBox extends Setting<Boolean, CheckBox> {
   private boolean defaultValue;

   public static CheckBox newInstance(String name, String configName, boolean value) {
      CheckBox checkBox = new CheckBox();
      checkBox.setName(name);
      checkBox.setValue(value);
      checkBox.configName = configName;
      checkBox.defaultValue = value;
      return checkBox;
   }

   public CheckBox setValue(Boolean value) {
      this.value = value;
      return this;
   }

   public CheckBox setName(String name) {
      this.name = name;
      return this;
   }

   public void resetToDefault() {
      this.setValue(this.defaultValue);
   }

   public void configure(JsonObject jsonObject) {
      JsonObject checkBoxObject = new JsonObject();
      checkBoxObject.addProperty("value", (Boolean)this.getValue());
      jsonObject.add(this.configName, checkBoxObject);
   }

   public void read(JsonObject jsonObject) {
      this.setValue(jsonObject.get(this.getConfigName()).getAsJsonObject().get("value").getAsBoolean());
   }

   public boolean isDefaultValue() {
      return this.defaultValue;
   }
}
