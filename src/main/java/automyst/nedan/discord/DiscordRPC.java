package automyst.nedan.discord;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DiscordRPC extends Library {
   DiscordRPC INSTANCE = (DiscordRPC)Native.loadLibrary("discord-rpc", DiscordRPC.class);

   void Discord_UpdatePresence(DiscordRichPresence var1);

   void Discord_RunCallbacks();

   void Discord_Initialize(String var1, DiscordEventHandlers var2, boolean var3, String var4);

   void Discord_Shutdown();
}
