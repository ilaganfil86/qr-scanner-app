# QR Code Scanner App (Android)

Simpleng QR code scanner app gamit ang Kotlin at ZXing library. Gumagana ito mula Android 7.0 (API 24) pataas, kaya kasama na ang Android 10.

## ⭐ Paano Kumuha ng .APK Kung WALANG Android Studio (Pinakamadali)

Gagamitin natin ang **GitHub** (libre) para automatic na i-build ang APK sa cloud — hindi mo kailangan mag-install ng kahit ano sa computer mo.

1. **Gumawa ng libreng GitHub account** sa https://github.com/signup (kung wala ka pa)
2. Sa GitHub, pindutin ang **"+"** sa taas-kanan → **"New repository"**
   - Pangalanan mo ng kahit ano, hal. `qr-scanner-app`
   - Piliin **Public** o **Private**, kahit alin
   - Pindutin ang **"Create repository"**
3. Sa page na lalabas, hahanapin mo ang link na **"uploading an existing file"** (o "upload files")
4. I-extract muna ang `QRScannerApp.zip` na binigay ko sa computer mo
5. I-drag-and-drop ang **LAHAT ng laman** ng `QRScannerApp` folder (hindi ang folder mismo, kundi ang mga laman nito — `app`, `gradle`, `build.gradle`, atbp.) papunta sa GitHub upload page
6. I-scroll pababa, pindutin ang **"Commit changes"**
7. Pumunta sa tab na **"Actions"** sa itaas ng repository mo
8. Makikita mo ang workflow na "Build APK" — awtomatiko itong tatakbo (may yellow/orange na dot habang nagbi-build, mag-a-antay ka lang ng 2-5 minuto)
9. Kapag naging **berde ✅** ang dot, pindutin mo ang workflow run na iyon
10. Sa ibaba ng page, may makikita kang seksyon na **"Artifacts"** — pindutin ang **"qr-scanner-debug-apk"** para i-download
11. Mag-do-download ito bilang .zip — i-extract mo, at makikita mo ang **`app-debug.apk`**
12. I-transfer ang `.apk` file sa Android phone mo (via Google Drive, USB, o email) at i-install (baka kailangan mong payagan muna ang "install from unknown sources" sa settings ng phone)

## Paano I-run (Kung May Android Studio)

1. I-download at i-extract ang buong `QRScannerApp` folder.
2. Buksan ang **Android Studio**.
3. Piliin ang **Open** (hindi bagong project) at i-select ang `QRScannerApp` folder.
4. Hintayin mag-sync ang Gradle (kakailanganin ng internet para i-download ang mga dependencies — lalo na ang ZXing library).
5. I-connect ang Android phone mo (o gumamit ng emulator) at pindutin ang **Run** (berdeng play button).
6. Sa unang pag-open, hihingi ng **camera permission** ang app — payagan ito.
7. Pindutin ang "I-scan ang QR Code" button, itapat sa QR code, at awtomatikong makikita ang resulta sa screen.
8. Pindutin ang result text para awtomatikong ma-copy ito sa clipboard.

## Mga Bahagi ng Project

- `MainActivity.kt` — ang pangunahing logic (camera permission + pag-launch ng scanner)
- `activity_main.xml` — ang UI/layout ng app
- `AndroidManifest.xml` — naka-declare dito ang CAMERA permission
- `app/build.gradle` — dito nakalagay ang ZXing dependency

## Mga Susunod na Maaaring Idagdag

- History ng mga na-scan na QR code (gamit ang Room database)
- Auto-open ng link kapag URL ang laman ng QR code
- Pag-generate din ng sariling QR code (hindi lang scan)
- Dark mode support

Kung may makakaharap kang error sa Gradle sync, siguraduhing updated ang Android Studio mo at may stable internet connection.
