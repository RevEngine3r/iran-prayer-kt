# Iran Prayer Times

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-blue.svg)](https://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/gradle-8.5-green.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

A modern Kotlin library for calculating accurate Islamic prayer times for major cities in Iran using astronomical algorithms.

## ✨ Features

- 🕌 Calculate all daily prayer times (Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha, Midnight)
- 🇮🇷 Pre-configured coordinates for 10 major Iranian cities
- 🌍 Support for custom coordinates and timezones
- 🔬 Astronomical calculations using Julian day and solar position algorithms
- ⏰ Accurate timezone handling with Java Time API
- 🎯 Configurable calculation parameters (Fajr/Isha angles, Asr shadow factor)
- 📦 Clean, idiomatic Kotlin code with proper package structure
- 🏗️ Modern Gradle build configuration

## 🏙️ Supported Cities

| English | Persian | Coordinates |
|---------|---------|-------------|
| Tehran | تهران | 35.69°N, 51.39°E |
| Tabriz | تبریز | 38.08°N, 46.29°E |
| Mashhad | مشهد | 36.33°N, 59.54°E |
| Isfahan | اصفهان | 32.65°N, 51.67°E |
| Shiraz | شیراز | 29.59°N, 52.58°E |
| Qom | قم | 34.64°N, 50.88°E |
| Ahvaz | اهواز | 31.32°N, 48.67°E |
| Kermanshah | کرمانشاه | 34.31°N, 47.07°E |
| Rasht | رشت | 37.28°N, 49.58°E |
| Yazd | یزد | 31.90°N, 54.36°E |

## 📋 Requirements

- Kotlin 1.9.22 or higher
- Java 17 or higher
- Gradle 8.5 or higher (wrapper included)

## 🚀 Quick Start

### Clone and Build

```bash
git clone https://github.com/RevEngine3r/iran-prayer-times-kt.git
cd iran-prayer-times-kt
./gradlew build
```

### Run Examples

```bash
./gradlew run
```

## 💻 Usage

### Basic Usage - City-based Calculation

```kotlin
import ir.revengine3r.prayertimes.IranPrayerTimes
import ir.revengine3r.prayertimes.model.City
import java.time.format.DateTimeFormatter

fun main() {
    // Create instance for a city
    val prayerTimes = IranPrayerTimes(City.TEHRAN)
    
    // Calculate prayer times for today
    val times = prayerTimes.calculate()
    
    // Format and display
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    println("Fajr: ${times.fajr.format(formatter)}")
    println("Dhuhr: ${times.dhuhr.format(formatter)}")
    println("Asr: ${times.asr.format(formatter)}")
    println("Maghrib: ${times.maghrib.format(formatter)}")
    
    // Or use the built-in toString
    println(times.toString())
}
```

### Custom Date

```kotlin
import java.time.LocalDate

val prayerTimes = IranPrayerTimes(City.MASHHAD)
val ramadanStart = LocalDate.of(2026, 2, 28)
val times = prayerTimes.calculate(ramadanStart)
```

### Custom Coordinates

```kotlin
import ir.revengine3r.prayertimes.IranPrayerTimes
import java.time.LocalDate

val times = IranPrayerTimes.calculateForCoordinates(
    date = LocalDate.now(),
    latitude = 36.3264,  // Mashhad
    longitude = 59.5433,
    timeZone = "Asia/Tehran"
)
```

### Custom Calculator Parameters

```kotlin
import ir.revengine3r.prayertimes.calculator.PrayerTimeCalculator
import ir.revengine3r.prayertimes.IranPrayerTimes
import ir.revengine3r.prayertimes.model.City

val customCalculator = PrayerTimeCalculator(
    fajrAngle = 17.7,              // Degrees below horizon
    ishaAngle = 14.0,              // Degrees below horizon
    sunriseSunsetAltitude = -0.833, // Geometric correction
    asrShadowFactor = 1.0,         // 1.0 = Shafii, 2.0 = Hanafi
    maghribOffsetMinutes = 19      // Minutes after sunset
)

val prayerTimes = IranPrayerTimes(City.TABRIZ, customCalculator)
val times = prayerTimes.calculate()
```

### Format All Times

```kotlin
val times = IranPrayerTimes(City.SHIRAZ).calculate()
val formatted = times.formatAll("HH:mm:ss")

formatted.forEach { (name, time) ->
    println("$name: $time")
}
```

## 📚 API Documentation

### Main Classes

#### `IranPrayerTimes`
Main API for calculating prayer times.

```kotlin
class IranPrayerTimes(val city: City, private val calculator: PrayerTimeCalculator)
```

**Methods:**
- `calculate(date: LocalDate? = null): PrayerTimes` - Calculate prayer times

**Companion Methods:**
- `forCity(city: City): IranPrayerTimes` - Factory method
- `calculateForCoordinates(...)` - Calculate for custom location

#### `PrayerTimeCalculator`
Core calculator with astronomical algorithms.

```kotlin
class PrayerTimeCalculator(
    fajrAngle: Double = 17.7,
    ishaAngle: Double = 14.0,
    sunriseSunsetAltitude: Double = -0.833,
    asrShadowFactor: Double = 1.0,
    maghribOffsetMinutes: Long = 19
)
```

**Methods:**
- `calculate(date, latitude, longitude, timeZone): PrayerTimes`

#### `City` (Enum)
Pre-configured Iranian cities.

```kotlin
enum class City {
    TEHRAN, TABRIZ, MASHHAD, ISFAHAN, SHIRAZ,
    QOM, AHVAZ, KERMANSHAH, RASHT, YAZD
}
```

**Properties:**
- `persianName: String` - Persian name
- `latitude: Double` - Geographic latitude
- `longitude: Double` - Geographic longitude
- `timeZone: String` - IANA timezone
- `displayName: String` - English display name

#### `PrayerTimes` (Data Class)
Contains all calculated prayer times.

```kotlin
data class PrayerTimes(
    val fajr: ZonedDateTime,
    val sunrise: ZonedDateTime,
    val dhuhr: ZonedDateTime,
    val asr: ZonedDateTime,
    val sunset: ZonedDateTime,
    val maghrib: ZonedDateTime,
    val isha: ZonedDateTime,
    val midnight: ZonedDateTime
)
```

**Methods:**
- `formatAll(pattern: String): Map<String, String>` - Format all times
- `toString(): String` - Pretty-printed format

## 🔬 Calculation Methodology

### Prayer Time Definitions

- **Fajr**: Sun is 17.7° below horizon (dawn)
- **Sunrise**: Sun crosses horizon with 0.833° correction
- **Dhuhr**: Solar noon (midday)
- **Asr**: Shadow length equals object height + noon shadow (Shafii)
- **Sunset**: Sun crosses horizon
- **Maghrib**: 19 minutes after sunset
- **Isha**: Sun is 14° below horizon (night)
- **Midnight**: Midpoint between sunset and next Fajr

### Algorithms

1. **Julian Day Conversion** - Accurate date handling for astronomy
2. **Solar Position** - Calculates declination and equation of time
3. **Hour Angle** - Determines sun position relative to observer
4. **Time Conversion** - Converts astronomical time to local timezone

### Default Parameters (Iran)

- Fajr angle: **17.7°** (Institute of Geophysics, University of Tehran)
- Isha angle: **14°** (Shia Ithna Ashari)
- Maghrib offset: **19 minutes** after sunset
- Asr method: **Shafii** (shadow factor = 1.0)

## 📁 Project Structure

```
iran-prayer-times-kt/
├── build.gradle.kts              # Build configuration
├── settings.gradle.kts           # Project settings
├── gradle.properties             # Gradle properties
├── gradlew                       # Gradle wrapper (Unix)
├── gradlew.bat                   # Gradle wrapper (Windows)
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
└── src/
    └── main/
        └── kotlin/
            └── ir/revengine3r/prayertimes/
                ├── IranPrayerTimes.kt           # Main API
                ├── calculator/
                │   └── PrayerTimeCalculator.kt  # Core calculator
                ├── model/
                │   ├── City.kt                  # City enum
                │   └── PrayerTimes.kt           # Data class
                └── examples/
                    └── Main.kt                  # Usage examples
```

## 🧪 Examples

See `src/main/kotlin/ir/revengine3r/prayertimes/examples/Main.kt` for complete examples:

1. **Simple city calculation** - Basic usage with pre-configured cities
2. **Custom coordinates** - Calculate for any location
3. **Companion object methods** - Using factory methods
4. **Multiple cities comparison** - Compare times across cities
5. **Specific date calculation** - Calculate for future dates

## 🛠️ Building from Source

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run the examples
./gradlew run

# Generate JAR
./gradlew jar
```

## 🤝 Contributing

Contributions are welcome! Areas for contribution:

- ✅ Add more Iranian cities
- ✅ Add unit tests
- ✅ Support different calculation methods (e.g., MWL, ISNA)
- ✅ Add Hijri calendar support
- ✅ Optimize astronomical calculations
- ✅ Create Maven/Gradle package for JitPack
- ✅ Add Qibla direction calculation

### Contribution Guidelines

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow Kotlin coding conventions
4. Add tests for new features
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

## 📄 License

MIT License - feel free to use this library in your projects.

## 🙏 Acknowledgments

- Astronomical algorithms based on Jean Meeus's "Astronomical Algorithms"
- Prayer time methodology from Institute of Geophysics, University of Tehran
- Calculation parameters validated against official Iranian prayer time sources
- Inspired by the need for accurate, open-source prayer times in Iran

## 👨‍💻 Author

**RevEngine3r**
- GitHub: [@RevEngine3r](https://github.com/RevEngine3r)
- Website: [RevEngine3r.iR](https://www.RevEngine3r.iR)
- Location: Tabriz, Iran

## 📞 Support

If you find this library helpful, please give it a ⭐ on GitHub!

For issues, questions, or suggestions, please [open an issue](https://github.com/RevEngine3r/iran-prayer-times-kt/issues).

---

*Made with ❤️ for the Muslim community in Iran*