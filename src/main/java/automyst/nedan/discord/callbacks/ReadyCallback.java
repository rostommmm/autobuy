package automyst.nedan.discord.callbacks;

import automyst.nedan.discord.DiscordUser;
import com.sun.jna.Callback;

public interface ReadyCallback extends Callback {
   void apply(DiscordUser var1);
}
