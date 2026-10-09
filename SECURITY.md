# Security policy

Report a suspected vulnerability through GitHub's private vulnerability reporting for this repository. Do not include personal account data, device identifiers, or private logs in a public issue. Include the affected release, Android version, concise reproduction steps, and the impact needed to assess the report.

The Android app has no runtime network permission or remote update mechanism. The standalone installer downloads release metadata and APK/checksum assets from this repository and verifies the APK checksum before installation.
