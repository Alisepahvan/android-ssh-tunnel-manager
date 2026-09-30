# SSH Tunnel Manager

Android application for managing SSH connections and authorized local port forwarding.

## Features
- Kotlin and Jetpack Compose UI
- Room persistence for server profiles
- Password or private-key authentication
- Foreground connection-status notification
- Local-to-remote SSH port forwarding

## Build
Open the project in Android Studio, sync Gradle, and run:

```bash
./gradlew assembleDebug
```

Use only with SSH servers and accounts you are authorized to access. Host-key verification should be enabled before production use; this demo stores credentials locally and should be hardened with Android Keystore encryption before release.