# listen-to-prabhupada

In few months this would be production ready app for playing lectures of His Divine Grace A. C. Bhaktivedanta Swami Prabhupada, Founder-Acharya of International Society for Krishna Consciousness (ISKCON) (https://prabhupada.krishna.com/)

Tech stack

- [Kotlin Mobile Multiplatform](https://kotlinlang.org/lp/mobile/)
- [MVI-Kotlin](https://arkivanov.github.io/MVIKotlin/)
- [Decompose](https://arkivanov.github.io/Decompose/)
- [Ktor](https://ktor.io/)
- [SqlDelight](https://cashapp.github.io/sqldelight/)
- [KMM Settings](https://github.com/russhwolf/multiplatform-settings)
- [ExoPlayer](https://exoplayer.dev/) (for now only Android app has a player functionality)
- [JetpackCompose](https://developer.android.com/jetpack/compose) (Android)
- [SwiftUI](https://developer.apple.com/xcode/swiftui/) (IOs)

in plans : 
KMM Firebase & Crashlytics ([kermit](https://touchlab.co/kermit-and-crashlytics/))
Material 3 and proper theming in all components
AudioPlayer will be fixed in bottombar (hide on scroll)
Topbar menu (hide on scroll)

No DI framework used for now... had a hard time trying to use Koin ))

# Main functionality 

## Results screen (main screen)

- Download paginated list of lectures
- List of lectures
- Pagination control
- Audio player

[results](./screenshots/lectures.ong)

[player](./screenshots/notification.ong)

[notification](./screenshots/notification.ong)

## Filters screen

[filters](./screenshots/lectures.ong)

## Favorites screen (TODO)

## Downloads screen (currently download functionality needs testing)

## ...
