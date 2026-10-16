# Quality correction

## Existing release

The checked-in native 1920x1080 emulator capture shows a small firebox low on
an almost empty wall. Its orange fire is made from solid Canvas Bezier shapes
whose tips are clipped against the opening. Logs are two thick painted lines.
The brick and stone surrounds are sparse lines and rounded rectangles. The
renderer schedules frames every 50 ms, a nominal 20 fps before device timing.
These observations come from the current source and original native capture,
not a new device run.

Package identity is `com.jeremykenedy.twilighthearth`. The existing signing
certificate SHA-256 is
`a5dc46da139280e0c3358c04c5635ff5cdb70c22f065375515c6b59fedb6b74c`.
The existing production key remains in use.

## Replacement candidate

The replacement decodes licensed 1920x1080 real wood fire into a native
OpenGL surface. Photographed brick, marble stone, dark slate and timber use
normal maps and restrained reflected firelight. The firebox is larger and
centered, and the full frame moves very slowly. Embers are separate small
additive points, avoiding a full-screen loop over particles in the fragment
shader. There are no new Android runtime dependencies or network permissions.

All existing setting keys and accepted choices remain valid: `hearth_style`,
`intensity`, `embers`, `motion`, `ambience` and `randomize_all`. Intensity adjusts
recorded flame exposure and reflected light. It does not resize logs or claim
independent physical combustion control. Movement controls actual video and
ember playback rate. The native candidate now has current 1920x1080 captures of all three materials, low and high flame exposure, remote settings and offline credits. An actual DreamService binder test rendered the scene and exited with Back without changing the selected dream. A 33.369 second native recording spans the 29.25 second fire loop. The final signed APK passed bounded physical Fire TV Preview and actual DreamService/Back checks with exact preference/system restoration and normal moving playback review. Automatic idle startup, sleep/wake and sustained performance remain unverified. The bundled preview image is refreshed from the current native brick capture.

## Coverage boundary

The saved original JaCoCo report showed 47 of 47 lines and 52 of 52 branches
covered in HearthOptions and SettingsValues. Its enforcement rule accepted one
covered line/branch rather than a 100 percent ratio. The candidate corrects
both limits to COVEREDRATIO 1.0. Android framework and rendering behavior still
require native verification; the host coverage number does not cover them.

## Formatting correction

The previous Gradle project lived under `coverage/`, with Spotless target globs
that reached outside its project directory. Its apparently successful run did
not format the application source. Making the targets explicit exposed the
outside-project error. The Gradle project now lives at the repository root and
checks the actual Java source, tests and coverage tests. The first real check
required formatting existing source files. Android packaging and Gradle coverage
outputs use separate build directories.
