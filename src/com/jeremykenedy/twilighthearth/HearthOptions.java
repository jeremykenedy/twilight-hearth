package com.jeremykenedy.twilighthearth;

import java.util.Random;

public final class HearthOptions {
    public final int hearthStyle;
    public final int flameCount;
    public final int emberCount;
    public final float speed;
    public final float flameHeight;
    public final float brightness;

    private HearthOptions(int hearthStyle, int flameCount, int emberCount, float speed,
            float flameHeight, float brightness) {
        this.hearthStyle = hearthStyle;
        this.flameCount = flameCount;
        this.emberCount = emberCount;
        this.speed = speed;
        this.flameHeight = flameHeight;
        this.brightness = brightness;
    }

    public static HearthOptions resolve(String style, String intensity, String embers,
            String motion, String ambience, boolean randomizeAll, Random random) {
        String selectedStyle = choose(style, randomizeAll, random, "brick", "stone", "modern");
        String selectedIntensity = choose(intensity, randomizeAll, random, "low", "normal", "high");
        String selectedEmbers = choose(embers, randomizeAll, random, "few", "handful", "many");
        String selectedMotion = choose(motion, randomizeAll, random, "slow", "normal", "fast");
        String selectedAmbience = choose(ambience, randomizeAll, random, "dim", "normal", "bright");
        return new HearthOptions(styleFor(selectedStyle), flamesFor(selectedIntensity),
                embersFor(selectedEmbers), speedFor(selectedMotion),
                heightFor(selectedIntensity), brightnessFor(selectedAmbience));
    }

    private static String choose(String selected, boolean randomizeAll, Random random, String... values) {
        if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
        for (String value : values) if (value.equals(selected)) return selected;
        return values[0];
    }

    static int styleFor(String style) {
        if ("stone".equals(style)) return 1;
        if ("modern".equals(style)) return 2;
        return 0;
    }

    static int flamesFor(String intensity) {
        if ("low".equals(intensity)) return 4;
        if ("high".equals(intensity)) return 10;
        return 7;
    }

    static int embersFor(String embers) {
        if ("few".equals(embers)) return 14;
        if ("many".equals(embers)) return 48;
        return 24;
    }

    static float speedFor(String motion) {
        if ("slow".equals(motion)) return 0.65f;
        if ("fast".equals(motion)) return 1.55f;
        return 1f;
    }

    static float heightFor(String intensity) {
        if ("low".equals(intensity)) return 0.56f;
        if ("high".equals(intensity)) return 1.08f;
        return 0.82f;
    }

    static float brightnessFor(String ambience) {
        if ("dim".equals(ambience)) return 0.58f;
        if ("bright".equals(ambience)) return 1.15f;
        return 0.86f;
    }
}
