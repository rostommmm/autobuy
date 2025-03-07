package neverbuy.util.render;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import java.nio.FloatBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import neverbuy.util.IUtil;
import neverbuy.util.misc.Utils;
import org.lwjgl.opengl.GL30;

public class BlurUtil implements IUtil {
   private static final MainWindow WINDOW;
   private static final ShaderUtil blur;
   private static final ConcurrentLinkedQueue<IRenderCall> renderQueue;
   private static final Framebuffer inFrameBuffer;
   private static final Framebuffer outFrameBuffer;

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
         blur.init();
         blur.setUniformf("radius", (float)radius);
         blur.setUniformi("sampler1", 0);
         blur.setUniformi("sampler2", 20);
         blur.setUniformfb("kernel", getKernel(radius));
         blur.setUniformf("texelSize", 1.0F / (float)WINDOW.getWidth(), 1.0F / (float)WINDOW.getHeight());
         blur.setUniformf("direction", 2.0F, 0.0F);
         GlStateManager.disableBlend();
         GlStateManager.blendFuncSeparate(770, 771, 1, 0);
         mc.getFramebuffer().bindFramebufferTexture();
         ShaderUtil.drawQuads();
         mc.getFramebuffer().bindFramebuffer(true);
         blur.setUniformf("direction", 0.0F, 2.0F);
         outFrameBuffer.bindFramebufferTexture();
         GL30.glActiveTexture(34004);
         inFrameBuffer.bindFramebufferTexture();
         GL30.glActiveTexture(33984);
         ShaderUtil.drawQuads();
         blur.unload();
         inFrameBuffer.unbindFramebufferTexture();
         GlStateManager.disableBlend();
      }
   }

   private static FloatBuffer getKernel(int radius) {
      return Utils.getFloatBuffer(radius);
   }

   private static void setupBuffer(Framebuffer frameBuffer) {
      if (frameBuffer.framebufferWidth == mc.getMainWindow().getWidth() && frameBuffer.framebufferHeight == mc.getMainWindow().getHeight()) {
         frameBuffer.framebufferClear(false);
      } else {
         frameBuffer.resize(Math.max(1, mc.getMainWindow().getWidth()), Math.max(1, mc.getMainWindow().getHeight()), false);
      }

   }

   static {
      WINDOW = mc.getMainWindow();
      blur = new ShaderUtil("blur");
      renderQueue = Queues.newConcurrentLinkedQueue();
      inFrameBuffer = new Framebuffer((int)((double)WINDOW.getWidth() / 2.0D), (int)((double)WINDOW.getHeight() / 2.0D), true, Minecraft.IS_RUNNING_ON_MAC);
      outFrameBuffer = new Framebuffer((int)((double)WINDOW.getWidth() / 2.0D), (int)((double)WINDOW.getHeight() / 2.0D), true, Minecraft.IS_RUNNING_ON_MAC);
   }
}
