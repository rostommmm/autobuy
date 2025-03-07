package neverbuy.telegram.command.api.exception;

import neverbuy.Constants;
import neverbuy.util.misc.ChatUtil;

public class CommandException extends Exception {
   private final String name;

   public CommandException(String name, String cause) {
      super(cause);
      this.name = name;
   }

   public void printStackTrace() {
      ChatUtil.addMessage("Ошибка телеграм:");
      String var10000 = this.name;
      ChatUtil.addMessage(var10000 + ": " + this.getMessage());
      Constants.TELEGRAM.sendMessage("Ошибка:");
      String var10001 = this.name;
      Constants.TELEGRAM.sendMessage(var10001 + ": " + this.getMessage());
   }
}
