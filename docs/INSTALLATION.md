# Installation

The standalone installer downloads the latest stable release APK and its `.sha256` file from this repository, verifies the APK bytes, then installs or updates Twilight Hearth with ADB. Downloads are initiated only when the installer runs. It sends no device or usage data. The app itself has no network permission.

## Install or update the latest release

Connect the TV with ADB and specify its serial:

```bash
python3 install.py --serial TV_IP:5555
```

Use `--yes` for a scripted install. If multiple devices are connected, `--serial` is required. The installer does not select the screensaver, change timers, or modify operating-system update or sleep settings. After installation, choose Twilight Hearth in the device's screensaver settings.

## Install a local build

```bash
bash build.sh
python3 install.py --serial TV_IP:5555 --apk build/twilight-hearth.apk
```

## Remove the app

Interactive removal asks for confirmation:

```bash
python3 install.py --serial TV_IP:5555 --uninstall
```

For unattended removal, both flags are required:

```bash
python3 install.py --serial TV_IP:5555 --uninstall --yes --force
```

Android removes the app and its local preferences. The installer does not alter the TV's system screensaver selection, timers, update behavior, or sleep configuration.

## Manual APK installation

Download `twilight-hearth.apk` and `twilight-hearth.apk.sha256` from the [latest release](https://github.com/jeremykenedy/twilight-hearth/releases/latest), then verify the checksum in the download directory:

```bash
shasum -a 256 -c twilight-hearth.apk.sha256
adb -s TV_IP:5555 install -r twilight-hearth.apk
```
