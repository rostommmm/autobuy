package neverbuy.chatcommands.api;

import net.minecraft.client.Minecraft;
import neverbuy.telegram.command.api.CommandInfo;

public abstract class ChatCommand {
   private final String name;
   private final String usage;
   private final String desc;
   private final int args;
   protected final Minecraft mc = Minecraft.getInstance();

   public ChatCommand() {
      CommandInfo info = (CommandInfo)this.getClass().getAnnotation(CommandInfo.class);
      this.name = info.name();
      this.usage = info.usage();
      this.desc = info.desc();
      this.args = this.usage.split(" ").length;
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
