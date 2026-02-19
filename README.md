# notey. 📝

A minimal, elegant note-taking app built with **Kotlin Multiplatform** and **Compose Multiplatform**, targeting Android and iOS — with optional sync powered by a backend server.

---

## ✨ Features

- **Create & delete notes** with a sleek bottom sheet UI
- **Soft delete** — notes are marked deleted and synced, not hard-removed
- **Cloud sync** — bidirectional sync with conflict resolution via a REST backend
- **Auth flow** — sign up / sign in with JWT-based authentication
- **Offline-first** — dirty-flag tracking ensures unsynced notes are pushed when connectivity returns
- **Dark theme** — custom deep dark color palette with gold accents
- **Cross-platform** — single codebase for Android and iOS (Desktop build also supported)

---

## 🏗️ Tech Stack

| Layer | Technology |
|---|---|
| UI | Compose Multiplatform |
| Navigation | Jetpack Navigation Compose (KMP) |
| Local DB | Room (KMP) + BundledSQLiteDriver |
| Networking | Ktor Client |
| Serialization | kotlinx.serialization |
| Persistence | DataStore Preferences |
| Architecture | MVVM + Repository pattern |
| Build | Gradle with version catalog (`libs.versions.toml`) |

---

## 📁 Project Structure

```
NoteyApp/
├── composeApp/
│   └── src/
│       ├── commonMain/         # Shared UI, ViewModels, data layer
│       │   ├── data/
│       │   │   ├── datastore/  # Auth token storage (DataStoreManager)
│       │   │   ├── db/         # Room DAOs & NoteDatabase
│       │   │   └── remote/     # ApiService, SyncRepository, HttpClientFactory
│       │   ├── feature/
│       │   │   ├── home/       # HomeScreen + HomeViewModel
│       │   │   ├── profile/    # ProfileScreen + ProfileViewModel
│       │   │   ├── signin/     # SignInScreen + SignInViewModel
│       │   │   └── signup/     # SignUpScreen + SignUpViewModel
│       │   ├── model/          # Note, SyncMetadata, AuthRequest/Response
│       │   ├── screen/         # NoteList & NoteItem composables
│       │   └── ui/theme/       # Colors, Typography, Theme
│       ├── androidMain/        # Android-specific: MainActivity, DB builder, DateUtils
│       └── iosMain/            # iOS-specific: MainViewController, DB builder, DateUtils
├── iosApp/                     # Xcode project & SwiftUI entry point
└── gradle/
    └── libs.versions.toml      # Centralized dependency versions
```

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17**
- **Android Studio** (Hedgehog or newer) with KMP plugin
- **Xcode 16+** (for iOS builds)
- Gradle 9.1+

---

### Android

**Debug build:**

```bash
# macOS / Linux
./gradlew :composeApp:assembleDebug

# Windows
.\gradlew.bat :composeApp:assembleDebug
```

**Release APK:**

```bash
./gradlew :composeApp:assembleRelease
```

The APK will be at `composeApp/build/outputs/apk/release/`.

---

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode, select your simulator or device, and hit **Run**.

Alternatively, the Kotlin framework is compiled automatically as part of the Xcode build phase via:

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

> **Note:** Set your `TEAM_ID` in `iosApp/Configuration/Config.xcconfig` before building for a real device.

---

### Desktop (JVM)

```bash
./gradlew :composeApp:packageReleaseDistributionForCurrentOS
```

Output: `composeApp/build/compose/binaries/main-release/`

---

## 🔄 Sync & Backend

The app uses a custom sync protocol to reconcile local and server notes.

**Endpoints expected (default: `http://localhost:8080`):**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/signup` | Register a new user |
| POST | `/auth/login` | Authenticate, returns JWT tokens |
| POST | `/sync` | Bidirectional note sync |

**Sync request payload:**

```json
{
  "since": "2024-01-01T00:00:00Z",
  "changes": [
    {
      "id": "uuid",
      "title": "Note title",
      "body": "Note content",
      "isDeleted": false,
      "updatedAt": "2024-06-01T10:00:00Z"
    }
  ]
}
```

**Android emulator** automatically resolves the backend at `http://10.0.2.2:8080`.  
**iOS simulator** uses `http://localhost:8080`.

To point to a different server, update `BASE_URL_EMULATOR` in the platform-specific `ApiService` files.

---

## 🗄️ Database Schema

### `Note`

| Column | Type | Notes |
|---|---|---|
| `id` | String (PK) | UUID, auto-generated |
| `title` | String | Required |
| `description` | String | Optional body text |
| `userId` | String | Owner's user ID |
| `updatedAt` | String | ISO-8601 timestamp |
| `isDeleted` | Boolean | Soft delete flag |
| `isDirty` | Boolean | Pending sync flag |

### `SyncMetadata`

| Column | Type | Notes |
|---|---|---|
| `id` | Int (PK) | Always `1` |
| `lastSyncTimestamp` | String? | Cursor for incremental sync |
| `isSyncing` | Boolean | Lock to prevent concurrent syncs |

---

## 🔐 Authentication

Auth tokens are stored securely in **DataStore Preferences**:

- `token` — JWT access token (sent as `Authorization: Bearer <token>`)
- `refresh` — Refresh token
- `user_id` — User identifier
- `email` — Cached email for display

Logging out clears all stored preferences via `DataStoreManager.clearAll()`.

---

## 🎨 Design System

The app uses a fully custom dark theme:

| Token | Hex | Usage |
|---|---|---|
| `BgDeep` | `#0D0D10` | App background |
| `BgCard` | `#1E1E28` | Card surfaces |
| `AccentGold` | `#D4A847` | Primary accent, CTAs |
| `TextPrimary` | `#F0EDE6` | Main text |
| `TextMuted` | `#7A7880` | Secondary text |
| `ErrorRed` | `#E05C5C` | Destructive actions |
| `SuccessGreen` | `#4CAF7D` | Confirmation states |

---

## ⚙️ CI/CD

GitHub Actions workflows are configured for automated builds on push to `master`:

- **`android-build`** — builds a release APK and uploads it as an artifact
- **`desktop-build`** — packages a desktop release for the current OS

See `.github/workflows/build.yml` for details.

---

## 📦 Key Dependencies

```toml
compose-multiplatform = "1.10.0"
kotlin              = "2.3.0"
room                = "2.8.2"
ktor                = "3.3.1"
androidx-lifecycle  = "2.9.6"
navigation-compose  = "2.9.1"
datastore           = "1.1.7"
```

---

## 📄 License

This project is open source. See `LICENSE` for details.