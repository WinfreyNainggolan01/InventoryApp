# 📦 InventoryApp

Aplikasi simple manajemen inventori berbasis Android yang dibangun menggunakan **Kotlin** dengan arsitektur **MVVM (Model-View-ViewModel)** dan prinsip **Clean Architecture**. Proyek ini dirancang sebagai studi kasus praktis pengembangan Android modern.

---

## 🏗️ Arsitektur

Proyek ini mengikuti pola **Clean Architecture** yang dibagi menjadi tiga lapisan utama:

```
presentation/   → UI (Compose Screens, ViewModels, Components, Navigation)
domain/         → Business Logic (Use Cases, Models, Repository Interfaces)
data/           → Data Source (Room DB, DAOs, Entities, Mappers, Repository Impl)
di/             → Dependency Injection Modules (Hilt)
```

### Layer Breakdown

| Layer | Komponen | Deskripsi |
|---|---|---|
| **Presentation** | Screens, ViewModels, Components | Mengelola tampilan UI & state |
| **Domain** | Use Cases, Models, Repository Interface | Business logic & abstraksi |
| **Data** | Room, DAO, Entity, Mapper, Repository Impl | Implementasi data persistence |
| **DI** | Hilt Modules | Penyedia dependency |

---

## 🛠️ Tech Stack

### 🔤 Bahasa & Platform

| Teknologi | Versi | Deskripsi |
|---|---|---|
| **Kotlin** | `2.0.21` | Bahasa pemrograman utama |
| **Android SDK** | `minSdk 24` / `targetSdk 36` | Platform target Android |
| **Java** | `VERSION_11` | JVM target compatibility |

---

### 🎨 UI Framework

| Teknologi | Versi | Deskripsi |
|---|---|---|
| **Jetpack Compose** | BOM `2024.10.01` | UI toolkit deklaratif modern |
| **Material 3** | via BOM | Komponen UI Material Design 3 |
| **Material Icons Extended** | via BOM | Set ikon Material yang lengkap |
| **Activity Compose** | `1.10.0` | Integrasi Compose dengan Activity |
| **AppCompat** | `1.8.0` | Kompatibilitas backward untuk UI |
| **ConstraintLayout** | `2.2.2` | Layout engine yang fleksibel |
| **Material Design** | `1.14.0` | Komponen Material Design untuk View |


## 📁 Struktur Proyek

```
app/src/main/java/com/example/inventoryapp/
│
├── 📂 data/
│   ├── local/
│   │   ├── dao/            -> ProductDao (CRUD queries)
│   │   ├── database/       -> AppDatabase (Room setup)
│   │   └── entity/         -> ProductEntity (table schema)
│   ├── mapper/             -> ProductMapper (Entity <-> Domain Model)
│   └── repository/         -> ProductRepositoryImpl
│
├── 📂 di/
│   ├── DatabaseModule.kt   -> Provides Room DB & DAO
│   └── RepositoryModule.kt -> Provides Repository bindings
│
├── 📂 domain/
│   ├── model/              -> Product (domain model)
│   ├── repository/         -> ProductRepository (interface)
│   └── usecase/
│       ├── AddProductUseCase
│       ├── DeleteProductUseCase
│       ├── GetProductsUseCase
│       └── UpdateProductUseCase
│
├── 📂 presentation/
│   ├── components/         -> BottomBar, CustomComponents, ProductCard
│   ├── navigation/         -> MainNavigation, Screen (routes)
│   ├── screens/
│   │   ├── addproduct/     -> Add Product Screen
│   │   ├── detail/         -> Detail Screen
│   │   ├── home/           -> Home Screen
│   │   ├── inventory/      -> Inventory Screen
│   │   └── profile/        -> Profile Screen
│   └── viewmodel/          -> ProductViewModel
│
├── InventoryApp.kt         -> @HiltAndroidApp Application class
└── MainActivity.kt         -> Entry point Activity
```

---

## ✨ Fitur Utama

- ✅ **CRUD Produk** — Tambah, lihat, edit, dan hapus produk inventori
- ✅ **Navigasi Multi-Screen** — Home, Inventory, Detail, Add Product, Profile
- ✅ **Reactive UI** — State dikelola dengan `StateFlow` + `collectAsStateWithLifecycle`
- ✅ **Local Persistence** — Data tersimpan secara lokal menggunakan Room Database
- ✅ **Clean Architecture** — Pemisahan jelas antara presentation, domain, dan data layer
- ✅ **Dependency Injection** — Hilt untuk pengelolaan dependency yang modular
- ✅ **Edge-to-Edge UI** — Tampilan modern yang memanfaatkan seluruh layar

---

## 🚀 Memulai Proyek

### Clone & Run

```bash
# Clone repository
git clone <repo-url>

# Buka di Android Studio
# File -> Open -> pilih folder InventoryApp

# Sync Gradle lalu jalankan di emulator/device
```

---

## 📦 Dependency Summary

```toml
# gradle/libs.versions.toml

[versions]
kotlin                = "2.0.21"
agp                   = "8.9.1"
ksp                   = "2.0.21-1.0.28"
coreKtx               = "1.15.0"
appcompat             = "1.8.0"
activityKtx           = "1.13.0"
constraintlayout      = "2.2.2"
material              = "1.14.0"
lifecycle             = "2.8.7"
room                  = "2.8.5"
hilt                  = "2.55"
hiltNavigationCompose = "1.2.0"
coroutines            = "1.9.0"
junit                 = "4.13.2"
junitVersion          = "1.3.0"
espressoCore          = "3.7.0"
```

---

*Proyek ini dibuat untuk keperluan pembelajaran dan pemahaman mengenai Android Development dengan Kotlin Modern Stack.*
