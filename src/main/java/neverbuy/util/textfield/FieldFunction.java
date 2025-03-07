package neverbuy.util.textfield;

public interface FieldFunction {
   void onChar(String var1);

   default void onEnter(String full) {
   }
}
