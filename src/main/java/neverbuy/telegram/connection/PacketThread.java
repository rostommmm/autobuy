package neverbuy.telegram.connection;

import java.util.Objects;

public class PacketThread extends Thread {
   public PacketThread(Packet packet) {
      Objects.requireNonNull(packet);
     // super(packet::send);
      this.start();
      this.interrupt();
   }
}
