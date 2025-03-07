package neverbuy.event;

public class EventMessage extends Cancel {
   private String msg;

   public EventMessage(String msg) {
      this.msg = msg;
   }

   public String getMsg() {
      return this.msg;
   }
}
