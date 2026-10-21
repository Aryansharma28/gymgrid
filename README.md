# GymGrid

<p align="center">
  <img src="app/src/main/res/drawable-nodpi/mascot_elephant.png" alt="GymGrid elephant mascot lifting a dumbbell" width="220" />
</p>

An open source Android workout calendar inspired by contribution grids. Answer **Yes** or **No** from your home screen widget. Yes turns a day green; No turns it gray. Your answers stay on your phone.

## Preview

![GymGrid design preview showing the app, month widget, and year widget](docs/preview.png)

The preview illustrates the app and both widget sizes. The elephant mascot is included in the app and its launcher icon.

## Features

- Yes and No actions directly on both home screen widgets
- Green Yes days, gray No days, and darker unanswered days
- One tap to change a past day in the app
- Monthly calendar and a rolling year grid
- Current streak and total logged days
- Compact month widget and larger year widget
- Data stored locally on your device, with no account or network access

## Install on a Samsung Galaxy S25

1. Open the [v0.2.0 release](https://github.com/Aryansharma28/gymgrid/releases/tag/v0.2.0) on your phone and tap **GymGrid-v0.2.0.apk** under **Assets**.
2. Open the download in Samsung Internet, Chrome, or **My Files → Downloads** and tap **Install**.
3. If Samsung asks, allow **Install unknown apps** for the app you opened the APK with. You can find this under **Settings → Security and privacy → More security settings → Install unknown apps**. If **Auto Blocker** interrupts the install, go to **Settings → Security and privacy → Auto Blocker**, switch it off for the installation, then turn it back on afterward.
4. Open GymGrid once. Long press an empty spot on the home screen, tap **Widgets**, choose **GymGrid · Month** or **GymGrid · Year**, then tap **Add**. You can resize the widget by long pressing it and dragging a corner.
5. Tap **Yes** or **No** directly on the widget each day. The app holds your monthly and yearly history.

## Install on another Android phone

Download **GymGrid-v0.2.0.apk** from the same release on an Android 8.0 or newer phone. Open the APK in your browser or file manager, allow that app to install unknown apps if prompted, and tap **Install**. Then long press the home screen, open **Widgets**, and add a GymGrid widget. Tap its calendar to open the app and edit past days.

If you installed the early v0.1.0 debug preview, uninstall it before installing v0.2.0 because the signing key changed. Uninstalling v0.1.0 removes its locally saved days. Releases from v0.2.0 onward use the same private signing key so future updates can keep your data.

## Build

The project uses Android Gradle Plugin 8.9.2, Gradle 8.11.1, Java 17, and Android SDK 35. Open this project in Android Studio and run the `app` configuration on your device, or run `./gradlew assembleDebug` (`gradlew.bat assembleDebug` on Windows). The output is `app/build/outputs/apk/debug/app-debug.apk`. Public downloads use a separately signed release APK.

## Privacy

Yes and No answers are saved in the app's private local storage. The app requests no permissions and does not send data anywhere. Uninstalling the app removes the answers.

## Contributing

Issues and pull requests are welcome. Please keep the app small, accessible, and useful without an account.

## License

MIT. See [LICENSE](LICENSE).
