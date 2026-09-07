# 🚀 খতিয়ান (Khatiyan) — Personal Financial & Debt Management Application

> **“সব হিসাব, এক জায়গায়।”**

**খতিয়ান** হলো একটি ফ্ল্যাগশিপ কোয়ালিটির, ১০০% ফ্রি, প্রাইভেট এবং অফলাইন-ফার্স্ট অ্যান্ড্রয়েড অ্যাপ্লিকেশন। এটি সাধারণ মানুষের দৈনন্দিন জীবনের সমস্ত আর্থিক দায়, দোকানের বাকী, ব্যাংক বা এনজিও লোন, পণ্যের ইএমআই বা কিস্তি, ব্যক্তিগত ধার-দেনা এবং আয়-ব্যয়ের হিসাব এক জায়গায় সহজে ও সুশৃঙ্খলভাবে পরিচালনা করতে সাহায্য করে।

---

## 🌟 প্রজেক্টের বৈশিষ্ট্য (Key Features)

- 🛍️ **দোকান ও বাজারের বাকী (Shop / Market Credit):** একাধিক দোকানের আলাদা ডিজিটাল খতিয়ান, বাকী কেনাকাটার হিসাব ও আংশিক/পূর্ণ পরিশোধ ট্র্যাকিং।
- 🏦 **ব্যাংক ও এনজিও লোন (Bank / NGO Loans):** লোনের পরিমাণ, কিস্তি, পরিশোধের সময়কাল ও রিমাইন্ডার ট্র্যাকার।
- 📱 **ইএমআই ও কিস্তি (EMI / Product Installment):** ল্যাপটপ, মোবাইল, ফ্রিজ বা অন্য পণ্যের কিস্তির হিসাব ও অগ্রগতি।
- 🤝 **ব্যক্তিগত ধার (Personal Borrowed Money):** বন্ধু, আত্মীয় বা সহকর্মীদের সাথে ধার নেওয়া বা দেওয়ার নিখুঁত হিসাব।
- 💰 **দৈনিক আয় ও ব্যয় (Income & Expense):** আয়-ব্যয়ের ট্র্যাকার, ক্যাটাগরি ও নিট ক্যাশ ফ্লো (Net Cashflow) এনালিটিক্স।
- 📊 **ইন্টারেক্টিভ ড্যাশবোর্ড ও রিপোর্ট (Dashboard & Reports):** ডোনাট চার্ট, বার চার্ট, স্মার্ট ইনসাইটস ও ভিজ্যুয়াল গ্রাফ।
- 📄 **লোকাল পিডিএফ রিপোর্ট (PDF Report Generator):** এক ক্লিকে সম্পূর্ণ আর্থিক অবস্থার ব্র্যান্ডেড পিডিএফ ফাইল তৈরি।
- 💾 **ব্যাকআপ ও পুনরুদ্ধার (Backup & Restore):** JSON ব্যাকআপ ও এক্সেল/CSV ফাইল সাপোর্ট।
- 🔒 **প্রাইভেসি ও অফলাইন-ফার্স্ট (Privacy First):** কোনো ক্লাউড সার্ভার প্রয়োজন নেই, আপনার সমস্ত তথ্য আপনার ফোনেই ১০০% নিরাপদ।
- 🚫 **কোনো বিজ্ঞাপন বা ফি নেই (100% Free & No Ads):** কোনো হিডেন চার্জ, বিজ্ঞাপন বা পেইড সাবস্ক্রিপশন নেই।

---

## 📱 স্ক্রিনশট (Screenshots)

*ড্যাশবোর্ড, হিসাবের খাতসমূহ, রিপোর্ট ও ডার্ক/লাইট মোড লুক।*

---

## 🛠️ টেকনোলজি স্ট্যাক (Tech Stack)

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material 3 Design
- **Architecture:** Clean Architecture / MVVM
- **Database:** Room Database
- **Local Storage:** Preferences DataStore
- **Concurrency:** Kotlin Coroutines & Flow
- **Background Work:** WorkManager & Android Notifications
- **Document Generation:** Android Native `PdfDocument` API
- **Export/Import:** Storage Access Framework (SAF) + Gson

---

## 🏗️ আর্কিটেকচার (Architecture Overview)

```
app/src/main/java/com/shohan/khatiyan/
├── KhatiyanApp.kt                  # Application Entry & WorkManager Setup
├── MainActivity.kt                 # Single Activity Host
├── data/
│   ├── local/                      # Room Database, DAOs & Entities
│   │   ├── entities/               # Shop, Loan, EMI, Personal, Income, Expense, Transaction
│   │   ├── dao/                    # Database Access Objects
│   │   └── KhatiyanDatabase.kt
│   ├── model/                      # Data Transfer & Aggregate Models
│   ├── preferences/                # DataStore User Preferences Repository
│   └── repository/                 # Repository Pattern Implementation
├── util/                           # Currency Formatter (Bengali digits), DateUtils, PDF Generator, Insights
├── notification/                   # Local Due Payment Reminder Engine
└── ui/
    ├── components/                 # Custom Canvas Charts, Stat Cards, Quick Add Sheet
    ├── navigation/                 # Jetpack Compose Navigation Graph
    ├── theme/                      # Forced Light Theme & Color Palette
    └── screens/                    # Onboarding, Dashboard, Accounts, Transactions, Reports, Settings, Backup
```

---

## 🛠️ বিল্ড ও রান করার নিয়ম (Build & Install Instructions)

### প্রয়োজনীয় টুলস (Prerequisites)
- **JDK 17+**
- **Android Studio Jellyfish / Iguana or later**
- **Android SDK (Compile SDK 34, Min SDK 26)**

### স্টেপ ১: রিপোজিটরি ক্লোন করুন
```bash
git clone https://github.com/shohanbusinessmail-cmd/khatiyan.git
cd khatiyan
```

> **Gradle Wrapper:** এই রিপোজিটরিতে বাইনারি `gradle/wrapper/gradle-wrapper.jar` কমিট করা নেই।
> ক্লোন করার পর একবার নিচের কমান্ড দিয়ে র‍্যাপার তৈরি করে নিন (Android Studio-তে খুললে স্বয়ংক্রিয়ভাবে হয়ে যায়)।
> CI-ও প্রতিটি রানে ঠিক এভাবেই র‍্যাপার জেনারেট করে নেয়।
>
> ```bash
> gradle wrapper --gradle-version 8.4
> ```

### স্টেপ ২: ডিবাগ এপিকে 빌ড করুন (Debug APK)
```bash
./gradlew assembleDebug
```
বিল্ড হয়ে যাওয়ার পর এপিকে ফাইল পাবেন: `app/build/outputs/apk/debug/app-debug.apk`

### স্টেপ ৩: রিলিজ এপিকে বিলড করুন (Release APK)
```bash
./gradlew assembleRelease
```
রিলিজ এপিকে ফাইল পাবেন: `app/build/outputs/apk/release/app-release.apk`

---

## 🤖 গিটহাব অ্যাকশনস ও সিআই/সিডি (GitHub Actions CI/CD)

রিপোজিটরিতে সংয়ংক্রিয় বিল্ড ও রিলিজের জন্য ২ টি গিটহাব ওয়ার্কফ্লো যুক্ত করা আছে:
1. `.github/workflows/ci.yml` — কোড পুশ বা পুল রিকোয়েস্ট তৈরি হলেই অটোমেটিক টেস্ট ও ডিবাগ এপিকে তৈরি করবে।
2. `.github/workflows/release.yml` — ভার্সন ট্যাগ (`v1.0.0`) পুশ করলে অটোমেটিক রিলিজ এপিকে বিল্ড করে গিটহাব রিলিজ তৈরি করে এপিকে অ্যাটাচ করে দেবে।
3. `.github/workflows/verify-release.yml` — প্রকাশিত রিলিজ থেকে `app-release.apk` ডাউনলোড করে `aapt2`/`apksigner` দিয়ে প্যাকেজ আইডি, ভার্সন ও সাইনার যাচাই করে।

CI-এর কোনো স্টেপ ব্যর্থ হলে পুরো ওয়ার্কফ্লো ব্যর্থ হবে — কোনো ধাপ চুপচাপ পাস করানো হয় না।

### 🔐 প্রোডাকশন সাইনিং (Production Signing)

ডিফল্টভাবে রিলিজ এপিকে **Android debug keystore** দিয়ে সাইন হয় (`CN=Android Debug`)।
এটি টেস্ট ইনস্টলের জন্য যথেষ্ট, কিন্তু **Google Play-তে আপলোড করা যাবে না**।

প্রকৃত আপলোড কী দিয়ে সাইন করতে চাইলে **Settings → Secrets and variables → Actions**-এ নিচের ৪টি সিক্রেট যোগ করুন।
রিপোজিটরিতে কখনো কীস্টোর, পাসওয়ার্ড বা সার্টিফিকেট কমিট করবেন না।

| Secret | বিবরণ |
|---|---|
| `KEYSTORE_BASE64` | আপলোড কীস্টোর (`.jks`) ফাইলের base64 কনটেন্ট — `base64 -w0 upload.jks` |
| `KEYSTORE_PASSWORD` | কীস্টোর পাসওয়ার্ড |
| `KEY_ALIAS` | কী অ্যালিয়াস |
| `KEY_PASSWORD` | কী পাসওয়ার্ড |

সিক্রেটগুলো সেট থাকলে `release.yml` স্বয়ংক্রিয়ভাবে সেগুলো ব্যবহার করবে; না থাকলে ওয়ার্কফ্লো একটি সতর্কবার্তা দেবে
এবং রিলিজ নোটে স্পষ্টভাবে লেখা থাকবে যে এপিকে-টি debug-signed।

---

## 👨‍💻 ডেভলপার তথ্য (Developer Credits)

- **App Creator:** Shohan Khan
- **Contact Email:** helloiamshohan@gmail.com
- **Repository:** [github.com/shohanbusinessmail-cmd/khatiyan](https://github.com/shohanbusinessmail-cmd/khatiyan)

---

## 📄 লাইসেন্স (License)

Copyright © 2026 **Shohan Khan**.
This project is open-source and free to use for personal financial management.
