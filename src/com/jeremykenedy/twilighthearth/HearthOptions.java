package com.jeremykenedy.twilighthearth;

import java.util.Random;

public final class HearthOptions {
  public final int hearthStyle;
  public final float fireExposure;
  public final int emberCount;
  public final float speed;
  public final float brightness;

  private HearthOptions(
      int hearthStyle, float fireExposure, int emberCount, float speed, float brightness) {
    this.hearthStyle = hearthStyle;
    this.fireExposure = fireExposure;
    this.emberCount = emberCount;
    this.speed = speed;
    this.brightness = brightness;
  }

  public static HearthOptions resolve(
      String style,
      String intensity,
      String embers,
      String motion,
      String ambience,
      boolean randomizeAll,
      Random random) {
    String selectedStyle = choose(style, randomizeAll, random, "brick", "stone", "modern");
    String selectedIntensity = choose(intensity, randomizeAll, random, "low", "normal", "high");
    String selectedEmbers = choose(embers, randomizeAll, random, "few", "handful", "many");
    String selectedMotion = choose(motion, randomizeAll, random, "slow", "normal", "fast");
    String selectedAmbience = choose(ambience, randomizeAll, random, "dim", "normal", "bright");
    return new HearthOptions(
        styleFor(selectedStyle),
        exposureFor(selectedIntensity),
        embersFor(selectedEmbers),
        speedFor(selectedMotion),
        brightnessFor(selectedAmbience));
  }

  private static String choose(
      String selected, boolean randomizeAll, Random random, String... values) {
    if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
    for (String value : values) if (value.equals(selected)) return selected;
    return values[0];
  }

  static int styleFor(String style) {
    if ("stone".equals(style)) return 1;
    if ("modern".equals(style)) return 2;
    return 0;
  }

  static float exposureFor(String intensity) {
    if ("low".equals(intensity)) return 0.72f;
    if ("high".equals(intensity)) return 1.25f;
    return 1f;
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

  static float brightnessFor(String ambience) {
    if ("dim".equals(ambience)) return 0.58f;
    if ("bright".equals(ambience)) return 1.15f;
    return 0.86f;
  }
}
