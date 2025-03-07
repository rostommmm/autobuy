package neverbuy.telegram.connection;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Packet {
   private final String host;
   private final String[] args;
   private final Returnable thr;
   public static long lastPacketTime;
   public static long maxPacketDelay = 200L;
   private boolean post;

   private Packet(String host, Returnable thr, String... args) {
      String finalHost = host;
      if (!host.contains("https://") && !host.contains("http://")) {
         finalHost = "https://" + host;
      }

      this.host = finalHost;
      this.thr = thr;
      this.args = args;
   }

   public static Packet newInstance(String host, Returnable returnable, String... args) {
      return new Packet(host, returnable, args);
   }

   public void send() {
      try {
         lastPacketTime = System.currentTimeMillis();
         StringBuilder args = new StringBuilder();

         for(int i = 0; i < this.args.length; ++i) {
            args.append(this.args[i]);
            if (i < this.args.length - 1) {
               args.append("/");
            }
         }

         String fullString = this.host + "/" + args;
         URL url = new URL(fullString);
         HttpURLConnection connection = (HttpURLConnection)url.openConnection();
         if (this.post) {
            this.thr.returnSome(connection);
         }

         connection.getResponseCode();
         if (!this.post) {
            this.thr.returnSome(connection);
         }

         connection.disconnect();
      } catch (Exception var5) {
         if (var5 instanceof FileNotFoundException) {
            return;
         }

         var5.printStackTrace(System.err);
      }

   }

   public Packet setPost(boolean b) {
      this.post = b;
      return this;
   }

   public String getHost() {
      return this.host;
   }
}
