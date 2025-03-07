package automyst.nedan.discord.callbacks;

import automyst.nedan.discord.DiscordUser;
import com.sun.jna.Callback;

public interface JoinRequestCallback extends Callback {
   void apply(DiscordUser var1);
}
