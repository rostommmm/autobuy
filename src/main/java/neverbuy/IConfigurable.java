package neverbuy;

import com.google.gson.JsonObject;

public interface IConfigurable {
   void configure(JsonObject var1);

   void read(JsonObject var1);
}
