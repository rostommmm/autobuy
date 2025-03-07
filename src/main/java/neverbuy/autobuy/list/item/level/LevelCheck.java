package neverbuy.autobuy.list.item.level;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface LevelCheck {
   int check(LevelItem var1, ItemStack var2);
}
