package neverbuy.telegram.command.api;

import net.minecraft.client.Minecraft;
import neverbuy.Constants;

public abstract class Command {
   private final String name;
   private final String usage;
   private final String desc;
   private final int args;
   protected final Minecraft mc = Minecraft.getInstance();

   public Command() {
      CommandInfo commandInfo = (CommandInfo)this.getClass().getAnnotation(CommandInfo.class);
      this.name = commandInfo.name();
      this.usage = commandInfo.usage();
      this.desc = commandInfo.desc();
      this.args = this.usage.split(" ").length;
   }

   public void sendMessage(String msg) {
      Constants.TELEGRAM.sendMessage(msg);
   }

   public abstract void execute(String[] var1);

   public String getName() {
      return this.name;
   }

   public String getUsage() {
      return this.usage;
   }

   public String getDesc() {
      return this.desc;
   }

   public int getArgs() {
      return this.args;
   }

   public Minecraft getMc() {
      return this.mc;
   }
}
