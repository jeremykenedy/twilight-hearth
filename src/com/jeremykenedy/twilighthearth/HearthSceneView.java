package com.jeremykenedy.twilighthearth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.preference.PreferenceManager;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public final class HearthSceneView extends GLSurfaceView implements GLSurfaceView.Renderer {
  private static final String[] MATERIAL_UNIFORMS = {
    "diffuseMap", "normalMap", "woodMap", "woodNormal"
  };
  private final HearthOptions options;
  private final FloatBuffer vertices =
      ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
  private final float[] matrix = new float[16];
  private final FloatBuffer sparks =
      ByteBuffer.allocateDirect(48 * 16).order(ByteOrder.nativeOrder()).asFloatBuffer();
  private int emberProgram, emberPosition;
  private final HashMap<String, Integer> uniforms = new HashMap<>();
  private int position;
  private volatile boolean active;
  private boolean frameAvailable;
  private SurfaceTexture video;
  private MediaPlayer player;
  private int program, texture, width, height;
  private final int[] materialTextures = new int[4];
  private long started;

  public HearthSceneView(Context context) {
    super(context);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    options =
        HearthOptions.resolve(
            prefs.getString("hearth_style", "brick"),
            prefs.getString("intensity", "normal"),
            prefs.getString("embers", "handful"),
            prefs.getString("motion", "normal"),
            prefs.getString("ambience", "normal"),
            prefs.getBoolean("randomize_all", false),
            new Random());
    vertices.put(new float[] {-1, -1, 1, -1, -1, 1, 1, 1}).position(0);
    setEGLContextClientVersion(2);
    setRenderer(this);
    setRenderMode(RENDERMODE_CONTINUOUSLY);
  }

  public void start() {
    active = true;
    onResume();
  }

  public void stop() {
    active = false;
    queueEvent(this::release);
    onPause();
  }

  private void release() {
    if (player != null) {
      player.release();
      player = null;
    }
    if (video != null) {
      video.release();
      video = null;
    }
  }

  private String readShader() {
    try (java.io.InputStream input = getContext().getAssets().open("hearth.frag")) {
      java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
      byte[] bytes = new byte[4096];
      int count;
      while ((count = input.read(bytes)) != -1) out.write(bytes, 0, count);
      return out.toString("UTF-8");
    } catch (java.io.IOException e) {
      throw new IllegalStateException(e);
    }
  }

  private int shader(int type, String source) {
    int shader = GLES20.glCreateShader(type);
    GLES20.glShaderSource(shader, source);
    GLES20.glCompileShader(shader);
    int[] ok = new int[1];
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, ok, 0);
    if (ok[0] == 0) throw new IllegalStateException(GLES20.glGetShaderInfoLog(shader));
    return shader;
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    release();
    int vertex =
        shader(
            GLES20.GL_VERTEX_SHADER,
            "attribute vec2 position; varying vec2 uv; void main(){uv=position*.5+.5;gl_Position=vec4(position,0.,1.);}");
    int fragment = shader(GLES20.GL_FRAGMENT_SHADER, readShader());
    program = GLES20.glCreateProgram();
    GLES20.glAttachShader(program, vertex);
    GLES20.glAttachShader(program, fragment);
    GLES20.glLinkProgram(program);
    int[] ok = new int[1];
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, ok, 0);
    if (ok[0] == 0) throw new IllegalStateException(GLES20.glGetProgramInfoLog(program));
    uniforms.clear();
    for (String name :
        new String[] {
          "fire",
          "diffuseMap",
          "normalMap",
          "woodMap",
          "woodNormal",
          "videoMatrix",
          "resolution",
          "time",
          "material",
          "light",
          "fireExposure"
        }) {
      uniforms.put(name, GLES20.glGetUniformLocation(program, name));
    }
    position = GLES20.glGetAttribLocation(program, "position");
    GLES20.glDeleteShader(vertex);
    GLES20.glDeleteShader(fragment);
    int[] textures = new int[1];
    GLES20.glGenTextures(1, textures, 0);
    texture = textures[0];
    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
    GLES20.glTexParameteri(
        GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
    GLES20.glTexParameteri(
        GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    GLES20.glGenTextures(4, materialTextures, 0);
    String material = new String[] {"brick", "stone", "modern"}[options.hearthStyle];
    loadTexture(materialTextures[0], "materials/" + material + "_diff.jpg");
    loadTexture(materialTextures[1], "materials/" + material + "_normal.jpg");
    loadTexture(materialTextures[2], "materials/timber_diff.jpg");
    loadTexture(materialTextures[3], "materials/timber_normal.jpg");
    video = new SurfaceTexture(texture);
    video.setOnFrameAvailableListener(
        surface -> {
          synchronized (this) {
            frameAvailable = true;
          }
        });
    Surface surface = new Surface(video);
    player = new MediaPlayer();
    try (AssetFileDescriptor asset = getResources().openRawResourceFd(R.raw.wood)) {
      player.setDataSource(asset.getFileDescriptor(), asset.getStartOffset(), asset.getLength());
      player.setSurface(surface);
      player.setLooping(true);
      player.setVolume(0, 0);
      player.prepare();
      player.setPlaybackParams(new PlaybackParams().setSpeed(options.speed).setPitch(1f));
      if (active) player.start();
    } catch (java.io.IOException e) {
      throw new IllegalStateException("The bundled fire could not play", e);
    } finally {
      surface.release();
    }
    int sparkVertex =
        shader(
            GLES20.GL_VERTEX_SHADER,
            "attribute vec4 spark; varying float brightness; void main(){gl_Position=vec4(spark.xy,0.,1.);gl_PointSize=spark.z;brightness=spark.w;}");
    int sparkFragment =
        shader(
            GLES20.GL_FRAGMENT_SHADER,
            "precision mediump float; varying float brightness; void main(){vec2 d=gl_PointCoord-.5;float a=exp(-dot(d,d)*18.)*brightness;gl_FragColor=vec4(1.,.42,.12,a);}");
    emberProgram = GLES20.glCreateProgram();
    GLES20.glAttachShader(emberProgram, sparkVertex);
    GLES20.glAttachShader(emberProgram, sparkFragment);
    GLES20.glLinkProgram(emberProgram);
    GLES20.glGetProgramiv(emberProgram, GLES20.GL_LINK_STATUS, ok, 0);
    if (ok[0] == 0) throw new IllegalStateException(GLES20.glGetProgramInfoLog(emberProgram));
    emberPosition = GLES20.glGetAttribLocation(emberProgram, "spark");
    GLES20.glDeleteShader(sparkVertex);
    GLES20.glDeleteShader(sparkFragment);
    started = System.nanoTime();
  }

  private void loadTexture(int textureId, String filename) {
    try (java.io.InputStream stream = getContext().getAssets().open(filename)) {
      Bitmap bitmap = BitmapFactory.decodeStream(stream);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
      GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
      GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
      bitmap.recycle();
    } catch (java.io.IOException error) {
      throw new IllegalStateException("The bundled material could not load", error);
    }
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int w, int h) {
    width = w;
    height = h;
    GLES20.glViewport(0, 0, w, h);
  }

  private void uniform(String name, float value) {
    GLES20.glUniform1f(uniforms.get(name), value);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    GLES20.glClearColor(0, 0, 0, 1);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    if (!active || video == null) return;
    synchronized (this) {
      if (frameAvailable) {
        video.updateTexImage();
        video.getTransformMatrix(matrix);
        frameAvailable = false;
      }
    }
    GLES20.glUseProgram(program);
    for (int i = 0; i < 4; i++) {
      GLES20.glActiveTexture(GLES20.GL_TEXTURE1 + i);
      GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, materialTextures[i]);
      GLES20.glUniform1i(uniforms.get(MATERIAL_UNIFORMS[i]), i + 1);
    }

    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
    GLES20.glUniform1i(uniforms.get("fire"), 0);
    GLES20.glUniformMatrix4fv(uniforms.get("videoMatrix"), 1, false, matrix, 0);
    GLES20.glUniform2f(uniforms.get("resolution"), width, height);
    float elapsed = (System.nanoTime() - started) / 1000000000f;
    uniform("time", elapsed);
    uniform("material", options.hearthStyle);
    uniform("light", options.brightness);
    uniform("fireExposure", options.fireExposure);
    GLES20.glEnableVertexAttribArray(position);
    GLES20.glVertexAttribPointer(position, 2, GLES20.GL_FLOAT, false, 0, vertices);
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    GLES20.glDisableVertexAttribArray(position);
    drawEmbers(elapsed);
  }

  private void drawEmbers(float time) {
    sparks.clear();
    for (int i = 0; i < options.emberCount; i++) {
      float phase = (time * (.045f + i * .0007f) * options.speed + i * .391f) % 1f;
      float x = (float) (Math.sin(i * 7.13) * .48 + Math.sin(time * .6 + i) * .018);
      float y = -.53f + phase * 1.18f;
      float size = (1.2f + i % 3 * .5f) * height / 1080f;
      float alpha = (1f - phase) * .7f;
      sparks.put(x).put(y).put(size).put(alpha);
    }
    sparks.position(0);
    GLES20.glUseProgram(emberProgram);
    GLES20.glEnable(GLES20.GL_BLEND);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
    GLES20.glEnableVertexAttribArray(emberPosition);
    GLES20.glVertexAttribPointer(emberPosition, 4, GLES20.GL_FLOAT, false, 0, sparks);
    GLES20.glDrawArrays(GLES20.GL_POINTS, 0, options.emberCount);
    GLES20.glDisableVertexAttribArray(emberPosition);
    GLES20.glDisable(GLES20.GL_BLEND);
  }
}
