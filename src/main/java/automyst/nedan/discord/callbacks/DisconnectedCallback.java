package automyst.nedan.discord.callbacks;

import com.sun.jna.Callback;

public interface DisconnectedCallback extends Callback {
   void apply(int var1, String var2);
}
