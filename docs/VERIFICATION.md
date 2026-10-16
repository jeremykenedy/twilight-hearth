# Device verification

## Version 1.1.0 native evidence

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
frame-rate measurement. The physical-device sample below establishes a bounded playback check, not sustained thermal performance.

Current captures live in `art/screenshots/`. The embedded Android screensaver
preview is the same current brick capture encoded as JPEG, without captions,
menus or watermarks. Earlier procedural captures under `docs/screenshots/`
are historical and are no longer used in the README or app preview.

## Restoration

The selected system dream remained Reel Atrium and `screensaver_enabled`
remained 1, confirmed by readback. Twilight Hearth's new-install defaults were
restored, both tested fireplace apps were stopped, and Home was restored before
the emulator was handed to the next worker. The emulator checks did not write to the physical TV; the separate physical check is recorded below.

## Historical release

Version 1.0.0 used the original Canvas renderer. Its Android TV API 31 emulator
5570 checks covered installation, launcher settings, provider reads and preview
captures. Those results do not verify this replacement renderer or a physical
Fire TV.

## Physical Fire TV

On October 16, 2026, the exact signed final APK `dd39810f8bf2a67af7f133554897741323d9a14d606b3fc1a441285646b440f5`, 27,207,363 bytes, version 1.1.0/code 2, was tested on an AFTDEC012E/API30 Fire TV. The same-signer update preserved all six existing preferences.

The actual remote Settings path opened Preview, ran the modern surround with bright/lively settings after a forty-second settle, and returned with Back. The 19.4756-second native movie was viewed in normal playback from start to end: recorded flames and reflected light changed continuously, with no empty firebox or incidental watermark. A nonpersistent bound HearthDreamService ran the stone/dim preset after a forty-five-second settle; Back detached the dream. Both current physical screenshots are in the [gallery](GALLERY.md).

The sampled SurfaceView presented 126 buffers over an interval corresponding to 49.0187 buffer presents/second. That is a compositor observation, not source-video FPS or a sustained rendering benchmark. Captures are 1920x1080; native 4K composition was not established. All six app preferences, five sampled system settings and the prior asleep state were restored exactly, with readback confirming Asleep=true. [Capture provenance](../art/screenshots/physical-provenance.json) records the tested artifact and scope.

## Remaining limits

Automatic idle startup, reboot/sleep-wake persistence, remote Menu behavior, sustained playback/thermal/power behavior and native 4K composition remain unverified. Direct Preview or testDream activation does not establish automatic idle activation. Release publication additionally requires current remote-primary CI and a matching verified tag.
