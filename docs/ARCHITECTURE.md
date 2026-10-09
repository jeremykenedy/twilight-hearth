# Architecture

Twilight Hearth is a dependency-free Android app with a `DreamService`, a native settings activity, and an exported settings provider. The scene is drawn on a hardware-accelerated Canvas. The renderer animates flame paths, glow, log highlights, and embers; it uses no bundled video or external media.

`HearthOptions` resolves persisted choices into renderer values. `SettingsValues` validates provider writes against the same supported option set. Both classes are Android-independent and are covered by host-side line and branch coverage tests. Android UI, graphics, service lifecycle, and platform framework code are verified on emulator 5570 rather than included in the host coverage percentage.

The renderer schedules frames only while the dream or preview is active. Dream lifecycle callbacks start and stop animation. The app requests no runtime network permission and has no remote configuration, background service, wake lock, or external runtime library.
