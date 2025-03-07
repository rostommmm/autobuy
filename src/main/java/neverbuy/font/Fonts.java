package neverbuy.font;

import it.unimi.dsi.fastutil.floats.Float2ObjectArrayMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;

public enum Fonts {
   Inter("inter-semibold.ttf"),
   mntsb("monsterat.ttf"),
   SF_Medium("sf_medium.ttf"),
   VarelaRound("VarelaRound-Regular.ttf");

   private final String file;
   private final Float2ObjectMap<Font> fontMap = new Float2ObjectArrayMap();

   public Font get(float size) {
      return (Font)this.fontMap.computeIfAbsent(size, (font) -> {
         try {
            return Font.create(this.getFile(), size, false, false, false);
         } catch (Exception var5) {
            throw new RuntimeException("Unable to load font: " + this, var5);
         }
      });
   }

   private Fonts(String file) {
      this.file = file;
   }

   public String getFile() {
      return this.file;
   }

   public Float2ObjectMap<Font> getFontMap() {
      return this.fontMap;
   }

   // $FF: synthetic method
   private static Fonts[] $values() {
      return new Fonts[]{Inter, mntsb, SF_Medium, VarelaRound};
   }
}
