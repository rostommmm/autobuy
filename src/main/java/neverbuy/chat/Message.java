package neverbuy.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
   private final String owner;
   private final String message;
   private final LocalDateTime dateTime;

   public Message(String owner, String message, String date) {
      this.owner = owner;
      this.message = message;
      this.dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm:ss"));
   }

   public String toString() {
      return "Message{msg=" + this.message + ",owner=" + this.owner + ",date=" + this.dateTime + "}";
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Message)) {
         return false;
      } else {
         Message msg = (Message)obj;
         return msg.owner.equals(this.owner) && msg.message.equals(this.message) && msg.dateTime.equals(this.dateTime);
      }
   }

   public String getOwner() {
      return this.owner;
   }

   public String getMessage() {
      return this.message;
   }

   public LocalDateTime getDateTime() {
      return this.dateTime;
   }
}
