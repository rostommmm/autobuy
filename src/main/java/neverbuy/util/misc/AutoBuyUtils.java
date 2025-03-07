package neverbuy.util.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import neverbuy.Constants;
import neverbuy.autobuy.list.AutoBuyList;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.autobuy.list.item.ClientItem;
import neverbuy.autobuy.list.item.ShulkerBoxItem;
import neverbuy.autobuy.list.item.level.LevelItem;
import neverbuy.util.IUtil;

public final class AutoBuyUtils implements IUtil {
   public static String getSeller(ItemStack stack) {
      List<ITextComponent> itemTooltip = getTooltip(stack);
      Iterator var2 = itemTooltip.iterator();

      while(var2.hasNext()) {
         ITextComponent line = (ITextComponent)var2.next();

         try {
            if (line.getString().contains("⚕ Прoдaвeц: ")) {
               return line.getString().replace("⚕ Прoдaвeц: ", "").replaceAll("\\$", "").replaceAll(" ", "");
            }
         } catch (Exception var5) {
         }
      }

      return "a";
   }

   public static int getPrice(ItemStack stack) {
      List<ITextComponent> itemTooltip = getTooltip(stack);
      Iterator var2 = itemTooltip.iterator();

      while(var2.hasNext()) {
         ITextComponent line = (ITextComponent)var2.next();

         try {
            if (line.getString().contains("$ Ценa $") && ((ITextComponent)itemTooltip.get(itemTooltip.indexOf(line) + 1)).getString().contains("⚕")) {
               String part = line.getString().replace("$ Ценa $", "").replaceAll("\\$", "").replaceAll(" ", "");
               return Integer.parseInt(part);
            }
         } catch (Exception var5) {
         }
      }

      return -1;
   }

   public static List<ITextComponent> getTooltip(ItemStack stack) {
      return stack.getTooltip(mc.player, mc.gameSettings.advancedItemTooltips ? TooltipFlags.ADVANCED : TooltipFlags.NORMAL);
   }

   public static boolean isValid(ItemStack stack) {
      int price = getPrice(stack);
      String seller = getSeller(stack);
      if (price != -1 && !seller.equalsIgnoreCase("a")) {
         if ((float)price > Constants.AUTOBUY.getBalance().floatValue()) {
            return false;
         } else {
            boolean flag = stack.getItem() instanceof BlockItem && ((BlockItem)stack.getItem()).getBlock() instanceof ShulkerBoxBlock;
            boolean allowedItem = stack.getItem() == Items.GOLD_BLOCK || stack.getItem() == Items.TOTEM_OF_UNDYING || stack.getItem() == Items.PLAYER_HEAD || stack.getItem() == Items.SPLASH_POTION || flag;
            return price % 10 == 0 && !allowedItem ? false : checkValidItem(stack, price);
         }
      } else {
         return false;
      }
   }

   private static boolean checkValidItem(ItemStack stack, int price) {
      AutoBuyItem<?> item = Constants.LIST.getItem(stack);
      if (item != null) {
         if (!(item instanceof ShulkerBoxItem)) {
            if (!item.isBuy()) {
               return false;
            } else if (item instanceof LevelItem) {
               LevelItem levelItem = (LevelItem)item;
               int level = levelItem.getLevel();
               return levelItem.getPriceFromLevel(level).floatValue() * (float)stack.getCount() >= (float)price;
            } else if (item.getBuyPrice() <= 0.0F) {
               return false;
            } else {
               if (item.isArmor() && item instanceof ClientItem) {
                  Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
                  if (enchantments.containsKey(Enchantments.THORNS) && !((ClientItem)item).isThorns()) {
                     return false;
                  }
               }

               return item.getBuyPrice() * (float)stack.getCount() >= (float)price;
            }
         } else {
            return getShulkerPrice(stack) >= (float)price;
         }
      } else {
         return false;
      }
   }

   public static List<ItemStack> getShulkerBoxItems(ItemStack stack) {
      List<ItemStack> items = new ArrayList();
      boolean flag = stack.getItem() instanceof BlockItem && ((BlockItem)stack.getItem()).getBlock() instanceof ShulkerBoxBlock;
      if (flag && stack.hasTag()) {
         assert stack.getTag() != null;

         CompoundNBT blockEntityTag = stack.getTag().getCompound("BlockEntityTag");
         if (!blockEntityTag.contains("Items", 9)) {
            return items;
         } else {
            ListNBT itemsList = blockEntityTag.getList("Items", 10);

            for(int i = 0; i < itemsList.size(); ++i) {
               CompoundNBT itemNBT = itemsList.getCompound(i);
               ItemStack itemStack = ItemStack.read(itemNBT);
               items.add(itemStack);
            }

            return items;
         }
      } else {
         return items;
      }
   }

   public static float getShulkerPrice(ItemStack stack1) {
      List<ItemStack> items = getShulkerBoxItems(stack1);
      float price = 0.0F;
      AutoBuyList list = Constants.LIST;
      if (items.isEmpty()) {
         return list.getItem("Шалкер").getBuyPrice();
      } else {
         Iterator var4 = items.iterator();

         while(var4.hasNext()) {
            ItemStack stack = (ItemStack)var4.next();
            AutoBuyItem<?> item = list.getItem(stack);
            if (item != null) {
               if (item instanceof LevelItem) {
                  LevelItem levelItem = (LevelItem)item;
                  price += ((LevelItem)item).getPriceFromLevel(levelItem.getLevel()).floatValue() * (float)stack.getCount();
               } else {
                  price += item.getBuyPrice() * (float)stack.getCount();
               }
            }
         }

         return price;
      }
   }

   public static int indexOf(List<ITextComponent> t, String n) {
      Iterator var2 = t.iterator();

      ITextComponent text;
      do {
         if (!var2.hasNext()) {
            return -1;
         }

         text = (ITextComponent)var2.next();
      } while(!text.getString().equalsIgnoreCase(n));

      return t.indexOf(text);
   }

   public static String getShulkerStrings(ItemStack stack) {
      boolean flag = stack.getItem() instanceof BlockItem && ((BlockItem)stack.getItem()).getBlock() instanceof ShulkerBoxBlock;
      if (!flag) {
         return "";
      } else {
         List<ItemStack> stacks = getShulkerBoxItems(stack);
         if (stacks.isEmpty()) {
            return " Пустой";
         } else {
            StringBuilder full = new StringBuilder(" Содержимое шалкера:\n");

            ItemStack shulkerStack;
            String itemName;
            for(Iterator var4 = stacks.iterator(); var4.hasNext(); full.append(stacks.indexOf(shulkerStack) + 1).append(": ").append(itemName).append(" x").append(shulkerStack.getCount()).append("\n")) {
               shulkerStack = (ItemStack)var4.next();
               itemName = " " + shulkerStack.getDisplayName().getString();
               AutoBuyItem<?> item = Constants.LIST.getItem(shulkerStack);
               if (item != null) {
                  itemName = itemName + String.format(" (%s)", item.getName());
               }
            }

            return full.toString();
         }
      }
   }

   private AutoBuyUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
