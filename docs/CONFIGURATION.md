# Configuration

Open Twilight Hearth from the TV launcher and use the remote to change a choice. Settings persist locally and apply the next time the screensaver starts.

| Setting | Choices | Default | Effect |
| --- | --- | --- | --- |
| Hearth surround | Traditional brick, natural stone, modern dark, random | Traditional brick | Changes the wall and fireplace surround. |
| Flame intensity | Low, natural, high, random | Natural | Adjusts exposure of the recorded flames and their reflected light, without resizing the logs. |
| Floating embers | A few, a handful, many, random | A handful | Changes the number of rising embers. |
| Flame movement | Slow, natural, fast, random | Natural | Changes recorded-fire playback and ember movement to 0.65x, 1x or 1.55x. |
| Room lighting | Dim, warm, bright, random | Warm | Changes the room material brightness and reflected glow. |
| Randomize all | On, off | Off | Chooses a fresh supported value for every setting when the screensaver starts. |

Per-setting random choices affect that setting each time the screensaver starts. The random choices are selected at startup and remain fixed until the next activation.

The bottom of the settings screen opens offline credits and full licenses. These are bundled with the app and need no browser or network connection.

## Host settings interface

The exported provider is `com.jeremykenedy.twilighthearth.settings`:

- `content://com.jeremykenedy.twilighthearth.settings/schema` returns each setting key, display title, type, default, supported values, and whether random is available.
- `content://com.jeremykenedy.twilighthearth.settings/settings` returns the current key and value pairs.
- Update a setting with a `ContentValues` record containing `key` and `value` at the `settings` URI. Unsupported keys or values are rejected.

The schema cursor type is `vnd.android.cursor.dir/vnd.twilighthearth.setting-schema.v1`. The current-values cursor type is `vnd.android.cursor.dir/vnd.twilighthearth.setting.v1`.

Example read:

```bash
adb shell content query --uri content://com.jeremykenedy.twilighthearth.settings/schema
adb shell content query --uri content://com.jeremykenedy.twilighthearth.settings/settings
```

The provider lets a host discover available settings. A host still needs explicit UI and write support before it can edit them.
