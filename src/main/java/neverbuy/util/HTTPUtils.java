package neverbuy.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class HTTPUtils {
   private static HttpURLConnection getHttpURLConnection(String url) {
      try {
         URL serverUrl = new URL(url);
         HttpURLConnection connection = (HttpURLConnection)serverUrl.openConnection();
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);
         connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
         return connection;
      } catch (IOException var3) {
         var3.printStackTrace(System.err);
         return null;
      }
   }

   private HTTPUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
