# SSH Tunnel Manager - Android

A professional Android application for managing SSH tunnels with port forwarding capabilities.

## Features

- 🔐 **SSH Connection Management**: Add, edit, and delete SSH servers
- 🔑 **Dual Authentication**: Support for both password and private key authentication
- 🛡️ **Port Forwarding**: Configure local and remote port forwarding
- 💾 **Persistent Storage**: All configurations saved locally using Room Database
- 🎨 **Modern UI**: Built with Jetpack Compose and Material Design 3
- 📱 **Responsive Design**: Works seamlessly on all Android devices
- 🔄 **Real-time Status**: Monitor connection status in real-time

## Architecture

The application follows the MVVM (Model-View-ViewModel) architecture pattern:

```
data/ 
├── SshServerEntity.kt      # Room entity
├── SshServerDao.kt         # Database access object
└── AppDatabase.kt          # Database configuration

repository/
└── SshServerRepository.kt   # Data access layer

service/
└── SshTunnelManager.kt     # SSH connection management

viewmodel/
└── SshViewModel.kt         # UI state and business logic

ui/
├── screens/                # Composable screens
│   ├── HomeScreen.kt
│   └── AddEditServerScreen.kt
├── navigation/
│   └── Navigation.kt       # Navigation logic
└── theme/                  # Material Design theme

di/
└── Module.kt              # Dependency injection
```

## Setup & Installation

### Prerequisites

- Android Studio Giraffe or later
- Android SDK 24+ (API Level 24)
- Kotlin 1.9.20+

### Clone Repository

```bash
git clone https://github.com/Alisepahvan/android-ssh-tunnel-manager.git
cd android-ssh-tunnel-manager
```

### Build

```bash
# Open in Android Studio and sync Gradle files
# Or build from terminal
./gradlew build

# Run on device/emulator
./gradlew installDebug
```

## Usage

### Adding a Server

1. Tap the **+** button on the home screen
2. Enter server details:
   - Server Name
   - Host/IP Address
   - SSH Port (default: 22)
   - Username
3. Choose authentication method:
   - **Password**: Enter your password
   - **Private Key**: Paste your private key
4. Configure port forwarding:
   - Local Port (default: 1080)
   - Remote Host (default: 127.0.0.1)
   - Remote Port (default: 80)
5. Tap **Add Server**

### Connecting to a Server

1. Select a server from the list
2. Tap **Connect**
3. Wait for connection to establish
4. A badge will show "Connected" status

### Editing/Deleting

- Tap **Edit** to modify server settings (only when disconnected)
- Tap **Delete** to remove a server (only when disconnected)

## Dependencies

### Core
- `androidx.core:core-ktx` - Kotlin extensions for AndroidX
- `androidx.lifecycle:lifecycle-runtime-ktx` - Lifecycle management

### Compose
- `androidx.compose.ui:ui` - Compose UI framework
- `androidx.compose.material3:material3` - Material Design 3
- `androidx.navigation:navigation-compose` - Navigation for Compose
- `androidx.lifecycle:lifecycle-viewmodel-compose` - ViewModel in Compose

### Database
- `androidx.room:room-runtime` - SQLite abstraction
- `androidx.room:room-ktx` - Coroutines support for Room

### SSH
- `com.jcraft:jsch` - SSH client library

## Project Structure

```
android-ssh-tunnel-manager/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── java/com/example/sshmanager/
│           │   ├── data/
│           │   ├── repository/
│           │   ├── service/
│           │   ├── viewmodel/
│           │   ├── ui/
│           │   ├── di/
│           │   ├── MainActivity.kt
│           │   └── SshApp.kt
│           ├── res/
│           └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## Key Classes

### SshViewModel
- Manages UI state using StateFlow
- Handles server operations (add, edit, delete)
- Manages SSH connections

### SshTunnelManager
- Handles SSH connections using JSch library
- Manages port forwarding
- Lifecycle management (connect/disconnect)

### SshServerRepository
- Abstracts database operations
- Provides data to ViewModel via Flow

### AppDatabase
- Singleton Room database instance
- Contains DAO definitions

## Configuration

### Android Manifest Permissions

The following permissions are required:
- `INTERNET` - For SSH connections

## Troubleshooting

### Build Issues

**Gradle sync fails**
- Clear build cache: `./gradlew clean`
- Invalidate Android Studio cache: File → Invalidate Caches

**Compilation errors**
- Ensure Kotlin plugin version matches (1.9.20)
- Check Android SDK is properly installed

### Runtime Issues

**Connection fails**
- Verify host/IP and port are correct
- Check network connectivity
- Ensure SSH server is running and accessible
- Verify credentials (password or private key)

**Private key not working**
- Ensure key is in PEM format
- Check key permissions
- Verify key passphrase if required

## Security Considerations

⚠️ **Important Security Notes**:

1. **Local Storage**: Passwords are stored in plain text in Room database
   - Consider adding encryption for sensitive data
   - Use Android Keystore for enhanced security

2. **Private Keys**: Keys should be handled securely
   - Implement proper key management
   - Consider using SSH key files from device storage

3. **Network**: Always use secure SSH connections
   - Verify server host keys
   - Use strong authentication methods

## Future Enhancements

- [ ] Encrypted password storage using Android Keystore
- [ ] SSH key management and generation
- [ ] Connection history and logs
- [ ] Multiple simultaneous connections
- [ ] SOCKS5 proxy support
- [ ] Connection profiles/favorites
- [ ] Backup and restore configurations
- [ ] Dark mode optimization

## License

MIT License - See LICENSE file for details

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## Support

For issues and feature requests, please use the GitHub issues page.

## Author

Ali Sepahvan (@Alisepahvan)