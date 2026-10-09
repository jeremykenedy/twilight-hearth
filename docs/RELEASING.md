# Releasing

1. Update the app version name/code and `docs/releases/vX.Y.Z.md`.
2. Run the full local test, coverage, style, documentation, privacy, and APK checks.
3. Build the signed production APK with the maintained release keystore.
4. Verify the APK package name, version, DreamService, permissions, and signing certificate. Confirm the generated `.sha256` matches the APK.
5. Commit and push the finished release candidate. Wait for every required GitHub Actions check on the exact commit to pass. If anything changes after CI, rerun it.
6. Create the SemVer tag/release and attach `build/twilight-hearth.apk` and its adjacent `.sha256` file.
7. Download both published assets and verify the checksum before treating the release as complete.

Do not publish CI APKs or reuse the release tag after a code correction. A change after release requires a new patch version and release notes. Keep the package ID, signing certificate, asset name, README release links, and installer metadata stable across updates.
