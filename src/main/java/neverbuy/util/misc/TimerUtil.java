package neverbuy.util.misc;

public class TimerUtil {
   private long lastMs;

   public TimerUtil() {
      this.update();
   }

   public boolean hasPassed(float milli, boolean reset) {
      boolean flag = (float)(System.currentTimeMillis() - this.lastMs) >= milli;
      if (flag && reset) {
         this.update();
      }

      return flag;
   }

   public boolean hasPassed(float milli) {
      return (float)(System.currentTimeMillis() - this.lastMs) >= milli;
   }

   public void update() {
      this.lastMs = System.currentTimeMillis();
   }
}
