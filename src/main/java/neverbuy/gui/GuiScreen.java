package neverbuy.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.NonNull;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import neverbuy.Constants;
import neverbuy.autobuy.api.CheckBox;
import neverbuy.autobuy.api.Delay;
import neverbuy.autobuy.api.FieldSetting;
import neverbuy.autobuy.api.Setting;
import neverbuy.autobuy.list.item.AutoBuyItem;
import neverbuy.autobuy.list.item.CustomItem;
import neverbuy.font.Font;
import neverbuy.font.Fonts;
import neverbuy.gui.component.Component;
import neverbuy.gui.component.ItemBoard;
import neverbuy.gui.component.ItemComponent;
import neverbuy.gui.component.SettingComponent;
import neverbuy.gui.component.action.Action;
import neverbuy.gui.component.action.ActionButton;
import neverbuy.gui.component.setting.CheckBoxComponent;
import neverbuy.gui.component.setting.DelayComponent;
import neverbuy.gui.component.setting.FieldSettingComponent;
import neverbuy.util.render.BlurUtil;
import neverbuy.util.render.ColorHelper;
import neverbuy.util.render.RenderUtils;
import neverbuy.util.render.RoundedUtils;
import neverbuy.util.render.Scissor;

public class GuiScreen extends Screen {
   public Category currentCategory;
   public float posX;
   public float posY;
   public float width;
   public float height;
   float scroll;
   float scrollAnimation;
   float maxAlpha;
   Minecraft mc;
   CopyOnWriteArrayList<ItemComponent> itemComponents;
   CopyOnWriteArrayList<SettingComponent> settingComponents;
   public CopyOnWriteArrayList<ItemBoard> boards;
   CopyOnWriteArrayList<GuiScreen.Circle> circles;
   ActionButton actionButton;
   ActionButton addButton;
   ActionButton telegramButton;

   public GuiScreen() {
      super(ITextComponent.getTextComponentOrEmpty("NeverBuy"));
      this.currentCategory = Category.NONE;
      this.mc = Minecraft.getInstance();
      MainWindow window = this.mc.getMainWindow();
      this.width = 250.0F;
      this.height = 150.0F;
      this.posX = ((float)window.getScaledWidth() - this.width) / 2.0F;
      this.posY = ((float)window.getScaledHeight() - this.height) / 2.0F;
      this.itemComponents = new CopyOnWriteArrayList();
      this.settingComponents = new CopyOnWriteArrayList();
      this.boards = new CopyOnWriteArrayList();
      this.circles = new CopyOnWriteArrayList();
      Component.guiScreen = this;
      this.actionButton = new ActionButton(new float[]{this.posX + this.width / 2.0F - 50.0F, this.posY - 22.0F, 16.0F, 16.0F}, new Action() {
         public void render(ActionButton button, MatrixStack matrixStack, int mouseX, int mouseY) {
            RoundedUtils.drawRound(button.posX, button.posY, button.width, button.height, 4.0F, new Color(1117717));
            ResourceLocation resourceLocation = new ResourceLocation("minecraft", "neverbuy/options.png");
            RenderUtils.drawImage(matrixStack, resourceLocation, (int)(button.posX + 2.0F), (int)(button.posY + 2.0F), (int)(button.width - 4.0F), (int)(button.height - 4.0F), 10, 10);
         }

         public void mouseClicked(double mouseX, double mouseY, int button) {
            GuiScreen.this.currentCategory = GuiScreen.this.currentCategory == Category.OPTIONS ? Category.NONE : Category.OPTIONS;
            GuiScreen.this.updateItems();
         }
      });
      this.telegramButton = new ActionButton(new float[]{this.posX + this.width / 2.0F + 50.0F, this.posY - 22.0F, 16.0F, 16.0F}, new Action() {
         public void render(ActionButton button, MatrixStack matrixStack, int mouseX, int mouseY) {
            RoundedUtils.drawRound(button.posX, button.posY, button.width, button.height, 4.0F, new Color(236517));
            ResourceLocation resourceLocation = new ResourceLocation("minecraft", "neverbuy/telegram.png");
            RenderUtils.drawImage(matrixStack, resourceLocation, (int)(button.posX - 5.0F), (int)(button.posY - 1.0F), (int)(button.width + 11.0F), (int)(button.height + 2.0F), 10, 10);
         }

         public void mouseClicked(double mouseX, double mouseY, int button) {
            GuiScreen.this.currentCategory = GuiScreen.this.currentCategory == Category.TELEGRAM ? Category.NONE : Category.TELEGRAM;
            GuiScreen.this.updateItems();
         }
      });
      this.addButton = new ActionButton(new float[]{this.posX + (this.width - 80.0F) / 2.0F, this.posY + this.height + 10.0F, 80.0F, 16.0F}, new Action() {
         public void render(ActionButton button, MatrixStack matrixStack, int mouseX, int mouseY) {
            RoundedUtils.drawRound(button.posX, button.posY, button.width, button.height, 4.0F, new Color(2103849));
            Font f = Fonts.Inter.get(18.0F);
            f.draw("Добавить", button.posX + (button.width - (float)f.getWidth("Добавить")) / 2.0F, button.posY + 2.0F, -1);
         }

         public void mouseClicked(double mouseX, double mouseY, int button) {
            CustomItem customItem = (new CustomItem()).item(Items.STONE).sell(BigDecimal.TEN).price(BigDecimal.TEN).name("Камень");
            Constants.LIST.items.add(customItem);
            GuiScreen.this.updateItems();
            ((ItemComponent)GuiScreen.this.itemComponents.get(GuiScreen.this.itemComponents.size() - 1)).openBoard();
         }
      });
      this.updateItems();
   }

   public void render(@NonNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      if (matrixStack == null) {
         throw new NullPointerException("matrixStack is marked non-null but is null");
      } else {
         try {
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            BlurUtil.registerRenderCall(() -> {
               Screen.fill(matrixStack, 0, 0, this.mc.getMainWindow().getScaledWidth(), this.mc.getMainWindow().getScaledHeight(), Color.BLACK.getRGB());
            });
            this.scrollAnimation = RenderUtils.lerp(this.scrollAnimation, this.scroll, 8.0F);
            if ((double)this.maxAlpha < 0.7D) {
               this.maxAlpha += 0.005F;
            }

            Color black = ColorHelper.injectAlpha(Color.BLACK, this.maxAlpha);
            Screen.fill(matrixStack, 0, 0, this.mc.getMainWindow().getScaledWidth(), this.mc.getMainWindow().getScaledHeight(), black.getRGB());
            BlurUtil.draw(8);
            CopyOnWriteArrayList items;
            if (this.currentCategory != Category.OPTIONS && this.currentCategory != Category.TELEGRAM) {
               items = this.itemComponents;
            } else {
               items = this.settingComponents;
            }

            this.scroll = MathHelper.clamp(this.scroll, (float)(-items.size() * 20), 0.0F);
            Color c = new Color(1117717);
            RoundedUtils.drawRound(this.posX, this.posY, this.width, this.height, 8.0F, c);
            Font f = Fonts.mntsb.get(20.0F);
            String text = "NeverBuy";
            int start = (new Color(4408534)).getRGB();
            int end = (new Color(410310)).getRGB();
            RoundedUtils.drawRound(this.posX + (this.width - (float)f.getWidth(text)) / 2.0F, this.posY - 21.0F, (float)(f.getWidth(text) + 8), (float)(f.getHeight() + 2), 4.0F, c);
            RenderUtils.drawSmoothText(matrixStack, f, text, this.posX + 4.0F + (this.width - (float)f.getWidth(text)) / 2.0F, this.posY - 20.0F, start, end, 2000L);
            Scissor.push();
            Scissor.setFromComponentCoordinates((double)this.posX, (double)this.posY, (double)this.width, (double)this.height);

            Iterator var13;
            ItemComponent itemComponent;
            for(var13 = this.itemComponents.iterator(); var13.hasNext(); itemComponent.offsets[2] = this.scrollAnimation) {
               itemComponent = (ItemComponent)var13.next();
               itemComponent.render(matrixStack, mouseX, mouseY);
            }

            SettingComponent settingComponent;
            for(var13 = this.settingComponents.iterator(); var13.hasNext(); settingComponent.scroll = this.scrollAnimation) {
               settingComponent = (SettingComponent)var13.next();
               settingComponent.render(matrixStack, mouseX, mouseY);
            }

            Scissor.pop();
            this.actionButton.render(matrixStack, mouseX, mouseY);
            this.addButton.render(matrixStack, mouseX, mouseY);
            this.telegramButton.render(matrixStack, mouseX, mouseY);
            var13 = this.boards.iterator();

            while(var13.hasNext()) {
               ItemBoard board = (ItemBoard)var13.next();
               board.animation.update();
               if (board.animation.getValue() <= 0.0D) {
                  this.boards.remove(board);
               }

               if (!(board.animation.getValue() <= 0.0D)) {
                  board.render(matrixStack, mouseX, mouseY);
               }
            }

            this.circles.removeIf((circle) -> {
               return circle.ended;
            });
         } catch (Exception var12) {
            var12.printStackTrace(System.err);
         }

      }
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      try {
         GuiScreen.Circle circle = new GuiScreen.Circle(new Vector2f((float)mouseX, (float)mouseY));
         this.circles.add(circle);
         boolean breaked = false;
         List<ItemBoard> b = new ArrayList(this.boards);
         Collections.reverse(b);
         Iterator var9 = b.iterator();

         while(var9.hasNext()) {
            ItemBoard board = (ItemBoard)var9.next();
            boolean breakFor = board.mouseClicked(mouseX, mouseY, button);
            if (breakFor) {
               breaked = true;
               break;
            }
         }

         if (breaked) {
            return super.mouseClicked(mouseX, mouseY, button);
         }

         this.actionButton.mouseClicked(mouseX, mouseY, button);
         this.addButton.mouseClicked(mouseX, mouseY, button);
         this.telegramButton.mouseClicked(mouseX, mouseY, button);
         if (mouseX > (double)(this.posX + this.width) || mouseX < (double)this.posY || mouseY < (double)this.posY || mouseY > (double)(this.posY + this.height)) {
            return false;
         }

         ItemComponent hovered = this.getHovered(mouseX, mouseY);
         if (hovered != null) {
            if (button == 1) {
               if (!hovered.autoBuyItem.isConstant) {
                  if (this.boards.contains(hovered.itemBoard)) {
                     hovered.itemBoard.close();
                  }

                  Constants.LIST.items.remove(hovered.autoBuyItem);
                  this.updateItems();
               }
            } else {
               hovered.mouseClicked(mouseX, mouseY, button);
            }
         }

         Iterator var14 = this.settingComponents.iterator();

         while(var14.hasNext()) {
            SettingComponent settingComponent = (SettingComponent)var14.next();
            settingComponent.mouseClicked(mouseX, mouseY, button);
         }
      } catch (Exception var12) {
         var12.printStackTrace(System.err);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public ItemComponent getHovered(double mouseX, double mouseY) {
      Iterator var5 = this.itemComponents.iterator();

      ItemComponent component;
      do {
         if (!var5.hasNext()) {
            return null;
         }

         component = (ItemComponent)var5.next();
      } while(!component.isHovered(mouseX, mouseY));

      return component;
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      try {
         Iterator var6 = this.boards.iterator();

         while(var6.hasNext()) {
            ItemBoard board = (ItemBoard)var6.next();
            board.mouseReleased();
         }
      } catch (Exception var8) {
         var8.printStackTrace(System.err);
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      try {
         Iterator var4 = this.settingComponents.iterator();

         while(var4.hasNext()) {
            SettingComponent settingComponent = (SettingComponent)var4.next();
            settingComponent.keyPressed(keyCode);
         }

         var4 = this.boards.iterator();

         while(var4.hasNext()) {
            ItemBoard itemBoard = (ItemBoard)var4.next();
            itemBoard.keyPressed(keyCode);
         }
      } catch (Exception var6) {
         var6.printStackTrace(System.err);
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   public boolean charTyped(char codePoint, int modifiers) {
      try {
         Iterator var3 = this.settingComponents.iterator();

         while(var3.hasNext()) {
            SettingComponent settingComponent = (SettingComponent)var3.next();
            if (settingComponent instanceof DelayComponent) {
               DelayComponent delay = (DelayComponent)settingComponent;
               delay.charTyped(codePoint);
            } else if (settingComponent instanceof FieldSettingComponent) {
               FieldSettingComponent field = (FieldSettingComponent)settingComponent;
               field.charTyped(codePoint);
            }
         }

         var3 = this.boards.iterator();

         while(var3.hasNext()) {
            ItemBoard itemBoard = (ItemBoard)var3.next();
            itemBoard.charTyped(codePoint);
         }
      } catch (Exception var7) {
         var7.printStackTrace(System.err);
      }

      return super.charTyped(codePoint, modifiers);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      this.scroll += (float)(delta * 15.0D);
      return super.mouseScrolled(mouseX, mouseY, delta);
   }

   public void updateItems() {
      this.scroll = 0.0F;
      this.itemComponents.clear();
      this.settingComponents.clear();
      float offsetX = 10.0F;
      float offsetY = 10.0F;
      int count = 0;
      Iterator var4;
      float[] current;
      if (this.currentCategory == Category.OPTIONS) {
         offsetY = 5.0F;

         for(var4 = Constants.AUTOBUY.all().iterator(); var4.hasNext(); offsetY += 25.0F) {
            Setting<?, ?> setting = (Setting)var4.next();
            current = new float[]{this.posX + 5.0F, this.posY, this.width - 10.0F, 18.0F, offsetY};
            if (setting instanceof Delay) {
               Delay delay = (Delay)setting;
               DelayComponent delayComponent = new DelayComponent(current, delay);
               this.settingComponents.add(delayComponent);
            } else if (setting instanceof CheckBox) {
               CheckBox checkBox = (CheckBox)setting;
               CheckBoxComponent checkboxComponent = new CheckBoxComponent(current, checkBox);
               this.settingComponents.add(checkboxComponent);
            } else if (setting instanceof FieldSetting) {
               FieldSetting fieldSetting = (FieldSetting)setting;
               FieldSettingComponent fieldSettingComponent = new FieldSettingComponent(current, fieldSetting);
               this.settingComponents.add(fieldSettingComponent);
            }
         }

      } else if (this.currentCategory != Category.TELEGRAM) {
         var4 = Constants.LIST.items.iterator();

         while(var4.hasNext()) {
            AutoBuyItem<?> autoBuyItem = (AutoBuyItem)var4.next();
            ItemComponent itemComponent = new ItemComponent(new float[]{this.posX, this.posY, 18.0F, 18.0F, offsetX, offsetY}, autoBuyItem);
            this.itemComponents.add(itemComponent);
            ++count;
            if (count % 9 == 0) {
               offsetX = 10.0F;
               offsetY += 25.0F;
            } else {
               offsetX += 26.5F;
            }
         }

      } else {
         offsetY = 5.0F;

         for(var4 = Constants.TELEGRAM.settings().iterator(); var4.hasNext(); offsetY += 25.0F) {
            FieldSetting setting = (FieldSetting)var4.next();
            current = new float[]{this.posX + 5.0F, this.posY, this.width - 10.0F, 18.0F, offsetY};
            FieldSettingComponent fieldSettingComponent = new FieldSettingComponent(current, setting);
            this.settingComponents.add(fieldSettingComponent);
         }

      }
   }

   private static class Circle {
      private final Vector2f pos;
      private final long time;
      private float scale;
      private float alpha;
      boolean ended;

      public Circle(Vector2f pos) {
         this.pos = pos;
         this.time = System.currentTimeMillis();
      }

      public void render(MatrixStack matrixStack) {
         this.update();
         Color color = ColorHelper.injectAlpha(Color.WHITE, this.alpha);
         RenderSystem.pushMatrix();
         RenderSystem.translatef(this.pos.x, this.pos.y, 0.0F);
         RenderSystem.scalef(this.scale, this.scale, 0.0F);
         RenderSystem.translatef(-this.pos.x, -this.pos.y, 0.0F);
         RenderUtils.drawCircle(matrixStack, this.pos.x, this.pos.y, 0.0F, 360.0F, 4.0F, false, color);
         RenderSystem.popMatrix();
      }

      public void update() {
         this.scale = RenderUtils.lerp(this.scale, 10.0F, 0.1F);
         if (System.currentTimeMillis() - this.time < 1500L) {
            this.alpha = RenderUtils.lerp(this.alpha, 1.0F, 4.0F);
         } else {
            this.alpha = RenderUtils.lerp(this.alpha, 0.0F, 4.0F);
            if (this.alpha <= 0.01F) {
               this.ended = true;
            }
         }

      }
   }
}
