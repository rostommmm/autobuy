package neverbuy.autobuy.api;

import neverbuy.IConfigurable;

public abstract class Setting<T, S> implements IConfigurable {
   protected String configName;
   protected String name;
   protected T value;

   public abstract S setValue(T var1);

   public abstract S setName(String var1);

   public String getConfigName() {
      return this.configName;
   }

   public String getName() {
      return this.name;
   }

   public T getValue() {
      return this.value;
   }
}
