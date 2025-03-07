package neverbuy.proxy;

public enum Type {
   DIRECT("None"),
   SOCKS4("Socks4"),
   SOCKS5("Socks5"),
   HTTP("HTTP");

   final String name;

   public String getName() {
      return this.name;
   }

   private Type(String name) {
      this.name = name;
   }

   // $FF: synthetic method
   private static Type[] $values() {
      return new Type[]{DIRECT, SOCKS4, SOCKS5, HTTP};
   }
}
