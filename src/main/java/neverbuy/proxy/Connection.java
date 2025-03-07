package neverbuy.proxy;

import java.net.InetSocketAddress;

public class Connection {
   private Type proxyType;
   private InetSocketAddress proxyAddr;
   private String username;
   private String password;

   public Connection() {
      this.proxyType = Type.DIRECT;
      this.proxyAddr = null;
      this.username = "";
      this.password = "";
   }

   public void reset() {
      this.proxyType = Type.DIRECT;
      this.proxyAddr = null;
      this.username = "";
      this.password = "";
   }

   public void setProxyType(Type proxyType) {
      this.proxyType = proxyType;
   }

   public void setProxyAddr(InetSocketAddress proxyAddr) {
      this.proxyAddr = proxyAddr;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Type getProxyType() {
      return this.proxyType;
   }

   public InetSocketAddress getProxyAddr() {
      return this.proxyAddr;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }
}
