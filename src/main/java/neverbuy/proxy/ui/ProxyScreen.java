package neverbuy.proxy.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import neverbuy.Constants;
import neverbuy.proxy.Connection;
import neverbuy.proxy.Type;

public class ProxyScreen extends Screen {
   protected ProxyScreen(ITextComponent titleIn) {
      super(titleIn);
   }/*
   private final Connection connection;
   final List<TextFieldWidget> textFieldWidgets;

   public ProxyScreen() {
      super(new StringTextComponent("Proxy"));
      Minecraft mc = Minecraft.getInstance();
      this.connection = Constants.PROXY;
      this.textFieldWidgets = new ArrayList();
      int offset = 60;
      float width = (float)mc.getMainWindow().getScaledWidth();
      int posX = (int)((width - 150.0F) / 2.0F);
      this.textFieldWidgets.add(new TextFieldWidget(mc.fontRenderer, posX, offset, 150, 20, new StringTextComponent("Тип"), (full) -> {
         Type type = this.parseProxy(full);
         this.connection.setProxyType(type);
      }));
      offset = offset + 60;
      this.textFieldWidgets.add(new TextFieldWidget(mc.fontRenderer, posX, offset, 150, 20, new StringTextComponent("Адрес"), (full) -> {
         try {
            this.connection.setProxyAddr(new InetSocketAddress(full.split(":")[0], Integer.parseInt(full.split(":")[1])));
         } catch (Exception var3) {
            this.connection.setProxyAddr((InetSocketAddress)null);
         }

      }));
      offset += 60;
      List var10000 = this.textFieldWidgets;
      FontRenderer var10003 = mc.fontRenderer;
      StringTextComponent var10008 = new StringTextComponent("Адрес");
      Connection var10009 = this.connection;
      Objects.requireNonNull(var10009);
      var10000.add(new TextFieldWidget(var10003, posX, offset, 150, 20, var10008, var10009::setUsername));
      offset += 60;
      var10000 = this.textFieldWidgets;
      var10003 = mc.fontRenderer;
      var10008 = new StringTextComponent("Адрес");
      var10009 = this.connection;
      Objects.requireNonNull(var10009);
      var10000.add(new TextFieldWidget(var10003, posX, offset, 150, 20, var10008, var10009::setPassword));
   }

   public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      Minecraft mc = Minecraft.getInstance();
      this.renderBackground(matrixStack);
      float center = (float)this.width / 2.0F;
      float offset = 40.0F;
      String typeString = String.format("Тип (Текущий: %s)", this.connection.getProxyType().getName());
      String addresString = String.format("Адрес (Текущий: %s)", this.connection.getProxyAddr() == null ? "Нету" : this.connection.getProxyAddr().getHostString() + ":" + this.connection.getProxyAddr().getPort());
      String usernameString = String.format("Логин (Текущий: %s)", this.connection.getUsername().isEmpty() ? "Нету" : this.connection.getUsername());
      String passwordString = String.format("Пароль (Текущий: %s)", this.connection.getPassword().isEmpty() ? "Нету" : this.connection.getPassword());
      mc.fontRenderer.drawString(matrixStack, "Прокси", (float)(this.width - mc.fontRenderer.getStringWidth("Прокси")) / 2.0F, 4.0F, -1);
      mc.fontRenderer.drawString(matrixStack, typeString, center - (float)mc.fontRenderer.getStringWidth(typeString) / 2.0F, offset, -1);
      offset += 60.0F;
      mc.fontRenderer.drawString(matrixStack, addresString, center - (float)mc.fontRenderer.getStringWidth(addresString) / 2.0F, offset, -1);
      offset += 60.0F;
      mc.fontRenderer.drawString(matrixStack, usernameString, center - (float)mc.fontRenderer.getStringWidth(usernameString) / 2.0F, offset, -1);
      offset += 60.0F;
      mc.fontRenderer.drawString(matrixStack, passwordString, center - (float)mc.fontRenderer.getStringWidth(passwordString) / 2.0F, offset, -1);
      Iterator var12 = this.textFieldWidgets.iterator();

      while(var12.hasNext()) {
         TextFieldWidget textFieldWidget = (TextFieldWidget)var12.next();
         textFieldWidget.render(matrixStack, mouseX, mouseY, partialTicks);
      }

      super.render(matrixStack, mouseX, mouseY, partialTicks);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      Iterator var6 = this.textFieldWidgets.iterator();

      while(var6.hasNext()) {
         TextFieldWidget textFieldWidget = (TextFieldWidget)var6.next();
         textFieldWidget.mouseClicked(mouseX, mouseY, button);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.minecraft.displayGuiScreen(new MultiplayerScreen(this));
         return true;
      } else {
         Iterator var4 = this.textFieldWidgets.iterator();

         while(var4.hasNext()) {
            TextFieldWidget textFieldWidget = (TextFieldWidget)var4.next();
            textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean charTyped(char codePoint, int modifiers) {
      Iterator var3 = this.textFieldWidgets.iterator();

      while(var3.hasNext()) {
         TextFieldWidget textFieldWidget = (TextFieldWidget)var3.next();
         textFieldWidget.charTyped(codePoint, modifiers);
      }

      return super.charTyped(codePoint, modifiers);
   }

   private Type parseProxy(String full) {
      String[] split = full.split(":");
      if (split.length < 1) {
         return Type.DIRECT;
      } else if (split[0].equalsIgnoreCase("http")) {
         return Type.HTTP;
      } else if (split[0].equalsIgnoreCase("socks4")) {
         return Type.SOCKS4;
      } else {
         return split[0].equalsIgnoreCase("socks5") ? Type.SOCKS5 : Type.DIRECT;
      }
   }*/
}
