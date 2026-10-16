# Developer integration contract

| Field | Value |
| --- | --- |
| Product | Twilight Hearth |
| Repository and saver ID | `jeremykenedy/twilight-hearth`, `twilight-hearth` |
| Package | `com.jeremykenedy.twilighthearth` |
| Dream component | `com.jeremykenedy.twilighthearth/.HearthDreamService` |
| Settings | `com.jeremykenedy.twilighthearth/.SettingsActivity` |
| Preview activity | `com.jeremykenedy.twilighthearth/.PreviewActivity` |
| Minimum SDK | 23 |
| Version | 1.1.0, code 2 |
| APK and checksum filenames | `twilight-hearth.apk`, `twilight-hearth.apk.sha256` |
| Release target | `v1.1.0` from verified remote primary; verify publication before pinning |
| Settings schema | `content://com.jeremykenedy.twilighthearth.settings/schema` |
| Current settings | `content://com.jeremykenedy.twilighthearth.settings/settings` |

The existing six preference keys are `hearth_style`, `intensity`, `embers`, `motion`, `ambience` and
`randomize_all`. Read the schema rather than assuming a new setting exists.
Supported values, validation and defaults are unchanged in this candidate.

The production certificate SHA-256 remains
`a5dc46da139280e0c3358c04c5635ff5cdb70c22f065375515c6b59fedb6b74c`.
The signed final APK SHA-256 is `dd39810f8bf2a67af7f133554897741323d9a14d606b3fc1a441285646b440f5`, 27,207,363 bytes. Its exact physical and emulator checks are recorded in
[device verification](VERIFICATION.md). The original native default preview is
`art/screenshots/brick-natural.png`, 1920x1080, and its JPEG derivative is bundled
as `res/drawable-nodpi/screensaver_preview.jpg`.

Before refreshing an ecosystem release pin, use the final published APK and
checksum from this repository's verified release. The candidate is not a release
asset. Verify same package/signer, the BIND_DREAM_SERVICE declaration, actual
preview provenance, provider reads/writes and the target host's native selection,
settings and Back behavior. Registration belongs in Toolkit's existing reviewed
screensaver registry; UI previews follow that project's normalized component hash
and capture manifest. Do not add an arbitrary URL downloader or a second app list.

Application source is Apache-2.0, recorded fire is CC BY 3.0 and material maps are
CC0. Preserve [NOTICE](../NOTICE) and the bundled offline attribution. See
[configuration](CONFIGURATION.md) for supported provider values.
