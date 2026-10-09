# Device verification

## Tested device

| Device | Type | Resolution | Test |
| --- | --- | --- | --- |
| Android TV emulator 5570 (`sdk_google_atv64_arm64`) | Emulator | 1920x1080, density 320 dpi, Android 12 (API 31) | APK install/update, launcher settings, settings-provider schema and values, D-pad option selection, animated preview, DreamService manifest discovery, and screenshot capture. |
| Fire TV | Not tested | Not available | A physical Fire TV owner is requested to test installation, screensaver selection/start/exit, settings navigation, and update behavior, then report the model, Fire OS version, and results. |
| Google TV | Not tested | Not available | A physical Google TV owner is requested to test installation, screensaver selection/start/exit, settings navigation, and update behavior, then report the model, Android version, and results. |

The emulator reports Android TV system images through the Android SDK. Use `adb -s emulator-5570 shell getprop ro.build.version.release` and `getprop ro.build.version.sdk` to identify the installed image version. This release was not tested on physical Fire TV, Android TV, or Google TV hardware. Native 4K composition, long-duration thermal behavior, panel power use, and vendor-specific idle activation remain unverified.

## Screenshots

[`screenshots/hearth-preview.png`](screenshots/hearth-preview.png) is a 1920x1080 ADB capture of the running `PreviewActivity` on emulator 5570. It was captured after the animation had been running for at least 40 seconds, with the default traditional-brick surround, natural intensity, a handful of embers, natural movement, and warm room lighting. It contains no menus, captions, or watermarks.

The Android DreamService preview image is derived from this same original procedural scene and bundled at `res/drawable-nodpi/screensaver_preview.jpg`.

## Checks performed

- Installed and updated the signed local APK through `install.py` using only serial `emulator-5570`.
- Confirmed the launcher resolves to `SettingsActivity` and the DreamService component is discoverable in Android TV's screensaver picker.
- Queried the settings schema and current values through the exported provider.
- Captured and visually inspected the running 1920x1080 animated preview after settling.
- Confirmed the installed manifest declares no network permissions.

## Device test reports requested

Physical Fire TV and Google TV device tests are still needed. If you can test either platform, please report the device model, OS version, display resolution, install method, screensaver activation and exit behavior, D-pad settings behavior, and any visible rendering or performance issue. Include whether the result came from a physical device or emulator. Do not include account identifiers or other private device data.

## Limits

The emulator verifies Android app behavior, not automatic idle startup through Android TV's system controls, Fire OS rollback behavior, physical-panel performance, or native 4K composition. The app's preview and service declaration were verified, but system-triggered DreamService activation was not established. No system screensaver choice or timer was left changed after testing.
