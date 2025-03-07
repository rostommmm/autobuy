package neverbuy.autobuy;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ShulkerBoxScreen;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ShulkerBoxContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CClickWindowPacket;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import neverbuy.Constants;
import neverbuy.autobuy.api.CheckBox;
import neverbuy.autobuy.api.Delay;
import neverbuy.autobuy.api.FieldSetting;
import neverbuy.autobuy.api.Setting;
import neverbuy.autobuy.list.AutoBuyList;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.autobuy.list.item.ShulkerBoxItem;
import neverbuy.autobuy.list.item.level.LevelItem;
import neverbuy.chatcommands.api.ChatCommand;
import neverbuy.event.EventActionBar;
import neverbuy.event.EventKey;
import neverbuy.event.EventMessage;
import neverbuy.event.EventSound;
import neverbuy.event.EventTick;
import neverbuy.telegram.TelegramApi;
import neverbuy.telegram.command.impl.ChangePriceCommand;
import neverbuy.util.misc.AutoBuyUtils;
import neverbuy.util.misc.ChatUtil;
import neverbuy.util.misc.TimerUtil;

public class AutoBuy implements IBuy {
   private final CheckBox autoSell = CheckBox.newInstance("Автоселл", "autoSell", true);
   private final CheckBox autoAuc = CheckBox.newInstance("Авто-открытие", "autoAuc", true);
   private final CheckBox resell = CheckBox.newInstance("Перевыставка", "resell", true);
   private final CheckBox autoRCT = CheckBox.newInstance("Авто-перезаход", "autoRCT", true);
   private final Delay update = Delay.newInstance("Обновление", "updateDelay", 1000.0F);
   private final Delay buy = Delay.newInstance("Покупка", "buyDelay", 100.0F);
   private final Delay autoAucDelay = Delay.newInstance("Задержка открытия", "autoAucDelay", 1500.0F);
   private final Delay resellDelay = Delay.newInstance("Задержка перевыставки", "resellDelay", 120000.0F);
   private final Delay autoRCTDelay = Delay.newInstance("Задержка на перезаход", "autoRCTDelay", 600000.0F);
   private final FieldSetting search = FieldSetting.newInstance("Nскать предмет", "search", "");
   private final CheckBox sellFromShulker = CheckBox.newInstance("Продавать с шалкера", "sellFromShulker", true);
   private final TimerUtil[] timers = new TimerUtil[]{new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil(), new TimerUtil()};
   private final TimerUtil[] autoCostTimers = new TimerUtil[]{new TimerUtil(), new TimerUtil(), new TimerUtil()};
   private final TimerUtil shulkerTimer = new TimerUtil();
   private boolean enabled;
   private BigDecimal balance = new BigDecimal(0);
   private BigDecimal sellBalance = new BigDecimal(0);
   private BigDecimal buyBalance = new BigDecimal(0);
   private AutoBuy.HistoryItem lastItem;
   public boolean sellNotification = true;
   public boolean buyNotification = true;
   public boolean notBuyNotification = true;
   private long lastMs = -1L;
   private long ping;
   private boolean reselling = false;
   private boolean selling = false;
   private boolean waiting;
   private boolean skip = false;
   private boolean added = false;
   private boolean checked;
   private final List<ItemStack> costed = new ArrayList();

   @Subscribe
   public void onTick(EventTick e) {
      try {
         if (Constants.DISTRUCTED) {
            return;
         }

         this.autoBuy();
         this.autoSell();
         this.autoCost();
      } catch (Exception var3) {
         var3.printStackTrace(System.err);
      }

   }

   @Subscribe
   public void onKey(EventKey eventKey) {
      if (!Constants.DISTRUCTED) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ChestScreen) {
            if (eventKey.getKey() == 79) {
               this.enabled = !this.enabled;
               if (this.enabled) {
                  this.onEnable();
               } else {
                  this.onDisable();
               }
            }

            if (eventKey.getKey() == 327) {
               Constants.DEBUG = !Constants.DEBUG;
            }

            if (eventKey.getKey() == 326) {
               Constants.LIST = new AutoBuyList();
               ChatUtil.addMessage("Перезагрузил автобай!");
            }
         }

      }
   }

   public void onEnable() {
      ChatUtil.addMessage("Автобай успешно включен!");
      TimerUtil[] var1 = this.timers;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         TimerUtil timerUtil = var1[var3];
         if (!timerUtil.equals(this.timers[8])) {
            timerUtil.update();
         }
      }

      this.checked = false;
   }

   public void onDisable() {
      ChatUtil.addMessage("Автобай успешно выключен!");
   }

   public void onHoverSlot(Slot slot) {
      AutoBuyItem<?> item = Constants.LIST.getItem(slot.getStack());
      if (item != null && item instanceof LevelItem) {
         String var10000 = item.getName();
         ChatUtil.addMessage(var10000 + ": " + ((LevelItem)item).getLevel());
      }

   }

   @Subscribe
   public void onSound(EventSound e) {
      if (e.getSoundEvent() == SoundEvents.ENTITY_ENDER_EYE_LAUNCH) {
         Screen var3 = mc.currentScreen;
         if (var3 instanceof ChestScreen) {
            ChestScreen chest = (ChestScreen)var3;
            if (chest.getTitle().getString().contains("Аукционы") || chest.getTitle().getString().contains("Поиск")) {
               this.checked = false;
               this.ping = System.currentTimeMillis() - this.lastMs;
               this.setFalse();
            }
         }
      }

   }

   public void autoCost() {
      if (Constants.ALPHA) {
         if (mc.currentScreen instanceof ChestScreen && !this.added) {
            ChestScreen screen = (ChestScreen)mc.currentScreen;
            List<ItemStack> stacks = ((ChestContainer)screen.getContainer()).getInventory().stream().filter((stackx) -> {
               return AutoBuyUtils.getPrice(stackx) != -1;
            }).sorted(Comparator.comparingInt(AutoBuyUtils::getPrice)).toList();
            Iterator var3 = stacks.iterator();

            while(var3.hasNext()) {
               ItemStack stack = (ItemStack)var3.next();
               AutoBuyItem<?> item = Constants.LIST.getItem(stack);
               if (item != null && !(item instanceof ShulkerBoxItem) && item.isUseAutoCost() && !this.costed.contains(stack)) {
                  item.putPrice(AutoBuyUtils.getPrice(stack) / stack.getCount());
                  item.added = true;
                  this.costed.add(stack);
               }
            }

            this.added = true;
         }

      }
   }

   @Subscribe
   public void onActionBar(EventActionBar e) {
      if (Constants.ALPHA) {
         if (e.getMessage().contains("Анализируем")) {
            ChatUtil.addMessage("Вы на проверке!");

            for(int i = 0; i < 3; ++i) {
               Constants.TELEGRAM.sendMessage("Вы на проверке!");
            }

            this.enabled = false;
         }

      }
   }

   @Subscribe
   public void onChat(EventMessage e) {
      if (!Constants.DISTRUCTED) {
         String msg = e.getMsg();
         if (this.enabled) {
            assert mc.player != null;

            String[] split;
            if (msg.contains("Ваш баланс")) {
               split = msg.split(" ");
               this.balance = BigDecimal.valueOf(Double.parseDouble(split[3].replaceAll("\\$", "").replaceAll(",", "")));
               if (mc.currentScreen instanceof ChestScreen) {
                  e.cancel();
               }
            } else if (msg.contains("Этот товар уже купили!")) {
               if (this.notBuyNotification) {
                  Constants.TELEGRAM.sendMessage("Вы не купили предмет \"" + this.lastItem.name + "\" (" + this.lastItem.autoBuyItemName + ") у продавца " + this.lastItem.seller + " по цене " + this.lastItem.price + ". Товар скуплен." + this.lastItem.strings);
               }

               this.timers[1].update();
               this.clickSilent(mc.player.openContainer.windowId, 49);
            } else {
               String mes;
               if (msg.contains("Вы успешно купили")) {
                  if (this.buyNotification) {
                     split = msg.split(" ");
                     mes = String.join(" ", (CharSequence[])Arrays.copyOfRange(split, 4, split.length));
                     TelegramApi var10000 = Constants.TELEGRAM;
                     String var10001 = mes.replaceAll("!", "");
                     var10000.sendMessage("Вы успешно купили " + var10001 + " у продавца " + this.lastItem.seller + String.format(" (%s)", this.lastItem.autoBuyItemName) + this.lastItem.strings);
                  }

                  this.timers[1].update();
                  this.buyBalance = this.buyBalance.add(new BigDecimal(this.lastItem.price));
               } else if (msg.contains("У Вас не хватает денег!")) {
                  if (this.notBuyNotification) {
                     if (this.lastItem == null) {
                        return;
                     }

                     Constants.TELEGRAM.sendMessage("Вы не купили предмет \"" + this.lastItem.name + "\" (" + this.lastItem.autoBuyItemName + ") у продавца " + this.lastItem.seller + " по цене " + this.lastItem.price + ". Недостаточно денег." + this.lastItem.strings);
                  }

                  this.clickSilent(mc.player.openContainer.windowId, 49);
                  this.timers[1].update();
               } else if (!msg.contains("Освободите хранилище или уберите предметы с продажи") && !msg.contains("Максимальная цена")) {
                  if (msg.contains("[☃] У Вас купили")) {
                     split = msg.split(" ");
                     if (this.sellNotification) {
                        mes = String.join(" ", (CharSequence[])Arrays.copyOfRange(split, 4, split.length - 2));
                        Constants.TELEGRAM.sendMessage("У вас купили " + mes);
                     }

                     int index = -1;

                     for(int i = 0; i < split.length; ++i) {
                        String s = split[i];
                        if (s.startsWith("$") && !ChangePriceCommand.isStringOnlyDigits(s.replaceAll("\\$", ""))) {
                           index = i;
                        }
                     }

                     if (index == -1) {
                        return;
                     }

                     double z = Double.parseDouble(split[index].replaceAll("\\$", ""));
                     this.sellBalance = this.sellBalance.add(new BigDecimal(z));
                  }
               } else {
                  this.skip = true;
               }
            }

         }
      }
   }

   public void autoBuy() {
      if (this.enabled && mc.player != null) {
         Screen var2 = mc.currentScreen;
         if (var2 instanceof ChestScreen) {
            ChestScreen screen = (ChestScreen)var2;
            ChestContainer handler = (ChestContainer)screen.getContainer();
            boolean flag = screen.getTitle().getString().contains("Хранилище");
            if (screen.getTitle().getString().contains("Подтверждение") || screen.getTitle().getString().contains("Подозрительная цена") && this.timers[7].hasPassed(50.0F, true)) {
               this.clickSilent(handler.windowId, 10);
            }

            if (this.timers[9].hasPassed(30000.0F, true)) {
               mc.player.closeScreen();
               this.timers[6].update();
            }

            if (this.timers[4].hasPassed(400.0F, true) && flag) {
               if (!((ItemStack)handler.getInventory().get(0)).isEmpty()) {
                  this.clickSilent(handler.windowId, 0);
               } else {
                  mc.player.closeScreen();
                  this.timers[6].update();
                  this.reselling = false;
                  this.selling = true;
               }
            }

            if (this.reselling && !flag) {
               this.clickSilent(handler.windowId, 46);
            }

            if ((Boolean)this.autoRCT.getValue() && !this.reselling && !this.selling && this.timers[8].hasPassed(this.autoRCTDelay.getDelay())) {
               ((ChatCommand)Constants.CHAT_COMMAND_MANAGER.getChatCommands().get(1)).execute(new String[]{".rct"});
               this.timers[8].update();
            }

            if (this.timers[0].hasPassed(this.update.getDelay(), true) && !flag) {
               this.clickSilent(handler.windowId, 49);
               this.added = false;
            }

            if ((Boolean)this.resell.getValue() && this.timers[3].hasPassed(this.resellDelay.getDelay(), true)) {
               this.reselling = true;
            }

            if (this.timers[2].hasPassed(2000.0F, true)) {
               mc.player.sendChatMessage("/bal");
            }

            if (!this.checked) {
               for(Iterator var4 = ((ChestContainer)screen.getContainer()).inventorySlots.iterator(); var4.hasNext(); this.checked = true) {
                  Slot slot = (Slot)var4.next();
                  if (AutoBuyUtils.isValid(slot.getStack()) && this.timers[1].hasPassed(this.buy.getDelay(), true)) {
                     AutoBuyItem<?> item = Constants.LIST.getItem(slot.getStack());
                     this.lastItem = new AutoBuy.HistoryItem(slot.getStack().getDisplayName().getString(), item.getName() + (item instanceof LevelItem ? " уровень " + ((LevelItem)item).getLevel() : ""), AutoBuyUtils.getSeller(slot.getStack()), AutoBuyUtils.getPrice(slot.getStack()), slot.getStack().getDamage(), AutoBuyUtils.getShulkerStrings(slot.getStack()));
                     mc.playerController.windowClick(handler.windowId, slot.slotNumber, 0, ClickType.QUICK_MOVE, mc.player);
                     this.timers[0].update();
                  }

                  this.lastMs = System.currentTimeMillis();
               }
            }
         }

      }
   }

   private void setFalse() {
      AutoBuyItem item;
      for(Iterator var1 = Constants.LIST.items.iterator(); var1.hasNext(); item.added = false) {
         item = (AutoBuyItem)var1.next();
      }

   }

   private void clickSilent(int syncId, int slot) {
      assert mc.player != null;

      short s = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
      mc.player.connection.sendPacket(new CClickWindowPacket(syncId, slot, 0, ClickType.PICKUP, ItemStack.EMPTY, s));
   }

   public void autoSell() {
      if (this.enabled && mc.player != null) {
         if (this.skip || !(Boolean)this.autoSell.getValue()) {
            this.selling = false;
         }

         if (Constants.ALPHA && (Boolean)this.sellFromShulker.getValue()) {
            Screen var2 = mc.currentScreen;
            if (var2 instanceof ShulkerBoxScreen) {
               ShulkerBoxScreen shulker = (ShulkerBoxScreen)var2;
               if (mc.player.inventory.isFull()) {
                  mc.player.closeScreen();
                  this.selling = true;
                  return;
               }

               Iterator var9 = ((ShulkerBoxContainer)shulker.getContainer()).inventorySlots.iterator();

               while(var9.hasNext()) {
                  Slot sl = (Slot)var9.next();
                  if (this.shulkerTimer.hasPassed(40.0F)) {
                     if (sl.slotNumber == 35) {
                        mc.player.closeScreen();
                        this.selling = true;
                     }

                     if (mc.player.inventory.isFull()) {
                        mc.player.closeScreen();
                        this.selling = true;
                        break;
                     }

                     if (!sl.getStack().isEmpty() && sl.slotNumber < ((ShulkerBoxContainer)shulker.getContainer()).getInventory().size() - 36) {
                        mc.playerController.windowClick(((ShulkerBoxContainer)shulker.getContainer()).windowId, sl.slotNumber, 0, ClickType.QUICK_MOVE, mc.player);
                        this.shulkerTimer.update();
                     }
                  }
               }

               return;
            }
         }

         if ((Boolean)this.autoSell.getValue() && this.selling) {
            for(int i = 36; i >= 0; --i) {
               if (this.timers[5].hasPassed(400.0F)) {
                  ItemStack stack = mc.player.inventory.getStackInSlot(i);
                  boolean isPhantom = !AutoBuyUtils.getSeller(stack).equals("a");
                  boolean flag = stack.getItem() instanceof BlockItem && ((BlockItem)stack.getItem()).getBlock() instanceof ShulkerBoxBlock;
                  if (flag && !mc.player.inventory.isFull()) {
                     List<ItemStack> stacks = AutoBuyUtils.getShulkerBoxItems(stack);
                     if (!stacks.isEmpty()) {
                        mc.playerController.windowClick(0, i, 1, ClickType.PICKUP, mc.player);
                     }
                  } else {
                     if (i == 0) {
                        this.selling = false;
                     }

                     if (!stack.isEmpty() && !isPhantom) {
                        AutoBuyItem<?> autoBuyItem = Constants.LIST.getItem(stack);
                        if (autoBuyItem != null) {
                           int finalSellPrice;
                           if (!(autoBuyItem instanceof LevelItem)) {
                              finalSellPrice = (int)autoBuyItem.getSellPrice() * stack.getCount();
                           } else {
                              finalSellPrice = ((LevelItem)autoBuyItem).getSellPriceFromLevel(((LevelItem)autoBuyItem).getLevel()).intValue() * stack.getCount();
                           }

                           if ((finalSellPrice <= 10000000 || autoBuyItem.getName().contains("Талисман") || autoBuyItem.getName().contains("Сфера") || autoBuyItem.getName().contains("мист")) && !(autoBuyItem instanceof ShulkerBoxItem)) {
                              if (i < 9) {
                                 mc.player.connection.sendPacket(new CHeldItemChangePacket(i));
                                 mc.player.sendChatMessage("/ah sell " + finalSellPrice);
                                 mc.player.swingArm(Hand.MAIN_HAND);
                                 mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
                              } else {
                                 mc.playerController.pickItem(i);
                                 mc.player.sendChatMessage("/ah sell " + finalSellPrice);
                                 mc.player.swingArm(Hand.MAIN_HAND);
                              }

                              this.timers[5].update();
                              this.timers[6].update();
                           }
                        }
                     }
                  }
               }
            }
         }

         if (!this.selling && (Boolean)this.autoAuc.getValue() && this.timers[6].hasPassed(this.autoAucDelay.getDelay())) {
            this.waiting = false;
            this.skip = false;
            if (!(mc.currentScreen instanceof ChestScreen)) {
               if (Constants.ALPHA) {
                  if (((String)this.search.getValue()).isEmpty()) {
                     mc.player.sendChatMessage("/ah");
                  } else {
                     mc.player.sendChatMessage("/ah search " + (String)this.search.getValue());
                  }
               } else {
                  mc.player.sendChatMessage("/ah");
               }

               this.checked = false;
               this.timers[6].update();
            }
         }

      }
   }

   public List<Delay> getAllDelays() {
      List<Delay> delays = Lists.newArrayList();
      delays.addAll(Arrays.asList(this.update, this.buy, this.autoAucDelay, this.resellDelay, this.autoRCTDelay));
      return delays;
   }

   public Delay getDelay(String name) {
      Iterator var2 = this.getAllDelays().iterator();

      Delay delay;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         delay = (Delay)var2.next();
      } while(!delay.getConfigName().equalsIgnoreCase(name));

      return delay;
   }

   public List<CheckBox> getAllCheckBoxes() {
      List<CheckBox> checkBoxes = Lists.newArrayList();
      checkBoxes.addAll(Arrays.asList(this.autoSell, this.autoAuc, this.resell, this.autoRCT));
      return checkBoxes;
   }

   public CheckBox getCheckbox(String name) {
      Iterator var2 = this.getAllCheckBoxes().iterator();

      CheckBox checkBox;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         checkBox = (CheckBox)var2.next();
      } while(!checkBox.getConfigName().equalsIgnoreCase(name));

      return checkBox;
   }

   public Delay getDelayByName(String name) {
      Iterator var2 = this.getAllDelays().iterator();

      Delay delay;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         delay = (Delay)var2.next();
      } while(!delay.getName().replaceAll(" ", "_").equalsIgnoreCase(name));

      return delay;
   }

   public CheckBox getCheckboxByName(String name) {
      Iterator var2 = this.getAllCheckBoxes().iterator();

      CheckBox checkBox;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         checkBox = (CheckBox)var2.next();
      } while(!checkBox.getName().replaceAll(" ", "_").equalsIgnoreCase(name));

      return checkBox;
   }

   public List<Setting<?, ?>> all() {
      ArrayList<Setting<?, ?>> settings = new ArrayList(Arrays.asList(this.update, this.buy, this.autoSell, this.autoAuc, this.autoAucDelay, this.resell, this.resellDelay, this.autoRCT, this.autoRCTDelay));
      if (Constants.ALPHA) {
         settings.addAll(Arrays.asList(this.search, this.sellFromShulker));
      }

      return settings;
   }

   public CheckBox getAutoSell() {
      return this.autoSell;
   }

   public CheckBox getAutoAuc() {
      return this.autoAuc;
   }

   public CheckBox getResell() {
      return this.resell;
   }

   public CheckBox getAutoRCT() {
      return this.autoRCT;
   }

   public Delay getUpdate() {
      return this.update;
   }

   public Delay getBuy() {
      return this.buy;
   }

   public Delay getAutoAucDelay() {
      return this.autoAucDelay;
   }

   public Delay getResellDelay() {
      return this.resellDelay;
   }

   public Delay getAutoRCTDelay() {
      return this.autoRCTDelay;
   }

   public FieldSetting getSearch() {
      return this.search;
   }

   public CheckBox getSellFromShulker() {
      return this.sellFromShulker;
   }

   public TimerUtil[] getTimers() {
      return this.timers;
   }

   public TimerUtil[] getAutoCostTimers() {
      return this.autoCostTimers;
   }

   public TimerUtil getShulkerTimer() {
      return this.shulkerTimer;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public BigDecimal getBalance() {
      return this.balance;
   }

   public BigDecimal getSellBalance() {
      return this.sellBalance;
   }

   public BigDecimal getBuyBalance() {
      return this.buyBalance;
   }

   public AutoBuy.HistoryItem getLastItem() {
      return this.lastItem;
   }

   public boolean isSellNotification() {
      return this.sellNotification;
   }

   public boolean isBuyNotification() {
      return this.buyNotification;
   }

   public boolean isNotBuyNotification() {
      return this.notBuyNotification;
   }

   public long getLastMs() {
      return this.lastMs;
   }

   public long getPing() {
      return this.ping;
   }

   public boolean isReselling() {
      return this.reselling;
   }

   public boolean isSelling() {
      return this.selling;
   }

   public boolean isWaiting() {
      return this.waiting;
   }

   public boolean isSkip() {
      return this.skip;
   }

   public boolean isAdded() {
      return this.added;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public List<ItemStack> getCosted() {
      return this.costed;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   private static class HistoryItem {
      String name;
      String autoBuyItemName;
      String seller;
      int price;
      int damage;
      String strings;

      public HistoryItem(String name, String autoBuyItemName, String seller, int price, int damage, String strings) {
         this.name = name;
         this.autoBuyItemName = autoBuyItemName;
         this.seller = seller;
         this.price = price;
         this.damage = damage;
         this.strings = strings;
      }
   }
}
