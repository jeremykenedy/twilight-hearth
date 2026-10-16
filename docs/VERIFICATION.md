# Device verification

## Current source candidate

On October 16, 2026, the real-fire replacement was tested on shared Google TV
emulator `emulator-5580`, Android 14/API 34, with actual 1920x1080 captures.
The native test APK SHA-256 was
`89bf3132418d506c6307c9ee0a89b56c8dba4ccee387138d76ff07a99bb8d9ea`
(26,814,147 bytes). The final source candidate refreshes only the embedded
preview JPEG from that run; its SHA-256 is
`dd39810f8bf2a67af7f133554897741323d9a14d606b3fc1a441285646b440f5`
(27,207,363 bytes). Both use the existing production signing certificate.

The native run verified:

- Installation and real 1080p decoded-fire playback in PreviewActivity.
- All three surrounds and low/high recorded-flame exposure, with unchanged logs and frame geometry.
- Settings reached through the launcher activity and the existing settings provider.
- D-pad navigation to Credits and licenses, Select opening the native dialog,
  Up/Down scrolling through source attribution into the full Apache license,
  and Back closing it.
- Actual DreamService activation through Android's testDream binder method and
  Back exiting the dream. This did not change the selected system dream.
- No FATAL EXCEPTION, MediaPlayer error or GL error in the inspected native log.

The default brick capture was taken after more than 40 seconds of playback.
The 33.369 second native recording contains 1,080 compositor frames and spans
the 29.25 second recorded-fire loop. These capture counts are not a renderer
frame-rate measurement. Physical-device performance has not been established.

Current captures live in `art/screenshots/`. The embedded Android screensaver
preview is the same current brick capture encoded as JPEG, without captions,
menus or watermarks. Earlier procedural captures under `docs/screenshots/`
are historical and are no longer used in the README or app preview.

## Restoration

The selected system dream remained Reel Atrium and `screensaver_enabled`
remained 1, confirmed by readback. Twilight Hearth's new-install defaults were
restored, both tested fireplace apps were stopped, and Home was restored before
the emulator was handed to the next worker. No physical TV commands were issued
as part of this candidate's checks.

## Historical release

Version 1.0.0 used the original Canvas renderer. Its Android TV API 31 emulator
5570 checks covered installation, launcher settings, provider reads and preview
captures. Those results do not verify this replacement renderer or a physical
Fire TV.

## Remaining gates

Fire TV hardware installation, actual system idle activation, sleep/wake,
remote Menu/Home paths, sustained playback, thermal behavior and native 4K
composition remain unverified for this candidate. A direct PreviewActivity or
testDream launch does not establish automatic idle activation. The replacement
is not released until the required device and publication gates finish.
