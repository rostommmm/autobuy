package neverbuy.util.misc;

import com.mojang.blaze3d.platform.GlStateManager;
import java.nio.FloatBuffer;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import neverbuy.util.IUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public final class Utils implements IUtil {
   private static final HashMap<Integer, FloatBuffer> kernelCache = new HashMap();

   public static FloatBuffer getFloatBuffer(int radius) {
      FloatBuffer buffer = (FloatBuffer)kernelCache.get(radius);
      if (buffer == null) {
         buffer = BufferUtils.createFloatBuffer(radius);
         float[] kernel = new float[radius];
         float sigma = (float)radius / 2.0F;
         float total = 0.0F;

         int i;
         for(i = 0; i < radius; ++i) {
            float multiplier = (float)i / sigma;
            kernel[i] = 1.0F / (Math.abs(sigma) * 2.5066283F) * (float)Math.exp(-0.5D * (double)multiplier * (double)multiplier);
            total += i > 0 ? kernel[i] * 2.0F : kernel[0];
         }

         for(i = 0; i < radius; ++i) {
            kernel[i] /= total;
         }

         buffer.put(kernel);
         buffer.flip();
         kernelCache.put(radius, buffer);
      }

      return buffer;
   }

   public static int getTextureId(ResourceLocation identifier) {
      SimpleTexture texture = new SimpleTexture(identifier);
      if (mc.getTextureManager().getTexture(identifier) == null) {
         mc.getTextureManager().loadTexture(identifier, texture);
      } else {
         texture = (SimpleTexture)mc.getTextureManager().getTexture(identifier);
      }

      return texture.getGlTextureId();
   }

   public static void initStencilReplace() {
      mc.getFramebuffer().enableStencil();
      GL30.glEnable(2960);
      GL30.glClear(1024);
      GlStateManager.stencilFunc(514, 1, 1);
      GlStateManager.stencilOp(7681, 7681, 7681);
      GlStateManager.colorMask(false, false, false, false);
   }

   public static void uninitStencilReplace() {
      GlStateManager.colorMask(true, true, true, true);
      GlStateManager.stencilOp(7680, 7680, 7680);
      GlStateManager.stencilFunc(514, 1, 1);
   }

   private Utils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
