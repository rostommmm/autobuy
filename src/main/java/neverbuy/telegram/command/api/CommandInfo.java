package neverbuy.telegram.command.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {
   String name();

   String usage();

   String desc();
}
