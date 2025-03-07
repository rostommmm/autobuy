package neverbuy.util.animation;

import java.io.PrintStream;
import neverbuy.util.animation.bezier.Bezier;
import neverbuy.util.animation.bezier.list.CubicBezier;
import neverbuy.util.animation.util.Easing;
import neverbuy.util.animation.util.Easings;

public class Animation {
   private long start;
   private double duration;
   private double fromValue;
   private double toValue;
   private Double value = 0.0D;
   private Easing easing;
   private Bezier bezier;
   private AnimationType type;
   private boolean debug;

   public Animation() {
      this.easing = Easings.NONE;
      this.bezier = new CubicBezier();
      this.type = AnimationType.EASING;
      this.debug = false;
   }

   public Animation animate(double valueTo, double duration) {
      return this.animate(valueTo, duration, Easings.NONE, false);
   }

   public Animation animate(double valueTo, double duration, Easing easing) {
      return this.animate(valueTo, duration, easing, false);
   }

   public Animation animate(double valueTo, double duration, Bezier bezier) {
      return this.animate(valueTo, duration, bezier, false);
   }

   public Animation animate(double valueTo, double duration, boolean safe) {
      return this.animate(valueTo, duration, Easings.NONE, safe);
   }

   public Animation animate(double valueTo, double duration, Easing easing, boolean safe) {
      if (this.check(safe, valueTo)) {
         if (this.isDebug()) {
            System.out.println("Animate cancelled due to target val equals from val");
         }

         return this;
      } else {
         this.setType(AnimationType.EASING).setEasing(easing).setDuration(duration * 1000.0D).setStart(System.currentTimeMillis()).setFromValue(this.getValue()).setToValue(valueTo);
         if (this.isDebug()) {
            PrintStream var10000 = System.out;
            double var10001 = this.getToValue();
            var10000.println("#animate {\n    to value: " + var10001 + "\n    from value: " + this.getValue() + "\n    duration: " + this.getDuration() + "\n}");
         }

         return this;
      }
   }

   public Animation animate(double valueTo, double duration, Bezier bezier, boolean safe) {
      if (this.check(safe, valueTo)) {
         if (this.isDebug()) {
            System.out.println("Animate cancelled due to target val equals from val");
         }

         return this;
      } else {
         this.setType(AnimationType.BEZIER).setBezier(bezier).setDuration(duration * 1000.0D).setStart(System.currentTimeMillis()).setFromValue(this.getValue()).setToValue(valueTo);
         if (this.isDebug()) {
            PrintStream var10000 = System.out;
            double var10001 = this.getToValue();
            var10000.println("#animate {\n    to value: " + var10001 + "\n    from value: " + this.getValue() + "\n    duration: " + this.getDuration() + "\n    type: " + this.getType().name() + "\n}");
         }

         return this;
      }
   }

   public boolean update() {
      boolean alive = this.isAlive();
      if (alive) {
         if (this.getType().equals(AnimationType.BEZIER)) {
            this.setValue(this.interpolate(this.getFromValue(), this.getToValue(), this.getBezier().getValue(this.calculatePart())));
         } else {
            this.setValue(this.interpolate(this.getFromValue(), this.getToValue(), this.getEasing().ease(this.calculatePart())));
         }
      } else {
         this.setStart(0L);
         this.setValue(this.getToValue());
      }

      return alive;
   }

   public boolean isAlive() {
      return !this.isDone();
   }

   public boolean isDone() {
      return this.calculatePart() >= 1.0D;
   }

   public double calculatePart() {
      return (double)(System.currentTimeMillis() - this.getStart()) / this.getDuration();
   }

   public boolean check(boolean safe, double valueTo) {
      return safe && this.isAlive() && (valueTo == this.getFromValue() || valueTo == this.getToValue() || valueTo == this.getValue());
   }

   public double interpolate(double start, double end, double pct) {
      return start + (end - start) * pct;
   }

   public Animation setStart(long start) {
      this.start = start;
      return this;
   }

   public Animation setDuration(double duration) {
      this.duration = duration;
      return this;
   }

   public Animation setFromValue(double fromValue) {
      this.fromValue = fromValue;
      return this;
   }

   public Animation setToValue(double toValue) {
      this.toValue = toValue;
      return this;
   }

   public Animation setValue(double value) {
      this.value = value;
      return this;
   }

   public Animation setEasing(Easing easing) {
      this.easing = easing;
      return this;
   }

   public Animation setDebug(boolean debug) {
      this.debug = debug;
      return this;
   }

   public Animation setBezier(Bezier bezier) {
      this.bezier = bezier;
      return this;
   }

   public Animation setType(AnimationType type) {
      this.type = type;
      return this;
   }

   public long getStart() {
      return this.start;
   }

   public double getDuration() {
      return this.duration;
   }

   public double getFromValue() {
      return this.fromValue;
   }

   public double getToValue() {
      return this.toValue;
   }

   public Double getValue() {
      return this.value;
   }

   public Easing getEasing() {
      return this.easing;
   }

   public Bezier getBezier() {
      return this.bezier;
   }

   public AnimationType getType() {
      return this.type;
   }

   public boolean isDebug() {
      return this.debug;
   }
}
