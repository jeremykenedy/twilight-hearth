# Contributing

Contributions should keep the app focused on an animated, offline fireplace screensaver for TV devices.

Before submitting a change:

1. Read the [architecture](docs/ARCHITECTURE.md), [configuration](docs/CONFIGURATION.md), and [testing](docs/TESTING.md) guides.
2. Run `bash test.sh`, `bash scripts/test-python-coverage.sh`, and `bash scripts/test-coverage.sh`.
3. Run `python3 scripts/check-docs.py`, `python3 scripts/check-privacy.py`, and `bash scripts/check-style.sh`.
4. For Android changes, build and verify the app on an Android TV emulator or device. Record the device, OS, resolution, settings, and limitations.

Keep changes within scope. Do not add ads, analytics, tracking, diagnostic reporting, runtime network access, unrelated dependencies, or any app behavior that sends data off-device. Use original visual work or assets with clear redistribution rights, preserve required notices, and do not include text or watermarks in the screensaver scene. Keep the Android package and signing identity stable for updates.

By contributing, you agree that your contribution is licensed under the repository's Apache License 2.0.
