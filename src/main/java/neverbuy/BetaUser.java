package neverbuy;


public class BetaUser {
   private final String name;
   private final int uid;

   public BetaUser(String name, int uid) {
      this.name = name;
      this.uid = uid;
   }

   public String getName() {
      return this.name;
   }

   public int getUid() {
      return this.uid;
   }
}
