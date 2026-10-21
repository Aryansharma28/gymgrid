# GymGrid

An open source Android workout calendar inspired by contribution grids. Answer **Yes** or **No** from your home screen widget. Yes turns a day green; No turns it gray. Your answers stay on your phone.

## Features

- Yes and No actions directly on both home screen widgets
- Green Yes days, gray No days, and darker unanswered days
- One tap to change a past day in the app
- Monthly calendar and a rolling year grid
- Current streak and total logged days
- Compact month widget and larger year widget
- Data stored locally on your device, with no account or network access

## Install

On an Android 8.0 (API 26) or newer phone, download the APK from the [v0.2.0 preview release](https://github.com/Aryansharma28/gymgrid/releases/tag/v0.2.0). Android may ask you to allow installation from the app used to open the APK. You can also open this project in Android Studio and run the `app` configuration on your device.

Add a widget by long pressing your home screen, choosing **Widgets**, then selecting **GymGrid · Month** or **GymGrid · Year**. Tap **Yes** or **No** on either widget to answer for today. Tap its calendar to open the app and edit past days.

## Build

The project uses Android Gradle Plugin 8.9.2, Gradle 8.11.1, Java 17, and Android SDK 35. Run `./gradlew assembleDebug` (or `gradlew.bat assembleDebug` on Windows). The output is `app/build/outputs/apk/debug/app-debug.apk`.

## Privacy

Yes and No answers are saved in the app's private local storage. The app requests no permissions and does not send data anywhere. Uninstalling the app removes the answers.

## Contributing

Issues and pull requests are welcome. Please keep the app small, accessible, and useful without an account.

## License

MIT. See [LICENSE](LICENSE).
