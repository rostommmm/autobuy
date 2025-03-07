package automyst.nedan.discord;

import automyst.nedan.discord.callbacks.DisconnectedCallback;
import automyst.nedan.discord.callbacks.ErroredCallback;
import automyst.nedan.discord.callbacks.JoinGameCallback;
import automyst.nedan.discord.callbacks.JoinRequestCallback;
import automyst.nedan.discord.callbacks.ReadyCallback;
import automyst.nedan.discord.callbacks.SpectateGameCallback;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

public class DiscordEventHandlers extends Structure {
   public DisconnectedCallback disconnected;
   public JoinRequestCallback joinRequest;
   public SpectateGameCallback spectateGame;
   public ReadyCallback ready;
   public ErroredCallback errored;
   public JoinGameCallback joinGame;

   protected List<String> getFieldOrder() {
      return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
   }
}
