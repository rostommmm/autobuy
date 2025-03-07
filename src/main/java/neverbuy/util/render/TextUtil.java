package neverbuy.util.render;

import neverbuy.util.IUtil;
import neverbuy.util.misc.TimerUtil;

public class TextUtil implements IUtil {
   private final String[] texts;
   private String fullText = "";
   private final TimerUtil textTimer = new TimerUtil();
   private int currentIndex;
   private int currentCharIndex;
   private boolean back;

   public TextUtil(String... texts) {
      this.texts = texts;
      this.currentIndex = 0;
   }

   public void tick() {
      if (this.textTimer.hasPassed(this.isFull() && this.back ? 700.0F : 150.0F, true)) {
         String s = this.texts[this.currentIndex];
         char[] ar = s.toCharArray();
         if (!this.isFull() && !this.back) {
            this.fullText = this.fullText + ar[this.currentCharIndex];
            if (this.currentCharIndex < ar.length - 1) {
               ++this.currentCharIndex;
            } else {
               this.back = true;
            }
         } else {
            this.fullText = this.fullText.substring(0, this.fullText.length() - 1);
            if (this.fullText.isEmpty()) {
               this.currentCharIndex = 0;
               this.back = false;
               if (this.currentIndex == this.texts.length - 1) {
                  this.currentIndex = 0;
               } else {
                  ++this.currentIndex;
               }
            }
         }
      }

   }

   private boolean isFull() {
      String[] var1 = this.texts;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String s = var1[var3];
         if (this.fullText.equals(s)) {
            return true;
         }
      }

      return false;
   }

   public String getFullText() {
      return this.fullText;
   }
}
