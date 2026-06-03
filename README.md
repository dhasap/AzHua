# AzHua - Donghua Streaming App

Aplikasi Android open-source untuk streaming donghua (animasi Tiongkok) dengan sistem ekstensi modular.

## Arsitektur

Clean Architecture dengan multi-module:

```
app/                    # Entry point, DI graph, navigation
core/
  core-common/          # Extensions, utilities, dispatchers
  core-ui/              # Design system, shared composables
  core-model/           # Domain models (pure Kotlin)
  core-database/        # Room database, DAOs, entities
  core-network/         # OkHttp, interceptors
feature/
  feature-library/      # Pusaka screen
  feature-discover/     # Jelajah screen
  feature-recents/      # Terkini screen
  feature-extensions/   # Paviliun screen
  feature-detail/       # Detail donghua
  feature-player/       # Video player
  feature-settings/     # Pengaturan
data/
  data-repository/      # Repository implementations
extension-api/          # Public API untuk extension developer
```

## Tech Stack

- **Language:** Kotlin 2.0.21
- **UI:** Jetpack Compose + Material3
- **DI:** Hilt
- **Database:** Room
- **Network:** OkHttp + Retrofit
- **Image:** Coil 3
- **Player:** Media3 ExoPlayer
- **Architecture:** Clean Architecture + MVVM + UDF

## Build

```bash
./gradlew assembleDebug
```

## License

MIT License
