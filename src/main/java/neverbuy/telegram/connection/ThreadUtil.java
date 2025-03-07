package neverbuy.telegram.connection;

public class ThreadUtil {
   public static void runInOtherThread(Runnable runnable) {
      (new Thread(runnable)).start();
   }
}
