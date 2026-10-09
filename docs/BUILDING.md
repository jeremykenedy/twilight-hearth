# Building from source

## Requirements

- JDK 17 or later.
- Android SDK platform 36 and build-tools 36.0.0.
- `aapt2`, `d8`, `zipalign`, `apksigner`, `javac`, `keytool`, `openssl`, and `zip` available at the standard SDK paths or in `PATH`.
- Python 3 for installer and documentation checks.

Build a signed APK and checksum:

```bash
bash build.sh
```

The first build creates a unique RSA signing key at `~/.android/twilight-hearth.jks` and a private password file beside it. Back up both files securely; the key must be preserved for compatible updates. The files are ignored by Git. To use another protected key, set `TWILIGHT_HEARTH_KEYSTORE` and `TWILIGHT_HEARTH_KEYPASS`. Do not commit or publish the key or password.

The output is `build/twilight-hearth.apk`, accompanied by `build/twilight-hearth.apk.sha256`. Set `VERSION_NAME` and `VERSION_CODE` when building a later version. Local release builds must use the maintained release keystore. CI uses disposable credentials for verification and its APK is not a production release.
