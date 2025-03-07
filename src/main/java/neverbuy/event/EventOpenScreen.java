package neverbuy.event;

public class EventOpenScreen {
   private Object screen;

   public Object getScreen() {
      return this.screen;
   }

   public EventOpenScreen(Object screen) {
      this.screen = screen;
   }
}
