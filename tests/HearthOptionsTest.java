package com.jeremykenedy.twilighthearth;

import java.util.Random;

public final class HearthOptionsTest {
    public static void main(String[] args) {
        verifyResolvedValues();
        verifyRandomChoices();
        verifyFallbacks();
        verifySettingsValidation();
        System.out.println("Hearth settings tests passed.");
    }

    private static void verifyResolvedValues() {
        HearthOptions options = HearthOptions.resolve("brick", "normal", "handful", "normal", "normal", false,
                new Random(1));
        check(options.hearthStyle == 0 && options.flameCount == 7 && options.emberCount == 24,
                "natural hearth values");
        check(options.speed == 1f && options.flameHeight == 0.82f && options.brightness == 0.86f,
                "natural movement and lighting");
        options = HearthOptions.resolve("stone", "low", "few", "slow", "dim", false, new Random(2));
        check(options.hearthStyle == 1 && options.flameCount == 4 && options.emberCount == 14,
                "low intensity stone scene");
        check(options.speed == 0.65f && options.flameHeight == 0.56f && options.brightness == 0.58f,
                "slow dim scene");
        options = HearthOptions.resolve("modern", "high", "many", "fast", "bright", false, new Random(3));
        check(options.hearthStyle == 2 && options.flameCount == 10 && options.emberCount == 48,
                "high intensity modern scene");
        check(options.speed == 1.55f && options.flameHeight == 1.08f && options.brightness == 1.15f,
                "fast bright scene");
    }

    private static void verifyRandomChoices() {
        HearthOptions options = HearthOptions.resolve("random", "random", "random", "random", "random", false,
                new FixedRandom(1));
        check(options.hearthStyle == 1 && options.flameCount == 7 && options.emberCount == 24,
                "each random setting resolves to a supported value");
        check(options.speed == 1f && options.flameHeight == 0.82f && options.brightness == 0.86f,
                "random choices resolve consistently");
        options = HearthOptions.resolve("brick", "low", "few", "slow", "dim", true, new FixedRandom(2));
        check(options.hearthStyle == 2 && options.flameCount == 10 && options.emberCount == 48,
                "randomize all uses each setting range");
        check(options.speed == 1.55f && options.flameHeight == 1.08f && options.brightness == 1.15f,
                "randomize all resolves all settings");
    }

    private static void verifyFallbacks() {
        check(HearthOptions.styleFor("unknown") == 0, "unknown style uses brick");
        check(HearthOptions.flamesFor("unknown") == 7, "unknown intensity uses natural");
        check(HearthOptions.embersFor("unknown") == 24, "unknown ember count uses handful");
        check(HearthOptions.speedFor("unknown") == 1f, "unknown motion uses natural");
        check(HearthOptions.heightFor("unknown") == 0.82f, "unknown height uses natural");
        check(HearthOptions.brightnessFor("unknown") == 0.86f, "unknown ambience uses warm");
        HearthOptions options = HearthOptions.resolve("invalid", "invalid", "invalid", "invalid", "invalid", false,
                new Random(4));
        check(options.hearthStyle == 0 && options.flameCount == 4 && options.emberCount == 14,
                "invalid selections use valid defaults");
        check(options.speed == 0.65f && options.flameHeight == 0.56f && options.brightness == 0.58f,
                "invalid visual selections use valid defaults");
    }

    private static void verifySettingsValidation() {
        check(!SettingsValues.isSupported(null, "brick"), "null key rejected");
        check(!SettingsValues.isSupported("hearth_style", null), "null value rejected");
        values("hearth_style", new String[] {"brick", "stone", "modern", "random"});
        values("intensity", new String[] {"low", "normal", "high", "random"});
        values("embers", new String[] {"few", "handful", "many", "random"});
        values("motion", new String[] {"slow", "normal", "fast", "random"});
        values("ambience", new String[] {"dim", "normal", "bright", "random"});
        values("randomize_all", new String[] {"true", "false"});
        check(!SettingsValues.isSupported("unknown", "value"), "unknown setting rejected");
        check(!SettingsValues.isSupported("hearth_style", "glass"), "invalid style rejected");
        check(!SettingsValues.isSupported("intensity", "extreme"), "invalid intensity rejected");
        check(!SettingsValues.isSupported("embers", "none"), "invalid embers rejected");
        check(!SettingsValues.isSupported("motion", "instant"), "invalid motion rejected");
        check(!SettingsValues.isSupported("ambience", "blue"), "invalid ambience rejected");
        check(!SettingsValues.isSupported("randomize_all", "yes"), "invalid toggle rejected");
    }

    private static void values(String key, String[] values) {
        for (String value : values) check(SettingsValues.isSupported(key, value), key + " accepts " + value);
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }

    private static final class FixedRandom extends Random {
        private final int value;

        FixedRandom(int value) {
            this.value = value;
        }

        @Override
        public int nextInt(int bound) {
            return Math.min(value, bound - 1);
        }
    }
}
