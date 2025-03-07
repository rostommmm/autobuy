package neverbuy.autobuy.list.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import neverbuy.autobuy.list.item.setting.ItemSetting;
import neverbuy.util.misc.AutoBuyUtils;

public class ClientItem extends AutoBuyItem<ClientItem> {
   private final List<String> containsTooltip = new ArrayList();
   private final List<String> notContainsTooltip = new ArrayList();
   private final Map<Enchantment, Integer> containsEnchantments = new HashMap();
   private final Map<Enchantment, Integer> notContainsEnchantments = new HashMap();
   private final List<EffectInstance> effects = new ArrayList();
   private String tag;
   protected String configName;
   protected Check check;
   private final Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
   private boolean thorns = false;

   public ClientItem name(String name) {
      this.name = name;
      return this;
   }

   public ClientItem check(Check check) {
      this.check = check;
      return this;
   }

   public ClientItem displayName(String name) {
      this.configName = name;
      return this;
   }

   public ClientItem item(Item item) {
      this.item = item;
      return this;
   }

   public ClientItem price(BigDecimal price) {
      this.getSettingByName("Цена покупки").getAsBigDecimal().setVal(price);
      return this;
   }

   public ClientItem tag(String tag) {
      this.tag = tag;
      return this;
   }

   private void addTooltip(String component) {
      this.containsTooltip.add(component);
   }

   public ClientItem addTooltips(String... ss) {
      String[] var2 = ss;
      int var3 = ss.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String s = var2[var4];
         this.addTooltip(s);
      }

      return this;
   }

   public ClientItem addEnchantment(Enchantment enc, int amp) {
      if (this.containsEnchantments.containsKey(enc)) {
         return this;
      } else {
         this.containsEnchantments.put(enc, amp);
         return this;
      }
   }

   public ClientItem stack(ItemStack stack) {
      this.stack = stack;
      this.containsStack = true;
      return this;
   }

   public ClientItem addEffect(Effect effect, int amp, int duration) {
      this.effects.add(new EffectInstance(effect, duration, amp));
      return this;
   }

   public void configure(JsonObject jsonObject) {
      JsonObject object = new JsonObject();
      Iterator var3 = this.getSettings().iterator();

      while(var3.hasNext()) {
         ItemSetting<?> setting = (ItemSetting)var3.next();
         setting.configure(object);
      }

      object.addProperty("client", true);
      if (this.isArmor()) {
         object.addProperty("thorns", this.isThorns());
      }

      if (this.configName == null) {
         System.out.println(this.name);
      } else {
         jsonObject.add(this.configName, object);
      }
   }

   public void read(JsonObject jsonObject) {
      if (!jsonObject.has("item")) {
         Iterator var2 = this.getSettings().iterator();

         while(var2.hasNext()) {
            ItemSetting<?> setting = (ItemSetting)var2.next();
            setting.read(jsonObject);
         }

         if (this.isArmor()) {
            this.thorns = jsonObject.get("thorns").getAsBoolean();
         }

      }
   }

   public ClientItem addAttribute(Attribute attribute, double value, Operation operation) {
      this.attributes.put(attribute, new AttributeModifier(attribute.getAttributeName(), value, operation));
      return this;
   }

   public ClientItem thorns(boolean thorns) {
      this.thorns = thorns;
      return this;
   }

   public void buying(boolean buy) {
      this.getSettingByName("Покупать").getAsBool().setVal(buy);
   }

   public void selling(boolean selling) {
      this.getSettingByName("Продавать").getAsBool().setVal(selling);
   }

   public ClientItem sell(BigDecimal price) {
      this.getSettingByName("Цена продажи").getAsBigDecimal().setVal(price);
      return this;
   }

   private boolean containsAttribute(Attribute attribute, double value, Operation operation) {
      List<AttributeModifier> matchingModifiers = new ArrayList();
      Iterator var6 = this.attributes.keySet().iterator();

      while(true) {
         Attribute attribute1;
         do {
            if (!var6.hasNext()) {
               return !matchingModifiers.isEmpty();
            }

            attribute1 = (Attribute)var6.next();
         } while(attribute1 != attribute);

         Iterator var8 = this.attributes.get(attribute).iterator();

         while(var8.hasNext()) {
            AttributeModifier modifier = (AttributeModifier)var8.next();
            if (modifier.getOperation() == operation && modifier.getAmount() == value) {
               matchingModifiers.add(modifier);
            }
         }
      }
   }

   public boolean containsAllAttributes(ItemStack stack) {
      Multimap<Attribute, AttributeModifier> attributes = stack.getAttributeModifiers(EquipmentSlotType.OFFHAND);
      if (attributes.size() != this.attributes.size()) {
         return false;
      } else {
         Iterator var3 = attributes.entries().iterator();

         Attribute key;
         AttributeModifier value;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            Entry<Attribute, AttributeModifier> entry = (Entry)var3.next();
            key = (Attribute)entry.getKey();
            value = (AttributeModifier)entry.getValue();
         } while(this.containsAttribute(key, value.getAmount(), value.getOperation()));

         return false;
      }
   }

   public boolean containsAllEffects(ItemStack stack) {
      List<EffectInstance> itemEffects = PotionUtils.getFullEffectsFromItem(stack);
      if (this.effects.size() != itemEffects.size()) {
         return false;
      } else {
         Iterator var3 = this.effects.iterator();

         EffectInstance effect;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            effect = (EffectInstance)var3.next();
         } while(itemEffects.contains(effect));

         return false;
      }
   }

   public boolean containsAllEnchantments(ItemStack stack) {
      Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
      if (!this.isThorns() && enchantments.size() != this.containsEnchantments.size()) {
         return false;
      } else {
         Iterator var3 = this.containsEnchantments.keySet().iterator();

         Enchantment ench;
         int value;
         do {
            do {
               if (!var3.hasNext()) {
                  return true;
               }

               ench = (Enchantment)var3.next();
            } while(ench == Enchantments.THORNS && this.isThorns());

            value = (Integer)this.containsEnchantments.get(ench);
         } while(enchantments.containsKey(ench) && (Integer)enchantments.get(ench) == value);

         return false;
      }
   }

   public boolean containsAllTooltip(ItemStack stack) {
      List<ITextComponent> components = AutoBuyUtils.getTooltip(stack);
      Iterator var3 = this.containsTooltip.iterator();

      String s;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         s = (String)var3.next();
      } while(this.contains(components, s));

      return false;
   }

   private boolean contains(List<ITextComponent> list, String found) {
      Iterator var3 = list.iterator();

      ITextComponent t;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         t = (ITextComponent)var3.next();
      } while(!t.getString().contains(found));

      return true;
   }

   public void update() {
      if (this.isThorns()) {
         this.containsEnchantments.put(Enchantments.THORNS, 3);
      } else {
         this.containsEnchantments.remove(Enchantments.THORNS);
      }

   }

   public List<String> getContainsTooltip() {
      return this.containsTooltip;
   }

   public List<String> getNotContainsTooltip() {
      return this.notContainsTooltip;
   }

   public Map<Enchantment, Integer> getContainsEnchantments() {
      return this.containsEnchantments;
   }

   public Map<Enchantment, Integer> getNotContainsEnchantments() {
      return this.notContainsEnchantments;
   }

   public List<EffectInstance> getEffects() {
      return this.effects;
   }

   public String getTag() {
      return this.tag;
   }

   public String getConfigName() {
      return this.configName;
   }

   public Check getCheck() {
      return this.check;
   }

   public Multimap<Attribute, AttributeModifier> getAttributes() {
      return this.attributes;
   }

   public boolean isThorns() {
      return this.thorns;
   }
}
