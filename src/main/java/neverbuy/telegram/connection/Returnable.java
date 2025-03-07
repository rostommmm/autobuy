package neverbuy.telegram.connection;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface Returnable {
   void returnSome(HttpURLConnection var1) throws IOException, InterruptedException;
}
