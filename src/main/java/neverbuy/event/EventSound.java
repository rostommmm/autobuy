package neverbuy.event;

import net.minecraft.util.SoundEvent;

public class EventSound {
   SoundEvent soundEvent;

   public SoundEvent getSoundEvent() {
      return this.soundEvent;
   }

   public EventSound(SoundEvent soundEvent) {
      this.soundEvent = soundEvent;
   }
}
