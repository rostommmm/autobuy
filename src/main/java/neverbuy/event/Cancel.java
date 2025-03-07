package neverbuy.event;

public class Cancel {
   private boolean canceled;

   public void cancel() {
      this.canceled = true;
   }

   public boolean isCanceled() {
      return this.canceled;
   }
}
