# Continuous integration

GitHub Actions runs the following checks on pushes to `main` and pull requests:

- Android host tests and Python installer tests.
- 100% line and branch coverage gates for project-owned option, validation, and installer logic.
- Java formatting and documentation link checks.
- APK build, package inspection, and a check that runtime network permissions are absent.
- Secret scanning through the configured GitHub security workflow.

Third-party GitHub Actions are pinned to full commit SHAs. CI uses temporary signing material and never publishes its APK as a release. The production APK is signed locally with the private release key documented in [building](BUILDING.md).

No SonarCloud, CodeFactor, Codacy, Aikido, or Scrutinizer integration is configured. There are no working credentials or onboarding for the optional external quality dashboards, so the repository does not display unconfigured ratings or badges.
