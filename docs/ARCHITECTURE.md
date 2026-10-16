# Architecture

Twilight Hearth uses a native Android DreamService, settings activity and exported settings provider. A GLSurfaceView draws an OpenGL ES 2.0 scene. MediaPlayer decodes the bundled 1920x1080, 30 fps wood-fire recording into a SurfaceTexture. The recorded clip loops locally; its attribution and checksum are in `assets/media.json`.

The fragment shader combines the recording with photographed brick, stone, slate and timber, including normal maps and restrained reflected firelight. A separate additive point pass draws floating embers. The architecture moves very slowly across the viewport. Recorded-fire exposure and playback speed change independently of room brightness and ember count.

`HearthOptions` resolves the existing six persisted choices into renderer values. `SettingsValues` validates provider writes against the same supported option set. Both classes are Android-independent and covered by host-side line and branch coverage tests. Android UI, decoder, graphics and service lifecycle code require native verification and are excluded from that percentage.

Dream and preview callbacks start and stop playback. MediaPlayer, SurfaceTexture and GL resources are released when the surface stops. The app has no network permission, remote configuration, background service, wake lock or external runtime library. Source credits and full licenses are bundled in the APK and accessible from settings.
