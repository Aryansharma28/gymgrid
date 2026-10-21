# GymGrid

An open source Android workout calendar inspired by contribution grids. Tap a date to check or uncheck a gym day, then keep your progress visible on your home screen.

## Features

- One tap to check or uncheck a workout day
- Monthly calendar and a rolling year grid
- Current streak and total logged days
- Compact month widget and larger year widget
- Data stored locally on your device, with no account or network access

## Install

On an Android 8.0 (API 26) or newer phone, download the APK from the [v0.1.0 preview release](https://github.com/Aryansharma28/gymgrid/releases/tag/v0.1.0). Android may ask you to allow installation from the app used to open the APK. You can also open this project in Android Studio and run the `app` configuration on your device.

Add a widget by long pressing your home screen, choosing **Widgets**, then selecting **GymGrid · Month** or **GymGrid · Year**. Tapping a widget opens the calendar so you can check or uncheck a day.

## Build

The project uses Android Gradle Plugin 8.2.1, Gradle 8.2, Java 17, and Android SDK 34. Run `./gradlew assembleDebug` (or `gradlew.bat assembleDebug` on Windows). The output is `app/build/outputs/apk/debug/app-debug.apk`.

## Privacy

Workout dates are saved in the app's private local storage. The app requests no permissions and does not send data anywhere. Uninstalling the app removes the dates.

## Contributing

Issues and pull requests are welcome. Please keep the app small, accessible, and useful without an account.

## License

MIT. See [LICENSE](LICENSE).
