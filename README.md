# Suv Tracker (Water Tracker)

Kunlik ichilgan suv miqdorini kuzatib boruvchi Android ilova.
Kotlin + Jetpack Compose bilan yozilgan, hech qanday internet ruxsati yo'q —
barcha ma'lumot faqat qurilmangizda (Room DB) saqlanadi.

## Asosiy imkoniyatlar

- Bosh ekranda bugungi kun uchun aylana progress: `ichilgan / meyor (ml)`
- Tezkor tugmalar: +100, +200, +300, +500 ml va shu miqdorlarni ayirish (xato bosilsa tuzatish uchun)
- "Boshqa miqdor" — o'zingiz xohlagan sonni kiritib, qo'shish yoki ayirish
- Kunlik meyorni (normani) sozlamalar orqali o'zgartirish
- Yangi kun avtomatik ravishda qurilma sanasiga qarab boshlanadi
- Statistika bo'limida oxirgi 31 kunlik grafik + to'liq ro'yxat
- 31 kundan eski yozuvlar avtomatik o'chib boradi

## Rang mantig'i (yashil/qizil)

**Bugungi kun (hali tugamagan):** kun davomidagi vaqtga mutanosib baholanadi —
meyor 24 soatga taqsimlanadi va shu daqiqagacha "kutilayotgan" miqdor bilan
solishtiriladi. Masalan ertalab soat 6 da meyorning 1/4 qismini ichgan bo'lsangiz
— bu me'yorda (yashil) hisoblanadi, chunki kun hali davom etyapti. Agar shu
vaqtga nisbatan orqada qolsangiz — qizil ko'rsatiladi.

**Tugagan kunlar (statistikada):** belgilangan meyordan ±1 litr (±1000 ml)
oralig'ida bo'lsa — yashil (normal). Undan ko'p kam yoki ko'p bo'lsa — qizil.

## Ma'lumotlar xavfsizligi

- `AndroidManifest.xml`da INTERNET ruxsati umuman yo'q — ilova tarmoqqa ulanmaydi.
- Barcha ma'lumot faqat lokal Room (SQLite) bazasida saqlanadi.
- `allowBackup=false` va cloud/device-transfer backup qoidalari o'chirilgan —
  ma'lumotlaringiz hech qayerga (Google backup'ga ham) yuklanmaydi.

## Signing key (yangilanish muammosi haqida)

Loyihaga `keystore/release.keystore` fayli committing qilingan va u
**debug hamda release** build turlarining ikkalasida ham ishlatiladi
(`app/build.gradle.kts`dagi `signingConfigs`). Bu shuni anglatadi:

- Ilovani necha marta qayta build qilib, qurilmaga qayta o'rnatsangiz ham,
  imzo (signature) doim bir xil bo'ladi — "package conflicts with an existing
  package" yoki "signature mismatch" xatoligi chiqmaydi.
- Eski ma'lumotlaringiz (ichilgan suv tarixi) yangilanishda saqlanib qoladi.

⚠️ Eslatma: qulaylik uchun keystore parollari `build.gradle.kts` ichida ochiq
yozilgan (`watertracker2026`). Shuning uchun bu repository'ni **private**
saqlashni tavsiya qilamiz. Xohlasangiz, keyinchalik parollarni GitHub
Secrets + `local.properties` orqali yashirishga o'tkazishingiz mumkin.

## GitHub orqali build qilish

Repo'ga push qilinganda `.github/workflows/build.yml` avtomatik ishga tushadi:

1. GitHub'ga push qiling (`main` yoki `master` branch)
2. **Actions** bo'limiga o'ting, oxirgi workflow run'ni oching
3. Tugagach, pastdagi **Artifacts** qismidan `water-tracker-release-apk`
   faylini yuklab oling — bu tayyor, imzolangan `.apk` fayl
4. Uni telefoningizga o'tkazib o'rnating (Noma'lum manbalardan o'rnatishga
   ruxsat berish kerak bo'lishi mumkin)

Har safar yangi versiya chiqarmoqchi bo'lsangiz, `app/build.gradle.kts`dagi
`versionCode`ni bittaga oshiring (masalan 1 → 2), `versionName`ni ham
yangilang, so'ng qayta push qiling.

## Android Studio'da ochish

Bu paketda `gradle-wrapper.jar` (binary fayl) yo'q — matn asosida
tayyorlanganligi sabab uni jo'natib bo'lmaydi. Android Studio'da loyihani
ochganingizda u avtomatik "Gradle sync" so'raydi va wrapper faylini o'zi
yaratib oladi (yoki terminalda `gradle wrapper` buyrug'ini bir marta
ishga tushiring, agar tizimingizda Gradle o'rnatilgan bo'lsa). GitHub
Actions esa bunga muhtoj emas — u Gradle'ni to'g'ridan-to'g'ri o'rnatib
build qiladi.

## Struktura

```
app/src/main/java/com/watertracker/app/
├── data/        # Room DB, DataStore sozlamalari, repository
├── util/        # Sana va rang hisoblash logikasi
├── ui/theme/    # Ranglar, tipografiya
├── ui/components/ # Progress halqa, bar-chart
├── ui/home/     # Bosh ekran
└── ui/stats/    # Statistika ekrani
```
