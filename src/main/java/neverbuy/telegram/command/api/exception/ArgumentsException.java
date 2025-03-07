package neverbuy.telegram.command.api.exception;

public class ArgumentsException extends CommandException {
   public ArgumentsException(String cause) {
      super("Аргументы", cause);
   }
}
