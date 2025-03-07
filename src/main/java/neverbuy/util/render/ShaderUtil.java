package neverbuy.util.render;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import net.minecraft.client.MainWindow;
import net.minecraft.util.ResourceLocation;
import neverbuy.util.IUtil;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ShaderUtil implements IUtil {
   private final int programID;
   private final String name;

   public ShaderUtil(String fragmentShaderLoc) {
      int program = GL20.glCreateProgram();
      this.name = fragmentShaderLoc;
      String blur = "#version 120\n\nuniform sampler2D sampler1;\nuniform sampler2D sampler2;\nuniform vec2 texelSize;\nuniform vec2 direction;\nuniform float radius;\nuniform float kernel[64];\n\nvoid main()\n{\n    vec2 uv = gl_TexCoord[0].st;\n    uv.y = 1.0f - uv.y;\n\n    float alpha = texture2D(sampler2, uv).a;\n    if (direction.x == 0.0 && alpha == 0.0) {\n        discard;\n    }\n\n    vec4 pixel_color = texture2D(sampler1, uv) * kernel[0];\n    for (float f = 1; f <= radius; f++) {\n        vec2 offset = f * texelSize * direction;\n        pixel_color += texture2D(sampler1, uv - offset) * kernel[int(f)];\n        pixel_color += texture2D(sampler1, uv + offset) * kernel[int(f)];\n    }\n\n    gl_FragColor = vec4(pixel_color.rgb, direction.x == 0.0 ? alpha : 1.0);\n}\n";
      String rounded = "#version 120\n\nuniform vec2 location, rectSize;\nuniform vec4 color;\nuniform float radius;\nuniform bool blur;\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b, 0.0)) - r;\n}\n\n\nvoid main() {\n    vec2 rectHalf = rectSize * .5;\n    // Smooth the result (free antialiasing).\n    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n\n}";
      String roundedGradient = "#version 120\n\nuniform float round;\nuniform vec2 size;\nuniform vec4 color1;\nuniform vec4 color2;\nuniform vec4 color3;\nuniform vec4 color4;\n\nfloat alpha(vec2 d, vec2 d1) {\n    vec2 v = abs(d) - d1 + round;\n    return min(max(v.x, v.y), 0.0) + length(max(v, .0f)) - round;\n}\n\nvoid main() {\n\t vec2 coords = gl_TexCoord[0].st;\n    vec2 centre = .5f * size;\n    vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);\n    gl_FragColor = vec4(color.rgb, color.a * (1.f- smoothstep(0.f, 1.5f, alpha(centre - (gl_TexCoord[0].st * size), centre - 1.f))));\n}\n";
      String roundedBlurred = "#version 120\n\nuniform float softness;\nuniform float radius;\nuniform vec2 size;\nuniform vec4 color;\n\nfloat alpha(vec2 p, vec2 b) {\n    return length(max(abs(p) - b, .0f)) - radius;\n}\n\nvoid main() {\n    vec2 centre = .5f * size;\n    gl_FragColor = vec4(color.rgb, color.a * (1.f - smoothstep(-softness, softness, alpha(centre - (gl_TexCoord[0].st * size), centre - radius - softness))));\n}\n";
      String roundedBlurredGradient = "#version 120\n\nuniform float softness;\nuniform float radius;\nuniform vec2 size;\nuniform vec4 color1;\nuniform vec4 color2;\nuniform vec4 color3;\nuniform vec4 color4;\n\nfloat alpha(vec2 p, vec2 b) {\n    return length(max(abs(p) - b, .0f)) - radius;\n}\n\nvoid main() {\n    vec2 coords = gl_TexCoord[0].st;\n    vec2 centre = .5f * size;\n    vec4 color = mix(mix(color1, color2, coords.y), mix(color3, color4, coords.y), coords.x);\n    gl_FragColor = vec4(color.rgb, color.a * (1.f - smoothstep(-softness, softness, alpha(centre - (gl_TexCoord[0].st * size), centre - radius - softness))));\n}\n";
      String roundedOutline = "#version 120\n\nuniform float round;\nuniform float thickness;\nuniform vec2 size;\nuniform vec4 color;\n\nfloat alpha(vec2 d, vec2 d1) {\n\tvec2 v = abs(d) - d1 + round;\n\treturn min(max(v.x, v.y), 0.0) + length(max(v, .0f)) - round;\n}\n\nvoid main() {\n    vec2 centre = .5f * size;\n    vec2 smoothness = vec2(thickness - 1.5f, thickness);\n    gl_FragColor = vec4(color.rgb, color.a * (1.f - smoothstep(smoothness.x, smoothness.y, abs(alpha(centre - (gl_TexCoord[0].st * size), centre - thickness)))));\n}\n";
      String roundedTexture = "#version 120\n\nuniform float round;\nuniform sampler2D texture;\nuniform vec2 size;\n\nfloat dstfn(vec2 p, vec2 b) {\n    return length(max(abs(p) - b, 0.0)) - round;\n}\n\nvoid main() {\n    vec2 tex = gl_TexCoord[0].st;\n    vec4 smpl = texture2D(texture, tex);\n    vec2 pixel = gl_TexCoord[0].st * size;\n    vec2 centre = 0.5 * size;\n    float sa = 1.f - smoothstep(0.0, 1, dstfn(centre - pixel, centre - round - 1));\n    gl_FragColor = vec4(smpl.rgb, smpl.a * sa);\n}\n";
      String bloom = "#version 120\n\nuniform sampler2D sampler1;\nuniform sampler2D sampler2;\nuniform vec2 texelSize;\nuniform vec2 direction;\nuniform float radius;\nuniform float kernel[64];\n\nvoid main(void)\n{\n    vec2 uv = gl_TexCoord[0].st;\n\n    if (direction.x == 0.0 && texture2D(sampler2, uv).a > 0.0) {\n    \tdiscard;\n    }\n\n    vec4 pixel_color = texture2D(sampler1, uv);\n    pixel_color.rgb *= pixel_color.a;\n    pixel_color *= kernel[0];\n\n    for (float f = 1; f <= radius; f++) {\n        vec2 offset = f * texelSize * direction;\n        vec4 left = texture2D(sampler1, uv - offset);\n        vec4 right = texture2D(sampler1, uv + offset);\n        left.rgb *= left.a;\n        right.rgb *= right.a;\n        pixel_color += (left + right) * kernel[int(f)];\n    }\n\n    gl_FragColor = vec4(pixel_color.rgb / pixel_color.a, pixel_color.a);\n}\n";
      byte var13 = -1;
      switch(fragmentShaderLoc.hashCode()) {
      case -265350775:
         if (fragmentShaderLoc.equals("rounded_texture")) {
            var13 = 6;
         }
         break;
      case -161337257:
         if (fragmentShaderLoc.equals("rounded_blurred_gradient")) {
            var13 = 4;
         }
         break;
      case 3027047:
         if (fragmentShaderLoc.equals("blur")) {
            var13 = 0;
         }
         break;
      case 46220464:
         if (fragmentShaderLoc.equals("rounded_outline")) {
            var13 = 5;
         }
         break;
      case 93832707:
         if (fragmentShaderLoc.equals("bloom")) {
            var13 = 7;
         }
         break;
      case 1137022776:
         if (fragmentShaderLoc.equals("rounded_blurred")) {
            var13 = 3;
         }
         break;
      case 1385468589:
         if (fragmentShaderLoc.equals("rounded")) {
            var13 = 1;
         }
         break;
      case 1456359554:
         if (fragmentShaderLoc.equals("rounded_gradient")) {
            var13 = 2;
         }
      }

      int fragmentShaderID;
      switch(var13) {
      case 0:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(blur.getBytes()), 35632);
         break;
      case 1:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(rounded.getBytes()), 35632);
         break;
      case 2:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedGradient.getBytes()), 35632);
         break;
      case 3:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedBlurred.getBytes()), 35632);
         break;
      case 4:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedBlurredGradient.getBytes()), 35632);
         break;
      case 5:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedOutline.getBytes()), 35632);
         break;
      case 6:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(roundedTexture.getBytes()), 35632);
         break;
      case 7:
         fragmentShaderID = this.createShader(new ByteArrayInputStream(bloom.getBytes()), 35632);
         break;
      default:
         try {
            InputStream inputStream = mc.getResourceManager().getResource(new ResourceLocation("minecraft", "automyst/shaders/" + fragmentShaderLoc.toLowerCase())).getInputStream();
            fragmentShaderID = this.createShader(inputStream, 35632);
         } catch (IOException var15) {
            fragmentShaderID = -1;
         }
      }

      GL20.glAttachShader(program, fragmentShaderID);
      String vertex = "#version 120\n\nvoid main() {\n    gl_TexCoord[0] = gl_MultiTexCoord0;\n    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n}";
      int vertexShaderID = this.createShader(new ByteArrayInputStream(vertex.getBytes()), 35633);
      GL20.glAttachShader(program, vertexShaderID);
      GL20.glLinkProgram(program);
      int status = GL20.glGetProgrami(program, 35714);
      if (status == 0) {
      }

      this.programID = program;
   }

   public void init() {
      GL20.glUseProgram(this.programID);
   }

   public void unload() {
      GL20.glUseProgram(0);
   }

   public int getUniform(String name) {
      return GL20.glGetUniformLocation(this.programID, name);
   }

   public void setUniformf(String name, float... args) {
      int loc = ARBShaderObjects.glGetUniformLocationARB(this.programID, name);
      switch(args.length) {
      case 1:
         ARBShaderObjects.glUniform1fARB(loc, args[0]);
         break;
      case 2:
         ARBShaderObjects.glUniform2fARB(loc, args[0], args[1]);
         break;
      case 3:
         ARBShaderObjects.glUniform3fARB(loc, args[0], args[1], args[2]);
         break;
      case 4:
         ARBShaderObjects.glUniform4fARB(loc, args[0], args[1], args[2], args[3]);
      }

   }

   public void setUniformi(String name, int... args) {
      int loc = GL20.glGetUniformLocation(this.programID, name);
      if (args.length > 1) {
         GL20.glUniform2i(loc, args[0], args[1]);
      } else {
         GL20.glUniform1i(loc, args[0]);
      }

   }

   public void setUniformfb(String name, FloatBuffer buffer) {
      GL30.glUniform1fv(GL30.glGetUniformLocation(this.programID, name), buffer);
   }

   public static void drawQuads() {
      MainWindow sr = mc.getMainWindow();
      float width = (float)sr.getScaledWidth();
      float height = (float)sr.getScaledHeight();
      GL20.glBegin(7);
      GL20.glTexCoord2f(0.0F, 1.0F);
      GL20.glVertex2f(0.0F, 0.0F);
      GL20.glTexCoord2f(0.0F, 0.0F);
      GL20.glVertex2f(0.0F, height);
      GL20.glTexCoord2f(1.0F, 0.0F);
      GL20.glVertex2f(width, height);
      GL20.glTexCoord2f(1.0F, 1.0F);
      GL20.glVertex2f(width, 0.0F);
      GL20.glEnd();
   }

   public static void drawQuads(float x, float y, float width, float height) {
      GL20.glBegin(7);
      GL20.glTexCoord2f(0.0F, 0.0F);
      GL20.glVertex2f(x, y);
      GL20.glTexCoord2f(0.0F, 1.0F);
      GL20.glVertex2f(x, y + height);
      GL20.glTexCoord2f(1.0F, 1.0F);
      GL20.glVertex2f(x + width, y + height);
      GL20.glTexCoord2f(1.0F, 0.0F);
      GL20.glVertex2f(x + width, y);
      GL20.glEnd();
   }

   public static void drawQuads(double x, double y, double width, double height) {
      GL20.glBegin(7);
      GL20.glTexCoord2f(0.0F, 0.0F);
      GL20.glVertex2d(x, y);
      GL20.glTexCoord2f(0.0F, 1.0F);
      GL20.glVertex2d(x, y + height);
      GL20.glTexCoord2f(1.0F, 1.0F);
      GL20.glVertex2d(x + width, y + height);
      GL20.glTexCoord2f(1.0F, 0.0F);
      GL20.glVertex2d(x + width, y);
      GL20.glEnd();
   }

   private int createShader(InputStream inputStream, int shaderType) {
      int shader = GL20.glCreateShader(shaderType);
      GL20.glShaderSource(shader, readInputStream(inputStream));
      GL20.glCompileShader(shader);
      if (GL20.glGetShaderi(shader, 35713) == 0) {
         String var10001 = this.name;
         System.out.println(var10001 + ": " + GL20.glGetShaderInfoLog(shader, 4096));
      }

      return shader;
   }

   public static String readInputStream(InputStream inputStream) {
      StringBuilder stringBuilder = new StringBuilder();

      try {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

         String line;
         while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append('\n');
         }
      } catch (Exception var4) {
         var4.printStackTrace(System.err);
      }

      return stringBuilder.toString();
   }
}
