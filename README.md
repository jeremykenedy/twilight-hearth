<p align="center">
  <a href="https://github.com/jeremykenedy/twilight-hearth#gh-light-mode-only"><picture><source media="(max-width: 600px) and (prefers-reduced-motion: reduce)" srcset="art/banner-mobile-still-light.bb5d13fe765e.svg"><source media="(prefers-reduced-motion: reduce)" srcset="art/banner-still-light.348ea3cac110.svg"><source media="(max-width: 600px)" srcset="art/banner-mobile-light.c1c623d6d0e4.svg"><img src="art/banner-light.f0596f34d32d.svg#gh-light-mode-only" alt="Twilight Hearth" width="800"></picture></a>
  <a href="https://github.com/jeremykenedy/twilight-hearth#gh-dark-mode-only"><picture><source media="(max-width: 600px) and (prefers-reduced-motion: reduce)" srcset="art/banner-mobile-still-dark.60eca254883c.svg"><source media="(prefers-reduced-motion: reduce)" srcset="art/banner-still-dark.e6398aeb0d35.svg"><source media="(max-width: 600px)" srcset="art/banner-mobile-dark.5d185b657645.svg"><img src="art/banner-dark.60e6f04a9f59.svg#gh-dark-mode-only" alt="Twilight Hearth" width="800"></picture></a>
</p>

<p align="center">Real wood fire and quiet architectural surrounds for Fire TV, Android TV, and Google TV.</p>

<p align="center">
    <a href="https://github.com/jeremykenedy/twilight-hearth/releases"><img src="https://img.shields.io/github/downloads/jeremykenedy/twilight-hearth/total" alt="GitHub release downloads"></a>
    <a href="https://github.com/jeremykenedy/twilight-hearth/releases"><img src="https://img.shields.io/github/v/release/jeremykenedy/twilight-hearth?label=latest%20release" alt="Latest release"></a>
    <a href="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/ci.yml"><img src="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/ci.yml/badge.svg" alt="CI"></a>
    <a href="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/style.yml"><img src="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/style.yml/badge.svg" alt="Code style"></a>
    <a href="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/docs.yml"><img src="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/docs.yml/badge.svg" alt="Documentation"></a>
    <a href="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/security.yml"><img src="https://github.com/jeremykenedy/twilight-hearth/actions/workflows/security.yml/badge.svg" alt="Security checks"></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache--2.0-blue.svg" alt="Apache-2.0 license"></a>
</p>

<p align="center">
    <a href="https://github.com/jeremykenedy"><img src="https://img.shields.io/github/followers/jeremykenedy?label=Follow&style=social" alt="Follow Jeremy Kenedy on GitHub"></a>
    <a href="https://github.com/jeremykenedy/twilight-hearth" title="Open the repository and click Star"><img src="https://img.shields.io/badge/Star-this%20repo-yellow?logo=github&style=social" alt="Star this repo"></a>
    <a href="https://github.com/sponsors/jeremykenedy"><img src="https://img.shields.io/badge/Sponsor-jeremykenedy-EA4AAA?logo=githubsponsors&logoColor=white" alt="Sponsor Jeremy Kenedy"></a>
</p>

<p align="center">Show some love by starring this repository on GitHub.</p>

## Table of contents

- [Privacy](#privacy)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Screenshots](#screenshots)
- [Building and testing](#building-and-testing)
- [Documentation](#documentation)
- [Release notes](#release-notes)
- [Project policies](#project-policies)
- [License](#license)

## Privacy

The app requests no network permission and makes no network requests. It includes no ads, analytics, telemetry, crash reporting, or tracking. The standalone installer contacts GitHub only when you ask it to download a release APK and checksum. It sends no device or usage data.

## Features

The source candidate for version 1.1.0 replaces the original painted fire. Its native verification is in progress; the latest published release still contains the earlier renderer.

- Licensed 1080p recorded wood fire, separate rising embers and reflected room light.
- Three photographed material surrounds: traditional brick, natural stone and modern dark, with normal-mapped surfaces.
- Independent recorded-flame exposure, ember count, playback speed and room lighting, plus very slow architectural drift.
- Per-setting random values and an option to randomize all settings whenever the screensaver starts.
- Remote-friendly settings and an app-owned provider schema for host applications.
- A native Android DreamService with an in-app animation preview.

## Requirements

- Android 6.0 (API 23) or later.
- A device that exposes Android's DreamService screensaver settings.
- Android SDK platform 36 and build-tools 36.0.0 to build from source.

Native support has been tested on the Android TV emulator listed in [device verification](docs/VERIFICATION.md). Fire TV and Google TV hardware have not been tested for this release.

## Installation

Connect the TV or emulator with ADB and run `python3 install.py --serial DEVICE`. The installer downloads the latest signed release, verifies its published SHA-256 checksum, and installs or updates the app. Select Twilight Hearth in the device's screensaver settings afterward. The installer does not change system settings.

To remove the app, run `python3 install.py --serial DEVICE --uninstall --yes --force`. See [installation](docs/INSTALLATION.md) for local APK installation and safety details.

## Configuration

Open Twilight Hearth from the TV launcher and use the remote to adjust settings. Changes take effect the next time the screensaver starts. See [configuration](docs/CONFIGURATION.md) for supported values and the settings-provider interface.

## Screenshots

<table><tr><td><img src="art/screenshots/brick-natural.png" alt="Recorded wood fire in a photographed brick and timber surround"></td><td><img src="art/screenshots/stone-natural.png" alt="Natural stone and timber with a larger centered firebox"></td><td><img src="art/screenshots/modern-natural.png" alt="Modern dark slate surround with a clean steel reveal"></td></tr><tr><td><img src="art/screenshots/brick-low.png" alt="Low recorded-flame exposure without resizing the logs"></td><td><img src="art/screenshots/brick-high.png" alt="High recorded-flame exposure with unchanged frame geometry"></td><td><img src="art/screenshots/settings.png" alt="Remote settings preserving every original option"></td></tr><tr><td><img src="art/screenshots/credits.png" alt="Offline source credits reached through the remote"></td><td><img src="art/screenshots/credits-scrolled.png" alt="Full application license scrolled with the remote"></td><td><img src="art/screenshots/dream-brick.png" alt="Actual DreamService test activation rendering real fire"></td></tr></table>

These actual 1920x1080 captures came from the version 1.1.0 source candidate on the shared Android 14/API 34 Google TV emulator 5580. The default preview was captured after more than 40 seconds of playback. See [device verification](docs/VERIFICATION.md) for the tested APK hash, methods and limits.

## Building and testing

```bash
bash build.sh
bash test.sh
bash scripts/test-python-coverage.sh
bash scripts/test-coverage.sh
```

The app uses the Android SDK and JDK without third-party runtime dependencies. See [building](docs/BUILDING.md), [testing](docs/TESTING.md), and [architecture](docs/ARCHITECTURE.md).

## Documentation

- [Installation, update, and removal](docs/INSTALLATION.md)
- [Configuration and host integration](docs/CONFIGURATION.md)
- [Building from source](docs/BUILDING.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Testing and coverage](docs/TESTING.md)
- [Device verification](docs/VERIFICATION.md)
- [Quality correction and verification limits](docs/QUALITY-AUDIT.md)
- [CI](docs/CI.md)
- [Troubleshooting](docs/TROUBLESHOOTING.md)
- [Release process](docs/RELEASING.md)

## Project policies

- [Contributing](CONTRIBUTING.md)
- [Security reporting](SECURITY.md)

## Release notes

- [Version 1.0.0](docs/releases/v1.0.0.md)

## License

Twilight Hearth is licensed under the [Apache License, Version 2.0](LICENSE). Recorded fire is CC BY 3.0 and photographed materials are CC0. See [NOTICE](NOTICE), [media provenance](assets/media.json) and [material provenance](assets/materials/provenance.json) for attribution and exact hashes. Full application licenses and credits are also available offline in settings.
