package com.jeremykenedy.twilighthearth;

public final class SettingsValues {
    private SettingsValues() {}

    public static boolean isSupported(String key, String value) {
        if (key == null || value == null) return false;
        if ("hearth_style".equals(key)) return oneOf(value, "brick", "stone", "modern", "random");
        if ("intensity".equals(key)) return oneOf(value, "low", "normal", "high", "random");
        if ("embers".equals(key)) return oneOf(value, "few", "handful", "many", "random");
        if ("motion".equals(key)) return oneOf(value, "slow", "normal", "fast", "random");
        if ("ambience".equals(key)) return oneOf(value, "dim", "normal", "bright", "random");
        if ("randomize_all".equals(key)) return oneOf(value, "true", "false");
        return false;
    }

    private static boolean oneOf(String value, String... allowed) {
        for (String option : allowed) if (option.equals(value)) return true;
        return false;
    }
}
