# Testing

Run the host-side tests and coverage gates:

```bash
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
```

The Java tests exercise setting validation, safe fallbacks, every supported style/intensity/ember/speed/lighting value, and deterministic random selection. JaCoCo enforces 100% line and branch coverage for `HearthOptions` and `SettingsValues`, the app-owned logic that can run without Android. The installer suite verifies URL and redirect allowlists, checksum validation, response limits, safe serial selection, confirmation rules, install failures, and temporary-file cleanup. Its coverage gate also requires 100% line and branch coverage.

Android framework graphics and lifecycle code is not included in host coverage. Verify the built app on an Android TV emulator or device: install/update, open settings with a remote, write/read settings through the provider, preview animation, activate the DreamService, leave it with Back, and check logcat for crashes. The verified emulator matrix and precise limits are recorded in [device verification](VERIFICATION.md).

CI also builds and inspects the APK, checks that runtime network permissions are absent, compiles Python sources, validates README links, and runs Spotless formatting checks.
