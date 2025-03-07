package neverbuy.autobuy.list.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface Check {
   boolean check(ClientItem var1, ItemStack var2);
}
