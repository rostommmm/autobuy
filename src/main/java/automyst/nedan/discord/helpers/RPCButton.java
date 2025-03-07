package automyst.nedan.discord.helpers;

import java.io.Serializable;

public class RPCButton implements Serializable {
   private final String url;
   private final String label;

   public static RPCButton create(String substring, String s) {
      substring = substring.substring(0, Math.min(substring.length(), 31));
      return new RPCButton(substring, s);
   }

   protected RPCButton(String label, String url) {
      this.label = label;
      this.url = url;
   }

   public String getUrl() {
      return this.url;
   }

   public String getLabel() {
      return this.label;
   }
}
