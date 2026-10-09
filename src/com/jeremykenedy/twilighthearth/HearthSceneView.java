package com.jeremykenedy.twilighthearth;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;

import java.util.Random;

final class HearthSceneView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random(78031L);
    private final Ember[] embers = new Ember[72];
    private final Path flamePath = new Path();
    private final Path corePath = new Path();
    private Bitmap roomBitmap;
    private HearthOptions options;
    private LinearGradient wallGradient;
    private LinearGradient fireGradient;
    private LinearGradient coreGradient;
    private RadialGradient glowGradient;
    private long startedAt;
    private boolean running;

    HearthSceneView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        for (int i = 0; i < embers.length; i++) {
            embers[i] = new Ember(random.nextFloat(), random.nextFloat(), 0.4f + random.nextFloat() * 1.9f,
                    random.nextFloat() * 6.28f);
        }
        loadOptions();
    }

    void start() {
        if (!running) {
            running = true;
            startedAt = SystemClock.uptimeMillis();
            postInvalidateOnAnimation();
        }
    }

    void stop() {
        running = false;
        removeCallbacks(invalidator);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (roomBitmap == null && getWidth() > 0 && getHeight() > 0) createRoomBitmap(getWidth(), getHeight());
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        if (roomBitmap != null) {
            roomBitmap.recycle();
            roomBitmap = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() <= 0 || getHeight() <= 0) return;
        float time = (SystemClock.uptimeMillis() - startedAt) / 1000f;
        drawRoom(canvas);
        drawFireplace(canvas, time);
        drawEmbers(canvas, time);
        if (running) postDelayed(invalidator, 50L);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (width <= 0 || height <= 0) return;
        int[] wall = options.hearthStyle == 1
                ? new int[] {0xff373632, 0xff181715}
                : options.hearthStyle == 2 ? new int[] {0xff242326, 0xff111013}
                : new int[] {0xff3a2018, 0xff1d100e};
        wallGradient = new LinearGradient(0, 0, 0, height, wall[0], wall[1], Shader.TileMode.CLAMP);
        fireGradient = new LinearGradient(0, height * 0.22f, 0, height * 0.88f,
                new int[] {0x00ff4012, 0x28a92a0d, 0xffed4a0c, 0xffff991d, 0xffffdc78},
                new float[] {0f, 0.2f, 0.52f, 0.85f, 1f}, Shader.TileMode.CLAMP);
        coreGradient = new LinearGradient(0, height * 0.66f, 0, height * 0.87f,
                new int[] {0x00ffe28a, 0x99ffb83f, 0xffffe9a0}, new float[] {0f, 0.42f, 1f},
                Shader.TileMode.CLAMP);
        glowGradient = new RadialGradient(width * 0.5f, height * 0.72f, width * 0.45f,
                new int[] {0x59ff6419, 0x27da3b12, 0x00c82d0e}, new float[] {0f, 0.46f, 1f},
                Shader.TileMode.CLAMP);
        createRoomBitmap(width, height);
    }

    private final Runnable invalidator = new Runnable() {
        @Override
        public void run() {
            if (running) invalidate();
        }
    };

    private void loadOptions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        options = HearthOptions.resolve(preferences.getString("hearth_style", "brick"),
                preferences.getString("intensity", "normal"), preferences.getString("embers", "handful"),
                preferences.getString("motion", "normal"), preferences.getString("ambience", "normal"),
                preferences.getBoolean("randomize_all", false), new Random(System.currentTimeMillis()));
    }

    private void createRoomBitmap(int width, int height) {
        if (roomBitmap != null) roomBitmap.recycle();
        roomBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roomBitmap);
        paint.setShader(wallGradient);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setShader(null);
        if (options.hearthStyle == 0) drawBricks(canvas, width, height);
        else if (options.hearthStyle == 1) drawStone(canvas, width, height);
        drawMantel(canvas, width, height);
        drawFireplaceShell(canvas, width, height);
        paint.setShader(null);
        paint.setAlpha(255);
    }

    private void drawRoom(Canvas canvas) {
        if (roomBitmap != null) canvas.drawBitmap(roomBitmap, 0, 0, paint);
    }

    private void drawBricks(Canvas canvas, int width, int height) {
        float blockW = width * 0.16f;
        float blockH = height * 0.09f;
        paint.setColor(0x27351a16);
        paint.setStrokeWidth(Math.max(1f, getHeight() * 0.004f));
        for (int row = 0; row < 7; row++) {
            float y = row * blockH;
            float offset = row % 2 == 0 ? 0 : blockW * 0.5f;
            for (float x = -blockW + offset; x < width; x += blockW) {
                canvas.drawLine(x, y, x + blockW, y, paint);
                canvas.drawLine(x, y, x, y + blockH, paint);
            }
        }
    }

    private void drawStone(Canvas canvas, int width, int height) {
        paint.setColor(0x1e080807);
        for (int row = 0; row < 5; row++) {
            float y = row * height * 0.13f;
            float offset = row % 2 == 0 ? 0 : width * 0.12f;
            for (float x = offset; x < width; x += width * 0.24f) {
                canvas.drawRoundRect(x + 3, y + 3, x + width * 0.23f, y + height * 0.12f,
                        height * 0.012f, height * 0.012f, paint);
            }
        }
    }

    private void drawMantel(Canvas canvas, int width, int height) {
        float left = width * 0.14f;
        float right = width * 0.86f;
        float top = height * 0.43f;
        paint.setColor(options.hearthStyle == 2 ? 0xff171719 : 0xff30221d);
        canvas.drawRect(left, top, right, top + height * 0.065f, paint);
        paint.setColor(0x557c6251);
        canvas.drawRect(left, top, right, top + height * 0.012f, paint);
    }

    private void drawFireplaceShell(Canvas canvas, int width, int height) {
        float left = width * 0.23f;
        float right = width * 0.77f;
        float top = height * 0.50f;
        float bottom = height * 0.91f;
        paint.setColor(options.hearthStyle == 2 ? 0xff111113 : 0xff191412);
        canvas.drawRoundRect(left, top, right, bottom, height * 0.025f, height * 0.025f, paint);
        paint.setColor(0xff080706);
        canvas.drawRect(left + width * 0.024f, top + height * 0.02f,
                right - width * 0.024f, bottom - height * 0.035f, paint);
        paint.setColor(0xff241d18);
        canvas.drawRect(width * 0.19f, bottom, width * 0.81f, bottom + height * 0.045f, paint);
    }

    private void drawFireplace(Canvas canvas, float time) {
        float width = getWidth();
        float height = getHeight();
        float left = width * 0.23f;
        float right = width * 0.77f;
        float top = height * 0.50f;
        float bottom = height * 0.91f;
        float floor = height * 0.855f;
        drawGlow(canvas, time, left, right, top, floor);
        int saveCount = canvas.save();
        canvas.clipRect(left + width * 0.04f, top + height * 0.025f,
                right - width * 0.04f, floor + height * 0.015f);
        for (int i = 0; i < options.flameCount; i++) drawFlame(canvas, time, i, left, right, floor);
        canvas.restoreToCount(saveCount);
        paint.setShader(null);
        drawLogs(canvas, time, left, right, floor);
    }

    private void drawGlow(Canvas canvas, float time, float left, float right, float top, float floor) {
        float pulse = 0.72f + 0.13f * (float) Math.sin(time * 3.1f * options.speed)
                + 0.08f * (float) Math.sin(time * 5.7f * options.speed);
        paint.setAlpha(Math.min(255, (int) (255 * pulse * options.brightness)));
        paint.setShader(glowGradient);
        canvas.drawRect(left + getWidth() * 0.024f, top + getHeight() * 0.02f,
                right - getWidth() * 0.024f, floor, paint);
        paint.setShader(null);
        paint.setAlpha(255);
    }

    private void drawLogs(Canvas canvas, float time, float left, float right, float floor) {
        float width = right - left;
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(getHeight() * 0.052f);
        paint.setColor(0xff24100b);
        canvas.drawLine(left + width * 0.19f, floor - getHeight() * 0.045f,
                right - width * 0.19f, floor - getHeight() * 0.035f, paint);
        paint.setColor(0xff2b140d);
        canvas.drawLine(left + width * 0.32f, floor - getHeight() * 0.09f,
                right - width * 0.31f, floor - getHeight() * 0.015f, paint);
        paint.setStrokeWidth(getHeight() * 0.009f);
        paint.setColor(withAlpha(0xffdf5e1e, 50 + (int) (35 * Math.sin(time * 3.6f * options.speed))));
        canvas.drawLine(left + width * 0.23f, floor - getHeight() * 0.063f,
                right - width * 0.2f, floor - getHeight() * 0.052f, paint);
        canvas.drawLine(left + width * 0.35f, floor - getHeight() * 0.102f,
                right - width * 0.34f, floor - getHeight() * 0.031f, paint);
        paint.setStrokeCap(Paint.Cap.BUTT);
    }

    private void drawFlame(Canvas canvas, float time, int index, float left, float right, float floor) {
        float width = right - left;
        float x = left + width * (0.14f + 0.72f * (index + 0.5f) / options.flameCount);
        float phase = index * 1.71f;
        float sway = (float) Math.sin(time * (1.7f + index % 3 * 0.23f) * options.speed + phase);
        float pulse = 0.84f + 0.16f * (float) Math.sin(time * 4.2f * options.speed + phase);
        float baseWidth = width * (0.13f + (index % 4) * 0.012f);
        float tipX = x + sway * baseWidth * 0.5f;
        float rise = Math.min(getHeight() * options.flameHeight * pulse * (0.25f + (index % 3) * 0.05f),
                getHeight() * 0.30f);
        float tipY = floor - rise;
        flamePath.reset();
        Path flame = flamePath;
        flame.moveTo(x - baseWidth * 0.54f, floor);
        flame.cubicTo(x - baseWidth * 0.76f, floor - getHeight() * 0.10f,
                x - baseWidth * (0.27f + sway * 0.08f), floor - getHeight() * 0.27f,
                x - baseWidth * (0.43f - sway * 0.07f), floor - getHeight() * 0.41f);
        flame.cubicTo(tipX - baseWidth * 0.38f, tipY + getHeight() * 0.13f,
                tipX - baseWidth * 0.11f, tipY + getHeight() * 0.055f, tipX, tipY);
        flame.cubicTo(tipX + baseWidth * 0.15f, tipY + getHeight() * 0.17f,
                x + baseWidth * (0.32f + sway * 0.06f), floor - getHeight() * 0.28f,
                x + baseWidth * (0.44f + sway * 0.08f), floor - getHeight() * 0.13f);
        flame.cubicTo(x + baseWidth * 0.63f, floor - getHeight() * 0.07f,
                x + baseWidth * 0.58f, floor - getHeight() * 0.025f, x + baseWidth * 0.50f, floor);
        flame.close();
        paint.setAlpha(Math.min(255, (int) (220 * options.brightness)));
        paint.setShader(fireGradient);
        canvas.drawPath(flame, paint);
        paint.setShader(null);
        paint.setAlpha(255);

        corePath.reset();
        Path core = corePath;
        float coreWidth = baseWidth * 0.42f;
        float coreTipY = floor - (floor - tipY) * (0.24f + (index % 2) * 0.035f);
        core.moveTo(x - coreWidth * 0.48f, floor);
        core.cubicTo(x - coreWidth * 0.42f, floor - getHeight() * 0.045f,
                x - coreWidth * 0.2f, coreTipY + getHeight() * 0.05f, x, coreTipY);
        core.cubicTo(x + coreWidth * 0.2f, coreTipY + getHeight() * 0.05f,
                x + coreWidth * 0.46f, floor - getHeight() * 0.055f,
                x + coreWidth * 0.46f, floor);
        core.close();
        paint.setAlpha((int) (210 * options.brightness));
        paint.setShader(coreGradient);
        canvas.drawPath(core, paint);
        paint.setShader(null);
        paint.setAlpha(255);
    }

    private void drawEmbers(Canvas canvas, float time) {
        for (int i = 0; i < options.emberCount; i++) {
            Ember ember = embers[i];
            float travel = (time * (0.025f + ember.size * 0.009f) * options.speed + ember.y) % 0.46f;
            float x = getWidth() * (0.28f + ember.x * 0.44f)
                    + (float) Math.sin(time * 0.8f + ember.phase) * getWidth() * 0.012f;
            float y = getHeight() * (0.86f - travel);
            float pulse = 0.5f + 0.5f * (float) Math.sin(time * 2.5f + ember.phase);
            paint.setColor(withAlpha(0xffffb147, (int) (80 + pulse * 160)));
            canvas.drawCircle(x, y, ember.size * (0.8f + pulse * 0.5f), paint);
        }
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00ffffff) | (Math.max(0, Math.min(255, alpha)) << 24);
    }

    private static final class Ember {
        final float x;
        final float y;
        final float size;
        final float phase;

        Ember(float x, float y, float size, float phase) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.phase = phase;
        }
    }
}
