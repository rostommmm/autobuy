package neverbuy.event;

public class EventChat extends Cancel {
   private String msg;

   public String getMsg() {
      return this.msg;
   }

   public EventChat(String msg) {
      this.msg = msg;
   }
}
