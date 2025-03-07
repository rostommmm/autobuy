package neverbuy.gui.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.autobuy.list.item.level.LevelItem;
import neverbuy.autobuy.list.item.setting.BDSetting;
import neverbuy.autobuy.list.item.setting.BoolSetting;
import neverbuy.autobuy.list.item.setting.ItemSetting;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.gui.component.action.Action;
import neverbuy.gui.component.action.ActionButton;
import neverbuy.gui.component.itemsetting.BDSettingComponent;
import neverbuy.gui.component.itemsetting.BoolSettingComponent;
import neverbuy.gui.component.itemsetting.ItemSettingComponent;
import neverbuy.util.animation.Animation;
import neverbuy.util.animation.util.Easings;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;
import neverbuy.util.render.Scissor;
import neverbuy.util.textfield.FieldFunction;
import neverbuy.util.textfield.TextField;

public class ItemBoard {
   AutoBuyItem<?> autoBuyItem;
   public Animation animation = new Animation();
   public float posX;
   public float posY;
   public float width;
   public float height;
   float dragX;
   float dragY;
   public boolean opened;
   public boolean dragging;
   public boolean renderItemField;
   TextField nameField;
   TextField itemField;
   List<ItemSettingComponent> components = new ArrayList();
   private int level = -1;
   private ActionButton leftSlider;
   private ActionButton rightSlider;
   private ActionButton crossAction;

   public ItemBoard(final AutoBuyItem<?> autoBuyItem, float[] coordinates) {
      this.autoBuyItem = autoBuyItem;
      this.posX = coordinates[0];
      this.posY = coordinates[1];
      this.width = coordinates[2];
      this.height = coordinates[3];
      AutoBuyItem var5 = this.autoBuyItem;
      String itemName;
      final ResourceLocation crossIcon;
       ResourceLocation crossIcon1;
       if (var5 instanceof LevelItem) {
         LevelItem levelItem = (LevelItem)var5;
         this.level = 1;
         itemName = levelItem.getName(this.level);
         crossIcon1 = new ResourceLocation("minecraft", "neverbuy/slider/slider-left.png");
         final ResourceLocation rightSliderRes = new ResourceLocation("minecraft", "neverbuy/slider/slider-right.png");
          ResourceLocation finalCrossIcon = crossIcon1;
          this.leftSlider = new ActionButton(new float[]{this.posX + 4.0F, this.posY + 100.0F, 15.0F, 15.0F}, new Action() {
            public void render(ActionButton button, MatrixStack matrixStack, int mouseX, int mouseY) {
               RoundedUtils.drawRound(button.posX, button.posY, button.width, button.height, 4.0F, new Color(1840933));
               RenderUtils.drawImage(matrixStack, finalCrossIcon, (int)button.posX, (int)button.posY, (int)button.width, (int)button.height, 10, 10);
            }

            public void mouseClicked(double mouseX, double mouseY, int button) {
               int newLevel = ItemBoard.this.level <= 1 ? 1 : ItemBoard.this.level - 1;
               if (newLevel != ItemBoard.this.level) {
                  ItemBoard.this.level = newLevel;
                  ItemBoard.this.updateLevelItems();
                  String a = ((LevelItem)autoBuyItem).getName(ItemBoard.this.level);
                  ItemBoard.this.nameField.value = a;
                  ItemBoard.this.nameField.title = a;
               }
            }
         });
         this.rightSlider = new ActionButton(new float[]{this.posX + this.width - 20.0F, this.posY + 100.0F, 15.0F, 15.0F}, new Action() {
            public void render(ActionButton button, MatrixStack matrixStack, int mouseX, int mouseY) {
               RoundedUtils.drawRound(button.posX, button.posY, button.width, button.height, 4.0F, new Color(1840933));
               RenderUtils.drawImage(matrixStack, rightSliderRes, (int)button.posX, (int)button.posY, (int)button.width, (int)button.height, 10, 10);
            }

            public void mouseClicked(double mouseX, double mouseY, int button) {
               int newLevel = ItemBoard.this.level >= 3 ? 3 : ItemBoard.this.level + 1;
               if (newLevel != ItemBoard.this.level) {
                  ItemBoard.this.level = newLevel;
                  ItemBoard.this.updateLevelItems();
                  String a = ((LevelItem)autoBuyItem).getName(ItemBoard.this.level);
                  ItemBoard.this.nameField.value = a;
                  ItemBoard.this.nameField.title = a;
               }
            }
         });
      } else {
         itemName = this.autoBuyItem.getName();
      }

      String var10000;
      if (this.autoBuyItem.isConstant) {
         AutoBuyItem var13 = this.autoBuyItem;
         if (var13 instanceof LevelItem) {
            LevelItem levelItem = (LevelItem)var13;
            var10000 = levelItem.getName(this.level);
         } else {
            var10000 = this.autoBuyItem.getName();
         }
      } else {
         var10000 = "Имя предмета";
      }

      String title = var10000;
      this.nameField = new TextField(title, Fonts.Inter.get(12.0F), this.posX + 28.0F, this.posY + 23.0F, (float)(Fonts.Inter.get(13.0F).getWidth(title) + 3), 12.0F, (full) -> {
         this.autoBuyItem.name(full);
         this.nameField.value = this.autoBuyItem.getName();
      });
      this.nameField.value = itemName;
      this.nameField.setStartX(this.nameField.posX);
      crossIcon1 = new ResourceLocation("minecraft", "neverbuy/cross.png");
       crossIcon = crossIcon1;
       this.crossAction = new ActionButton(new float[]{this.posX + this.width - 15.0F, this.posY + 5.0F, 10.0F, 10.0F}, new Action() {
         public void render(ActionButton button, MatrixStack matrixStack, int mouseX, int mouseY) {
            RenderUtils.drawImage(matrixStack, crossIcon, (int)button.posX, (int)button.posY, (int)button.width, (int)button.height, 10, 10);
         }

         public void mouseClicked(double mouseX, double mouseY, int button) {
            ItemBoard.this.close();
         }
      });
      if (!this.autoBuyItem.isConstant) {
         this.itemField = new TextField("ID предмета", Fonts.Inter.get(13.0F), this.posX, this.posY - 20.0F, this.width, 15.0F, new FieldFunction() {
            public void onChar(String full) {
            }

            public void onEnter(String full) {
               Item item = (Item)Registry.ITEM.getOrDefault(new ResourceLocation(full.toLowerCase()));
               autoBuyItem.item(item);
            }
         });
         this.itemField.setStartX(this.itemField.posX);
      }

      float offset = 42.5F;
      ItemSetting setting;
      if (!(this.autoBuyItem instanceof LevelItem)) {
         for(Iterator var7 = this.autoBuyItem.getSettings().iterator(); var7.hasNext(); offset += (float)setting.getOffset() + 1.0F) {
            setting = (ItemSetting)var7.next();
            if (setting instanceof BoolSetting) {
               BoolSettingComponent component = new BoolSettingComponent(new float[]{this.posX + 2.0F, this.posY + 2.0F, this.width - 4.0F, 15.0F, offset}, setting.getAsBool());
               this.components.add(component);
            } else if (setting instanceof BDSetting) {
               BDSettingComponent component = new BDSettingComponent(new float[]{this.posX + 2.0F, this.posY + 2.0F, this.width - 4.0F, 15.0F, offset}, setting.getAsBigDecimal());
               this.components.add(component);
            }
         }
      } else {
         this.updateLevelItems();
      }

   }

   private void updateLevelItems() {
      this.components.clear();
      LevelItem levelItem = (LevelItem)this.autoBuyItem;
      float offset = 42.5F;

      ItemSetting setting;
      for(Iterator var3 = levelItem.getSettingsFromLevel(this.level).iterator(); var3.hasNext(); offset += (float)setting.getOffset() + 1.0F) {
         setting = (ItemSetting)var3.next();
         if (setting instanceof BoolSetting) {
            BoolSettingComponent component = new BoolSettingComponent(new float[]{this.posX + 2.0F, this.posY + 2.0F, this.width - 4.0F, 15.0F, offset}, setting.getAsBool());
            this.components.add(component);
         } else if (setting instanceof BDSetting) {
            BDSettingComponent component = new BDSettingComponent(new float[]{this.posX + 2.0F, this.posY + 2.0F, this.width - 4.0F, 15.0F, offset}, setting.getAsBigDecimal());
            this.components.add(component);
         }
      }

   }

   public void open() {
      this.animation.animate(1.0D, 0.3499999940395355D, Easings.BACK_OUT);
      this.opened = true;
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
      Minecraft mc = Minecraft.getInstance();
      MainWindow sr = mc.getMainWindow();
      if (this.dragging) {
         this.posX = (float)((int)Math.max(0.0F, Math.min((float)mouseX - this.dragX, (float)sr.getScaledWidth() - this.width)));
         this.posY = (float)((int)Math.max(0.0F, Math.min((float)mouseY - this.dragY, (float)sr.getScaledHeight() - this.height)));
      }

      if (this.renderItemField) {
         this.itemField.render(matrixStack);
      }

      RenderSystem.pushMatrix();
      RenderSystem.translatef(this.posX + this.width / 2.0F, this.posY + this.height / 2.0F, 0.0F);
      RenderSystem.scaled(this.animation.getValue(), this.animation.getValue(), this.animation.getValue());
      RenderSystem.translatef(-(this.posX + this.width / 2.0F), -(this.posY + this.height / 2.0F), 0.0F);
      double finalHeight = 0.0D;
      RoundedUtils.drawRound(this.posX, this.posY, this.width, this.height, 8.0F, new Color(1775904));
      RenderUtils.drawOneSideRound(matrixStack, this.posX, this.posY, this.width, 17.0F, 8.0F, new Color(1840933));
      finalHeight += 20.0D;
      Font f = Fonts.mntsb.get(16.0F);
      String text = "Настройка предмета";
      f.draw(text, this.posX + 4.0F, this.posY + 3.5F, -1);
      ItemStack stack = this.autoBuyItem.isContainsStack() ? this.autoBuyItem.getStack() : this.autoBuyItem.getItem().getDefaultInstance();
      Scissor.push();
      Scissor.setFromComponentCoordinates((double)this.posX, (double)this.posY, (double)this.width, (double)this.height, this.animation.getValue().floatValue());
      this.nameField.render(matrixStack);
      Scissor.pop();
      RoundedUtils.drawRound(this.posX + 4.0F, this.posY + 20.0F, 18.0F, 18.0F, 4.0F, new Color(2103849));
      RenderUtils.renderItem(stack, (int)(this.posX + 5.0F), (int)(this.posY + 21.0F));
      Iterator var11 = this.components.iterator();

      while(var11.hasNext()) {
         ItemSettingComponent component = (ItemSettingComponent)var11.next();
         component.render(matrixStack, mouseX, mouseY);
         finalHeight += component.apply() + 1.0D;
         component.updatePos(this);
      }

      finalHeight += 20.0D;
      if (this.autoBuyItem instanceof LevelItem) {
         this.leftSlider.render(matrixStack, mouseX, mouseY);
         this.rightSlider.render(matrixStack, mouseX, mouseY);
         finalHeight += 20.0D;
      }

      this.crossAction.render(matrixStack, mouseX, mouseY);
      RenderSystem.popMatrix();
      this.nameField.posX = this.posX + (this.width - (float)this.nameField.f.getWidth(this.nameField.title)) / 2.0F;
      this.nameField.posY = this.posY + 23.0F;
      finalHeight += 5.0D;
      if (this.autoBuyItem instanceof LevelItem) {
         this.leftSlider.posX = this.posX + 6.0F;
         this.leftSlider.posY = (float)((double)this.posY + finalHeight - 20.0D);
         this.rightSlider.posX = this.posX + this.width - 20.0F;
         this.rightSlider.posY = (float)((double)this.posY + finalHeight - 20.0D);
      }

      this.crossAction.posX = this.posX + this.width - 15.0F;
      this.crossAction.posY = this.posY + 4.0F;
      if (this.itemField != null) {
         this.itemField.posX = this.posX;
         this.itemField.posY = this.posY - 20.0F;
      }

      this.height = RenderUtils.lerp(this.height, (float)finalHeight, 8.0F);
      this.updateItems();
   }

   public void close() {
      if (this.opened) {
         this.animation.animate(-1.0D, 0.3499999940395355D, Easings.BACK_IN);
         this.opened = false;
      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.isHoveredOnTitle(mouseX, mouseY)) {
         this.dragging = true;
         this.dragX = (float)(mouseX - (double)this.posX - 1.0D);
         this.dragY = (float)(mouseY - (double)this.posY - 1.0D);
      }

      if (!this.autoBuyItem.isConstant()) {
         this.nameField.mouseClicked(mouseX, mouseY, button);
      }

      Iterator var6 = this.components.iterator();

      while(var6.hasNext()) {
         ItemSettingComponent component = (ItemSettingComponent)var6.next();
         component.mouseClicked(mouseX, mouseY, button);
      }

      if (this.leftSlider != null) {
         this.leftSlider.mouseClicked(mouseX, mouseY, button);
         this.rightSlider.mouseClicked(mouseX, mouseY, button);
      }

      this.crossAction.mouseClicked(mouseX, mouseY, button);
      RoundedUtils.drawRound(this.posX + 4.0F, this.posY + 20.0F, 18.0F, 18.0F, 4.0F, new Color(2103849));
      if (mouseX >= (double)(this.posX + 4.0F) && mouseX <= (double)(this.posX + 22.0F) && mouseY >= (double)(this.posY + 20.0F) && mouseY <= (double)(this.posY + 38.0F) && !this.autoBuyItem.isConstant) {
         this.renderItemField = !this.renderItemField;
      }

      if (this.renderItemField) {
         this.itemField.mouseClicked(mouseX, mouseY, button);
      }

      return this.isHovered(mouseX, mouseY);
   }

   public void charTyped(char codePoint) {
      this.nameField.charTyped(codePoint);
      if (this.renderItemField) {
         this.itemField.charTyped(codePoint);
      }

      Iterator var2 = this.components.iterator();

      while(var2.hasNext()) {
         ItemSettingComponent component = (ItemSettingComponent)var2.next();
         if (component instanceof BDSettingComponent) {
            BDSettingComponent bd = (BDSettingComponent)component;
            bd.charTyped(codePoint);
         }
      }

   }

   public void keyPressed(int keyCode) {
      this.nameField.keyPressed(keyCode);
      if (this.renderItemField) {
         this.itemField.keyPressed(keyCode);
      }

      Iterator var2 = this.components.iterator();

      while(var2.hasNext()) {
         ItemSettingComponent component = (ItemSettingComponent)var2.next();
         if (component instanceof BDSettingComponent) {
            BDSettingComponent bd = (BDSettingComponent)component;
            bd.keyPressed(keyCode);
         }
      }

   }

   public void mouseReleased() {
      this.dragging = false;
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return this.opened && mouseX >= (double)this.posX && mouseY >= (double)this.posY && mouseY <= (double)(this.posY + this.height) && mouseX <= (double)(this.posX + this.width);
   }

   public boolean isHoveredOnTitle(double mouseX, double mouseY) {
      return this.opened && mouseX >= (double)this.posX && mouseY >= (double)this.posY && mouseY <= (double)(this.posY + 17.0F) && mouseX <= (double)(this.posX + this.width);
   }

   private void updateItems() {
      float offset = 42.5F;

      ItemSettingComponent component;
      for(Iterator var2 = this.components.iterator(); var2.hasNext(); offset += (float)component.apply() + 1.0F) {
         component = (ItemSettingComponent)var2.next();
         component.offset = offset;
      }

   }
}
