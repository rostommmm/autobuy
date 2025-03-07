package neverbuy.util.misc;

import java.awt.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import neverbuy.util.IUtil;
import neverbuy.util.render.ColorHelper;

public final class ChatUtil implements IUtil {
   public static void addMessage(Object message) {
      try {
         Color colorOne = new Color(4408534);
         Color colorTwo = new Color(410310);
         mc.ingameGUI.getChatGUI().printChatMessage(gradient(colorOne.getRGB(), colorTwo.getRGB()).append(new StringTextComponent(TextFormatting.RED + " -> " + TextFormatting.RESET + message)));
      } catch (Exception var3) {
      }

   }

   private static StringTextComponent gradient(int first, int end) {
      StringTextComponent text = new StringTextComponent("");

      for(int i = 0; i < "NeverBuy".length(); ++i) {
         text.append((new StringTextComponent(String.valueOf("NeverBuy".charAt(i)))).setStyle(Style.EMPTY.setColor(net.minecraft.util.text.Color.fromInt(ColorHelper.interpolateColor(first, end, (float)i / (float)"NeverBuy".length())))));
      }

      return text;
   }

   private ChatUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
