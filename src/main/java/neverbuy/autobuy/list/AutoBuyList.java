package neverbuy.autobuy.list;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.autobuy.list.item.ClientItem;
import neverbuy.autobuy.list.item.CustomItem;
import neverbuy.autobuy.list.item.ShulkerBoxItem;
import neverbuy.autobuy.list.item.level.LevelItem;
import neverbuy.util.misc.AutoBuyUtils;
import neverbuy.util.misc.ChatUtil;

public class AutoBuyList {
   public List<AutoBuyItem<?>> items = new ArrayList();
   BigDecimal price = new BigDecimal(100);
   ClientItem helmetCrush;
   ClientItem chestPlateCrush;
   ClientItem leggingsCrush;
   ClientItem bootsCrush;
   ClientItem swordCrush;
   ClientItem pickaxeCrush;
   ClientItem crossBowCrush;
   ClientItem elytra;
   ClientItem tridentCrush;
   ClientItem tripwireSphere;
   ClientItem trapka;
   ClientItem silver;
   ClientItem bozieKasanie;
   ClientItem megaBur;
   ClientItem ledyanka;
   ClientItem tierBlack;
   ClientItem tierWhite;
   ShulkerBoxItem shulker;
   ClientItem potionKillera;
   ClientItem potionPobeditela;
   ClientItem potionMedic;
   ClientItem potionSernaya;
   ClientItem potionAgent;
   ClientItem potionOtrizhka;
   LevelItem sphereApollona;
   LevelItem sphereTitana;
   LevelItem sphereAndromedi;
   LevelItem sphereAstrea;
   LevelItem sphereHimeri;
   LevelItem spherePandori;
   LevelItem sphereOsiris;
   ClientItem talCrush;
   ClientItem talKaratel;
   LevelItem talGarmonii;
   LevelItem talGrani;
   LevelItem talDedala;
   LevelItem talTritona;
   LevelItem talFenix;
   LevelItem talEchidna;
   ClientItem defaultMyst;
   ClientItem bogatMyst;
   ClientItem legMyst;
   ClientItem sila3;
   ClientItem skorka3;
   ClientItem godAura;

   public AutoBuyList() {
      this.helmetCrush = (new ClientItem()).item(Items.NETHERITE_HELMET).name("Шлем Крушителя").price(this.price).addEnchantment(Enchantments.AQUA_AFFINITY, 1).addEnchantment(Enchantments.RESPIRATION, 3).thorns(false).displayName("helmet_crush").check((item, stack) -> {
         return stack.getItem() == Items.NETHERITE_HELMET && item.containsAllEnchantments(stack);
      });
      this.chestPlateCrush = (new ClientItem()).item(Items.NETHERITE_CHESTPLATE).name("Нагрудник Крушителя").price(this.price).thorns(false).displayName("chestplate_crush").check((item, stack) -> {
         return stack.getItem() == Items.NETHERITE_CHESTPLATE && item.containsAllEnchantments(stack);
      });
      this.leggingsCrush = (new ClientItem()).item(Items.NETHERITE_LEGGINGS).name("Поножи Крушителя").price(this.price).thorns(false).displayName("leggings_crush").check((item, stack) -> {
         return stack.getItem() == Items.NETHERITE_LEGGINGS && item.containsAllEnchantments(stack);
      });
      this.bootsCrush = (new ClientItem()).item(Items.NETHERITE_BOOTS).name("Ботинки Крушителя").price(this.price).addEnchantment(Enchantments.DEPTH_STRIDER, 3).addEnchantment(Enchantments.FEATHER_FALLING, 4).addEnchantment(Enchantments.SOUL_SPEED, 3).thorns(false).displayName("boots_crush").check((item, stack) -> {
         return stack.getItem() == Items.NETHERITE_BOOTS && item.containsAllEnchantments(stack);
      });
      this.swordCrush = (new ClientItem()).item(Items.NETHERITE_SWORD).name("Меч Крушителя").price(this.price).addEnchantment(Enchantments.BANE_OF_ARTHROPODS, 7).addEnchantment(Enchantments.FIRE_ASPECT, 2).addEnchantment(Enchantments.LOOTING, 5).addEnchantment(Enchantments.MENDING, 1).addEnchantment(Enchantments.SHARPNESS, 7).addEnchantment(Enchantments.SMITE, 7).addEnchantment(Enchantments.SWEEPING, 3).addEnchantment(Enchantments.UNBREAKING, 5).addTooltips("Вампиризм II", "Детекция III", "Опытный III", "Яд III", "Окисление II").displayName("sword_crush").check((item, stack) -> {
         return stack.getItem() == Items.NETHERITE_SWORD && item.containsAllTooltip(stack) && item.containsAllEnchantments(stack);
      });
      this.pickaxeCrush = (new ClientItem()).item(Items.NETHERITE_PICKAXE).name("Кирка Крушителя").price(this.price).addEnchantment(Enchantments.EFFICIENCY, 10).addEnchantment(Enchantments.FORTUNE, 5).addEnchantment(Enchantments.MENDING, 1).addEnchantment(Enchantments.UNBREAKING, 5).addTooltips("Магнит", "Пингер", "Паутина", "Бульдозер II", "Авто-Плавка", "Опытный III").displayName("pickaxe_crush").check((item, stack) -> {
         return stack.getItem() == Items.NETHERITE_PICKAXE && item.containsAllEnchantments(stack) && item.containsAllTooltip(stack);
      });
      this.crossBowCrush = (new ClientItem()).item(Items.CROSSBOW).name("Арбалет Крушителя").price(this.price).addEnchantment(Enchantments.MULTISHOT, 1).addEnchantment(Enchantments.PIERCING, 5).addEnchantment(Enchantments.QUICK_CHARGE, 3).addEnchantment(Enchantments.MENDING, 1).addEnchantment(Enchantments.UNBREAKING, 3).displayName("crossbow_crush").check((item, stack) -> {
         return stack.getItem() == Items.CROSSBOW && item.containsAllEnchantments(stack);
      });
      this.elytra = (new ClientItem()).item(Items.ELYTRA).name("Элитры Крушителя").displayName("elytra_crush").price(this.price).addEnchantment(Enchantments.UNBREAKING, 5).addEnchantment(Enchantments.MENDING, 1).check((item, stack) -> {
         return stack.getItem() == Items.ELYTRA && item.containsAllEnchantments(stack);
      });
      this.tridentCrush = (new ClientItem()).price(this.price).name("Трезубец Крушителя").item(Items.TRIDENT).addEnchantment(Enchantments.CHANNELING, 1).addEnchantment(Enchantments.FIRE_ASPECT, 2).addEnchantment(Enchantments.IMPALING, 5).addEnchantment(Enchantments.LOYALTY, 3).addEnchantment(Enchantments.MENDING, 1).addEnchantment(Enchantments.SHARPNESS, 7).addEnchantment(Enchantments.UNBREAKING, 5).addTooltips("Детекция III", "Яд III", "Возвращение", "Вампиризм II", "Опытный III", "Окисление II", "Ступор III", "Притяжение II", "Подрывник", "Скаут III").displayName("tridentCrush").check((item, stack) -> {
         return stack.getItem() == Items.TRIDENT && item.containsAllEnchantments(stack) && item.containsAllTooltip(stack);
      });
      this.tripwireSphere = (new ClientItem()).item(Items.TRIPWIRE_HOOK).price(this.price).name("Отмычка к сферам").addTooltips("Этой отмычкой можно", "Открыть хранилище", "С Сферами").displayName("passkey").check((item, stack) -> {
         return stack.getItem() == Items.TRIPWIRE_HOOK && item.containsAllTooltip(stack);
      });
      this.trapka = (new ClientItem()).item(Items.NETHERITE_SCRAP).price(this.price).name("Трапка").tag("trap").displayName("trapka").check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().contains(item.getTag());
      });
      this.silver = (new ClientItem()).item(Items.IRON_NUGGET).price(this.price).name("Серебро").tag("isSilver").displayName("silver").check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().contains(item.getTag());
      });
      this.bozieKasanie = (new ClientItem()).item(Items.GOLDEN_PICKAXE).price(this.price).name("Божье Касание").tag("custom-enchantments:[{level:1,type:\"spawner\"}]").displayName("bozhie_kasanie").check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().toString().contains(item.getTag());
      });
      this.megaBur = (new ClientItem()).item(Items.NETHERITE_PICKAXE).price(this.price).name("Мега-Бульдозер").displayName("megaBur").addTooltips("Мега-бульдозер").price(this.price).check((item, stack) -> {
         List<ITextComponent> texts = AutoBuyUtils.getTooltip(stack);
         int index = AutoBuyUtils.indexOf(texts, "Мега-Бульдозер");
         return stack.getItem() == Items.NETHERITE_PICKAXE && item.containsAllTooltip(stack) && index != 0;
      });
      this.ledyanka = (new ClientItem()).item(Items.TIPPED_ARROW).stack(this.getPotionWithColor(Items.TIPPED_ARROW.getDefaultInstance(), 65535)).price(this.price).name("Ледяная стрела").addEffect(Effects.SLOWNESS, 50, 100).addEffect(Effects.WEAKNESS, 0, 120).displayName("ledyanka").check((item, stack) -> {
         return stack.getItem() == Items.TIPPED_ARROW && item.containsAllEffects(stack);
      });
      this.tierBlack = (new ClientItem()).item(Items.TNT).tag("tier2").name("Таер Блэк").displayName("tierBlack").price(this.price).check((item, stack) -> {
         return stack.getItem() == Items.TNT && stack.getTag() != null && stack.hasTag() && stack.getTag().contains(item.getTag());
      });
      this.tierWhite = (new ClientItem()).item(Items.TNT).tag("tier1").name("Таер Вайт").displayName("tierWhite").price(this.price).check((item, stack) -> {
         return stack.getItem() == Items.TNT && stack.getTag() != null && stack.hasTag() && stack.getTag().contains(item.getTag());
      });
      this.shulker = (new ShulkerBoxItem()).item(Items.SHULKER_BOX).price(this.price).name("Шалкер").displayName("shulker").check((item, stack) -> {
         return stack.getItem() instanceof BlockItem && ((BlockItem)stack.getItem()).getBlock() instanceof ShulkerBoxBlock;
      });
      this.potionKillera = (new ClientItem()).name("Зелье Киллера").price(this.price).stack(this.getPotionWithColor(Items.SPLASH_POTION.getDefaultInstance(), 13369344)).item(Items.SPLASH_POTION).addEffect(Effects.STRENGTH, 3, 3600).addEffect(Effects.RESISTANCE, 0, 3600).displayName("killerka").check(ClientItem::containsAllEffects);
      this.potionPobeditela = (new ClientItem()).name("Зелье Победителя").price(this.price).stack(this.getPotionWithColor(Items.SPLASH_POTION.getDefaultInstance(), 65280)).item(Items.SPLASH_POTION).addEffect(Effects.HEALTH_BOOST, 1, 3600).addEffect(Effects.RESISTANCE, 0, 1200).addEffect(Effects.INVISIBILITY, 0, 18000).addEffect(Effects.REGENERATION, 1, 1200).displayName("pobedilka").check(ClientItem::containsAllEffects);
      this.potionMedic = (new ClientItem()).name("Зелье Медика").price(this.price).item(Items.SPLASH_POTION).stack(this.getPotionWithColor(Items.SPLASH_POTION.getDefaultInstance(), 16711935)).addEffect(Effects.HEALTH_BOOST, 2, 900).addEffect(Effects.REGENERATION, 2, 900).displayName("medic").check(ClientItem::containsAllEffects);
      this.potionSernaya = (new ClientItem()).name("Серная Кислота").price(this.price).stack(this.getPotionWithColor(Items.SPLASH_POTION.getDefaultInstance(), 10092339)).item(Items.SPLASH_POTION).addEffect(Effects.POISON, 1, 1000).addEffect(Effects.SLOWNESS, 3, 1800).addEffect(Effects.WEAKNESS, 2, 1800).addEffect(Effects.WITHER, 4, 600).displayName("serka").check(ClientItem::containsAllEffects);
      this.potionAgent = (new ClientItem()).name("Зелье Агента").price(this.price).stack(this.getPotionWithColor(Items.SPLASH_POTION.getDefaultInstance(), 16776960)).item(Items.SPLASH_POTION).addEffect(Effects.INVISIBILITY, 1, 18000).addEffect(Effects.FIRE_RESISTANCE, 0, 18000).addEffect(Effects.SPEED, 2, 18000).addEffect(Effects.HASTE, 0, 3500).addEffect(Effects.STRENGTH, 2, 6000).displayName("agent").check(ClientItem::containsAllEffects);
      this.potionOtrizhka = (new ClientItem()).name("Зелье Отрыжки").price(this.price).stack(this.getPotionWithColor(Items.SPLASH_POTION.getDefaultInstance(), 16737792)).item(Items.SPLASH_POTION).addEffect(Effects.BLINDNESS, 0, 200).addEffect(Effects.GLOWING, 0, 3600).addEffect(Effects.HUNGER, 10, 1800).addEffect(Effects.SLOWNESS, 2, 3600).addEffect(Effects.WITHER, 4, 600).displayName("otrizhka").check(ClientItem::containsAllEffects);
      this.sphereApollona = (new LevelItem()).name("Сфера Аполлона").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjQxMTdiNjAxOGZlZjBkNTE1NjcyMTczZTNiMjZlNjYwZDY1MWU1ODc2YmE2ZDAzZTUzNDIyNzBjNDliZWM4MCJ9fX0", new int[]{-583276185, 1226649644, -1204324405, -1669610114})).displayName("sphereApollona").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else if (item.containsAllAttributes(Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.MOVEMENT_SPEED), Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, true)) {
            return 1;
         } else if (item.containsAllAttributes(Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.MOVEMENT_SPEED), Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, true)) {
            return 2;
         } else {
            return item.containsAllAttributes(Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.MOVEMENT_SPEED), Arrays.asList(this.fromValueOperation(4.0D, Operation.ADDITION), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, true) ? 3 : -1;
         }
      });
      this.sphereTitana = (new LevelItem()).name("Сфера Титана").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAzN2JiYmViNjJlMTAyMGRmOWEwNmM0ZWRkNjAzMzBlNzA2MzBkMDkwZjA5NGQ4Nzc2YzJiZDEzNWRlYzIyIn19fQ", new int[]{948851494, 1905995944, -1587568059, -1930302791})).displayName("sphereTitana").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS);
            List<Attribute> attributes1 = Arrays.asList(Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS, Attributes.MOVEMENT_SPEED);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(1.0D, Operation.ADDITION), this.fromValueOperation(1.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (!item.containsAllAttributes(attributes1, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, false) && !item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, false)) {
               return item.containsAllAttributes(attributes1, Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(-0.15D, Operation.MULTIPLY_BASE)), stack, false) ? 3 : -1;
            } else {
               return 2;
            }
         }
      });
      this.sphereAndromedi = (new LevelItem()).name("Сфера Андромеды").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRmZmUzZjM1OGYyMDliYWQ4ZmZmNGRjNDgyNDVkOWJhZjBhMDMxYjNjMWVlNmI3NTg0NjBhMzM5YjE1MTllMiJ9fX0", new int[]{-409043918, -1171898787, -1747900738, 1613574445})).displayName("sphereAndromedi").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.MOVEMENT_SPEED, Attributes.MAX_HEALTH, Attributes.ARMOR);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(1.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(2.5D, Operation.ADDITION), this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(1.5D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(0.15D, Operation.MULTIPLY_BASE), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.sphereAstrea = (new LevelItem()).name("Сфера Астрея").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWE1YWFkZDUyYTVmYWI5NzA4ODE0NTFhZGY1NmZiYjQ5M2EzNTg1NmVhOTZmNTRlMzJlZWE2NjJkNzg3ZWQyMCJ9fX0", new int[]{1973113463, 39597456, -1350933335, 352009817})).displayName("sphereAstrea").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.ATTACK_SPEED, Attributes.MAX_HEALTH);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(1.0D, Operation.ADDITION), this.fromValueOperation(-0.2D, Operation.MULTIPLY_BASE), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(-0.17D, Operation.MULTIPLY_BASE), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(-0.15D, Operation.MULTIPLY_BASE), this.fromValueOperation(4.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.sphereHimeri = (new LevelItem()).name("Сфера Химеры").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWZhYmVlZDQyNGIyNTJhODk0NWE2NDQyYjQ2MmQ1ZjMxNDcwMWE4MTZkYTJkMGE2OWNjZGZjZmQ3NDZlNTg4ZSJ9fX0", new int[]{-259687615, 1038236524, -1643836608, -598286448})).displayName("sphereHimeri").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.MAX_HEALTH);
            List<Attribute> attributes1 = Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.ATTACK_SPEED, Attributes.MAX_HEALTH);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(1.0D, Operation.ADDITION), this.fromValueOperation(-2.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes1, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-2.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes1, Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(0.15D, Operation.MULTIPLY_BASE), this.fromValueOperation(-2.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.spherePandori = (new LevelItem()).name("Сфера Пандора").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU1MWU2NWViNDA1Mjc3MjM4MmM5ZTUwN2E1NGJkZWQ0M2UzOWY3NTViNWRkZjU1YjNmMzk0NDNjZWQ0NjdmNCJ9fX0", new int[]{-1353476096, 948187814, -1596066254, 111158627})).displayName("spherePandori").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ATTACK_DAMAGE, Attributes.MOVEMENT_SPEED, Attributes.ARMOR);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(0.15D, Operation.MULTIPLY_BASE), this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(0.2D, Operation.MULTIPLY_BASE), this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(0.25D, Operation.MULTIPLY_BASE), this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-0.1D, Operation.MULTIPLY_BASE)), stack, true) ? 3 : -1;
            }
         }
      });
      this.sphereOsiris = (new LevelItem()).name("Сфера Осириса").stack(this.getWithSkullOwner("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgxMzYzNWJkODZiMTcxYmJlMTQzYWQ3MWUwOTAyMjkyNjQ5Y2IzYWI4NDQwZWQwMGY4NWNhNmNhMzgyOTkzNiJ9fX0", new int[]{-344931716, -1185402122, -1547537939, -733767741})).displayName("sphereOsiris").lcheck((item, stack) -> {
         if (stack.getItem() != Items.PLAYER_HEAD) {
            return -1;
         } else if (item.containsAllAttributes(Arrays.asList(Attributes.ARMOR, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK), Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(-0.25D, Operation.MULTIPLY_BASE), this.fromValueOperation(0.25D, Operation.MULTIPLY_BASE)), stack, true)) {
            return 1;
         } else if (item.containsAllAttributes(Arrays.asList(Attributes.ARMOR, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK), Arrays.asList(this.fromValueOperation(2.5D, Operation.ADDITION), this.fromValueOperation(-0.2D, Operation.MULTIPLY_BASE), this.fromValueOperation(0.2D, Operation.MULTIPLY_BASE)), stack, true)) {
            return 2;
         } else {
            return item.containsAllAttributes(Arrays.asList(Attributes.ARMOR, Attributes.KNOCKBACK_RESISTANCE, Attributes.ATTACK_KNOCKBACK), Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(-0.15D, Operation.MULTIPLY_BASE), this.fromValueOperation(0.15D, Operation.MULTIPLY_BASE)), stack, true) ? 3 : -1;
         }
      });
      this.talCrush = (new ClientItem()).price(this.price).name("Талисман Крушителя").item(Items.TOTEM_OF_UNDYING).addAttribute(Attributes.ATTACK_DAMAGE, 3.0D, Operation.ADDITION).addAttribute(Attributes.ARMOR_TOUGHNESS, 2.0D, Operation.ADDITION).addAttribute(Attributes.ARMOR, 2.0D, Operation.ADDITION).addAttribute(Attributes.MAX_HEALTH, 4.0D, Operation.ADDITION).displayName("tal_crush").check(ClientItem::containsAllAttributes);
      this.talKaratel = (new ClientItem()).price(this.price).name("Талисман Карателя").item(Items.TOTEM_OF_UNDYING).addAttribute(Attributes.MOVEMENT_SPEED, 0.1D, Operation.MULTIPLY_BASE).addAttribute(Attributes.MAX_HEALTH, -4.0D, Operation.ADDITION).addAttribute(Attributes.ATTACK_DAMAGE, 7.0D, Operation.ADDITION).displayName("tal_karatel").check(ClientItem::containsAllAttributes);
      this.talGarmonii = (new LevelItem()).name("Талисман Гармонии").item(Items.TOTEM_OF_UNDYING).displayName("talGarmonii").lcheck((item, stack) -> {
         if (stack.getItem() != Items.TOTEM_OF_UNDYING) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ARMOR, Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(1.0D, Operation.ADDITION), this.fromValueOperation(1.0D, Operation.ADDITION), this.fromValueOperation(1.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(1.5D, Operation.ADDITION), this.fromValueOperation(1.5D, Operation.ADDITION), this.fromValueOperation(1.5D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.talGrani = (new LevelItem()).name("Талисман Грани").item(Items.TOTEM_OF_UNDYING).displayName("talGrani").lcheck((item, stack) -> {
         if (stack.getItem() != Items.TOTEM_OF_UNDYING) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE);
            List<Attribute> attributes1 = Arrays.asList(Attributes.MOVEMENT_SPEED, Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(1.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes1, Arrays.asList(this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes1, Arrays.asList(this.fromValueOperation(0.15D, Operation.MULTIPLY_BASE), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(3.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.talDedala = (new LevelItem()).name("Талисман Дедала").item(Items.TOTEM_OF_UNDYING).displayName("talDedala").lcheck((item, stack) -> {
         if (stack.getItem() != Items.TOTEM_OF_UNDYING) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(3.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(4.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(5.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.talTritona = (new LevelItem()).name("Талисман Тритона").item(Items.TOTEM_OF_UNDYING).displayName("talTritona").lcheck((item, stack) -> {
         if (stack.getItem() != Items.TOTEM_OF_UNDYING) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS, Attributes.MAX_HEALTH);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(1.0D, Operation.ADDITION), this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(1.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(2.0D, Operation.ADDITION), this.fromValueOperation(-3.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(3.0D, Operation.ADDITION), this.fromValueOperation(-3.0D, Operation.ADDITION), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.talFenix = (new LevelItem()).name("Талисман Феникса").item(Items.TOTEM_OF_UNDYING).displayName("talFenix").lcheck((item, stack) -> {
         if (stack.getItem() != Items.TOTEM_OF_UNDYING) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ATTACK_SPEED, Attributes.MAX_HEALTH);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(2.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(4.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(0.1D, Operation.MULTIPLY_BASE), this.fromValueOperation(6.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.talEchidna = (new LevelItem()).name("Талисман Ехидны").item(Items.TOTEM_OF_UNDYING).displayName("talEchidna").lcheck((item, stack) -> {
         if (stack.getItem() != Items.TOTEM_OF_UNDYING) {
            return -1;
         } else {
            List<Attribute> attributes = Arrays.asList(Attributes.ARMOR, Attributes.ARMOR_TOUGHNESS, Attributes.MAX_HEALTH, Attributes.ATTACK_DAMAGE);
            if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(4.0D, Operation.ADDITION)), stack, true)) {
               return 1;
            } else if (item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(5.0D, Operation.ADDITION)), stack, true)) {
               return 2;
            } else {
               return item.containsAllAttributes(attributes, Arrays.asList(this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(-2.0D, Operation.ADDITION), this.fromValueOperation(-4.0D, Operation.ADDITION), this.fromValueOperation(6.0D, Operation.ADDITION)), stack, true) ? 3 : -1;
            }
         }
      });
      this.defaultMyst = (new ClientItem()).price(this.price).name("Обычный мист").item(Items.CAMPFIRE).tag("loots:summon:mystic/loot1").displayName("default_myst").check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().toString().contains(item.getTag());
      });
      this.bogatMyst = (new ClientItem()).price(this.price).name("Богатый мист").item(Items.CAMPFIRE).tag("loots:summon:mystic/loot2").displayName("bogat_myst").check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().toString().contains(item.getTag());
      });
      this.legMyst = (new ClientItem()).price(this.price).name("Легендарный мист").item(Items.CAMPFIRE).tag("loots:summon:mystic/loot3").displayName("leg_myst").check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().toString().contains(item.getTag());
      });
      this.sila3 = (new ClientItem()).price(this.price).name("Сила 3").item(Items.POTION).displayName("silka3").addEffect(Effects.STRENGTH, 2, 3600).check(ClientItem::containsAllEffects);
      this.skorka3 = (new ClientItem()).price(this.price).name("Скорость 3").item(Items.POTION).displayName("skorka3").addEffect(Effects.SPEED, 2, 3600).check(ClientItem::containsAllEffects);
      this.godAura = (new ClientItem()).price(this.price).name("Божья аура").displayName("godAura").tag("godsaura").item(Items.PHANTOM_MEMBRANE).check((item, stack) -> {
         return stack.getTag() != null && stack.hasTag() && stack.getTag().contains(item.getTag());
      });
      this.reload();
   }

   private void addChecksValidArmor(ClientItem item) {
      item.addEnchantment(Enchantments.UNBREAKING, 5).addEnchantment(Enchantments.PROTECTION, 5).addEnchantment(Enchantments.PROJECTILE_PROTECTION, 5).addEnchantment(Enchantments.BLAST_PROTECTION, 5).addEnchantment(Enchantments.MENDING, 1).addEnchantment(Enchantments.FIRE_PROTECTION, 5);
   }

   private ItemStack getWithSkullOwner(String owner, int[] id) {
      ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
      CompoundNBT main = new CompoundNBT();
      CompoundNBT skullOwner = new CompoundNBT();
      CompoundNBT properties = new CompoundNBT();
      ListNBT textures = new ListNBT();
      CompoundNBT texture = new CompoundNBT();
      texture.putString("Value", owner);
      textures.add(texture);
      properties.put("textures", textures);
      skullOwner.put("Properties", properties);
      skullOwner.putIntArray("Id", id);
      main.put("SkullOwner", skullOwner);
      stack.setTag(main);
      return stack;
   }

   private ItemStack getPotionWithColor(ItemStack stack, int value) {
      CompoundNBT nbt = new CompoundNBT();
      nbt.putInt("CustomPotionColor", value);
      stack.setTag(nbt);
      return stack;
   }

   public ClientItem getItem(String item) {
      Iterator var2 = this.items.iterator();

      AutoBuyItem autoBuyItem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         autoBuyItem = (AutoBuyItem)var2.next();
      } while(!autoBuyItem.getName().contains(item));

      return (ClientItem)autoBuyItem;
   }

   public AutoBuyItem<?> getItemS(String item) {
      Iterator var2 = this.items.iterator();

      AutoBuyItem autoBuyItem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         autoBuyItem = (AutoBuyItem)var2.next();
      } while(!autoBuyItem.getName().contains(item));

      return autoBuyItem;
   }

   private AttributeModifier fromValueOperation(double value, Operation operation) {
      return new AttributeModifier("aa", value, operation);
   }

   public ClientItem getAsDisplay(String displayName) {
      Iterator var2 = this.items.iterator();

      while(var2.hasNext()) {
         AutoBuyItem<?> autoBuyItem = (AutoBuyItem)var2.next();
         if (autoBuyItem instanceof ClientItem) {
            ClientItem clientItem = (ClientItem)autoBuyItem;
            if (clientItem.getConfigName().equalsIgnoreCase(displayName)) {
               return clientItem;
            }
         }
      }

      return null;
   }

   public AutoBuyItem<?> getItem(ItemStack stack) {
      assert stack.getTag() != null;

      Iterator var3 = this.items.iterator();

      while(var3.hasNext()) {
         AutoBuyItem<?> item = (AutoBuyItem)var3.next();
         if (item instanceof ClientItem) {
            if (!(item instanceof LevelItem)) {
               ClientItem it = (ClientItem)item;
               if (it.getCheck() == null) {
                  ChatUtil.addMessage("Null Check: " + it.getName());
                  break;
               }

               if (it.getCheck().check(it, stack)) {
                  return it;
               }
            } else {
               LevelItem levelItem = (LevelItem)item;
               if (levelItem.getLevelCheck() == null) {
                  ChatUtil.addMessage("Level Item Null Check " + levelItem.getName());
                  break;
               }

               if (levelItem.getLevelCheck().check(levelItem, stack) != -1) {
                  return levelItem.withLevel(levelItem.getLevelCheck().check(levelItem, stack));
               }
            }
         }
      }

      return stack.getDamage() != 0 ? null : this.getItem(stack.getItem());
   }

   public void reload() {
      this.items.clear();
      this.addChecksValidArmor(this.helmetCrush);
      this.addChecksValidArmor(this.chestPlateCrush);
      this.addChecksValidArmor(this.leggingsCrush);
      this.addChecksValidArmor(this.bootsCrush);
      this.items.addAll(Arrays.asList(this.helmetCrush, this.chestPlateCrush, this.leggingsCrush, this.bootsCrush, this.swordCrush, this.pickaxeCrush, this.crossBowCrush, this.elytra, this.tridentCrush, this.potionKillera, this.potionPobeditela, this.potionMedic, this.potionSernaya, this.potionAgent, this.potionOtrizhka, this.sphereApollona, this.sphereTitana, this.sphereAndromedi, this.sphereAstrea, this.sphereHimeri, this.spherePandori, this.sphereOsiris, this.talCrush, this.talKaratel, this.talGarmonii, this.talGrani, this.talDedala, this.talTritona, this.talFenix, this.talEchidna, this.tripwireSphere, this.trapka, this.silver, this.bozieKasanie, this.megaBur, this.ledyanka, this.tierWhite, this.tierBlack, this.shulker, this.defaultMyst, this.bogatMyst, this.legMyst, this.sila3, this.skorka3, this.godAura));
   }

   public boolean contains(Item item) {
      Iterator var2 = this.items.iterator();

      AutoBuyItem autoBuyItem;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         autoBuyItem = (AutoBuyItem)var2.next();
      } while(!(autoBuyItem instanceof CustomItem) || autoBuyItem.getItem() != item);

      return true;
   }

   public boolean contains(String name, float price, Item item, float sellPrice) {
      Iterator var5 = this.items.iterator();

      AutoBuyItem autoBuyItem;
      do {
         if (!var5.hasNext()) {
            return false;
         }

         autoBuyItem = (AutoBuyItem)var5.next();
      } while(!(autoBuyItem instanceof CustomItem) || !autoBuyItem.getName().equals(name) || autoBuyItem.getBuyPrice() != price || autoBuyItem.getItem() != item || sellPrice != autoBuyItem.getSellPrice());

      return true;
   }

   public AutoBuyItem<?> getAnyItem(String name) {
      Iterator var2 = this.items.iterator();

      AutoBuyItem item;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         item = (AutoBuyItem)var2.next();
      } while(!item.getName().replaceAll(" ", "_").equalsIgnoreCase(name));

      return item;
   }

   public CustomItem getItem(Item item) {
      Iterator var2 = this.items.iterator();

      AutoBuyItem autoBuyItem;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         autoBuyItem = (AutoBuyItem)var2.next();
      } while(!(autoBuyItem instanceof CustomItem) || autoBuyItem.getItem() != item);

      return (CustomItem)autoBuyItem;
   }
}
