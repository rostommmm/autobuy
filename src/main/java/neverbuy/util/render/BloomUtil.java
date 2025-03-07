package neverbuy.util.render;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.shader.Framebuffer;
import neverbuy.util.IUtil;
import neverbuy.util.misc.Utils;
import org.lwjgl.opengl.GL30;

public class BloomUtil implements IUtil {
   private static final ShaderUtil bloom = new ShaderUtil("bloom");
   private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
   private static final Framebuffer inFrameBuffer = new Framebuffer(1, 1, true, false);
   private static final Framebuffer outFrameBuffer = new Framebuffer(1, 1, true, false);

   public static void registerRenderCall(IRenderCall rc) {
      renderQueue.add(rc);
   }

   public static void draw(int radius) {
      if (!renderQueue.isEmpty()) {
         setupBuffer(inFrameBuffer);
         setupBuffer(outFrameBuffer);
         inFrameBuffer.bindFramebuffer(true);

         while(!renderQueue.isEmpty()) {
            ((IRenderCall)renderQueue.poll()).execute();
         }

         outFrameBuffer.bindFramebuffer(true);
         bloom.init();
         bloom.setUniformf("radius", (float)radius);
         bloom.setUniformi("sampler1", 0);
         bloom.setUniformi("sampler2", 20);
         bloom.setUniformfb("kernel", Utils.getFloatBuffer(radius));
         bloom.setUniformf("texelSize", 1.0F / (float)mc.getMainWindow().getWidth(), 1.0F / (float)mc.getMainWindow().getHeight());
         bloom.setUniformf("direction", 2.0F, 0.0F);
         GlStateManager.enableBlend();
         GlStateManager.blendFunc(1, 770);
         GL30.glAlphaFunc(516, 1.0E-4F);
         inFrameBuffer.bindFramebufferTexture();
         ShaderUtil.drawQuads();
         mc.getFramebuffer().bindFramebuffer(false);
         GlStateManager.blendFunc(770, 771);
         bloom.setUniformf("direction", 0.0F, 2.0F);
         outFrameBuffer.bindFramebufferTexture();
         GL30.glActiveTexture(34004);
         inFrameBuffer.bindFramebufferTexture();
         GL30.glActiveTexture(33984);
         ShaderUtil.drawQuads();
         bloom.unload();
         inFrameBuffer.unbindFramebufferTexture();
         GlStateManager.disableBlend();
      }
   }

   public static Framebuffer setupBuffer(Framebuffer frameBuffer) {
      if (frameBuffer.framebufferWidth == mc.getMainWindow().getWidth() && frameBuffer.framebufferHeight == mc.getMainWindow().getHeight()) {
         frameBuffer.framebufferClear(false);
      } else {
         frameBuffer.resize(Math.max(1, mc.getMainWindow().getWidth()), Math.max(1, mc.getMainWindow().getHeight()), false);
      }

      frameBuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
      return frameBuffer;
   }
}
