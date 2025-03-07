package neverbuy.autobuy;

import net.minecraft.client.Minecraft;

public interface IBuy {
   Minecraft mc = Minecraft.getInstance();

   void autoBuy();

   void autoSell();

   void autoCost();

   void onEnable();

   void onDisable();
}
