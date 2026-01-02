# Iran Prayer Times (Kotlin)

[![Kotlin](https://img.shields.io/badge/kotlin-1.9+-blue.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

A Kotlin library for calculating accurate Islamic prayer times for major cities in Iran using astronomical algorithms.

## Features

- 🕌 Calculate all daily prayer times (Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha, Midnight)
- 🇮🇷 Pre-configured coordinates for 10 major Iranian cities
- 🌍 Support for custom coordinates and timezones
- 🔬 Astronomical calculations using Julian day and solar position algorithms
- ⏰ Accurate timezone handling with `ZonedDateTime`
- 🎯 Configurable calculation parameters (Fajr/Isha angles, Asr shadow factor)

## Supported Cities

- Tehran (تهران)
- Tabriz (تبریز)
- Mashhad (مشهد)
- Isfahan (اصفهان)
- Shiraz (شیراز)
- Qom (قم)
- Ahvaz (اهواز)
- Kermanshah (کرمانشاه)
- Rasht (رشت)
- Yazd (یزد)

## Installation

### Manual

1. Clone this repository:
```bash
git clone https://github.com/RevEngine3r/iran-prayer-times-kt.git
```

2. Add the source files to your Kotlin project:
   - `src/IranPrayerCalculator.kt`
   - `src/IranPrayerTimes.kt`

## Usage

### Quick Start - City-based Calculation

```kotlin
import java.time.format.DateTimeFormatter

fun main() {
    // Choose a city
    val prayerTimes = IranPrayerTimes(IranCity.TEHRAN)
    
    // Compute prayer times for today
    val times = prayerTimes.compute()
    
    // Format and display
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    println("Prayer times for Tehran:")
    println("Fajr: ${times.fajr.format(formatter)}")
    println("Sunrise: ${times.sunrise.format(formatter)}")
    println("Dhuhr: ${times.dhuhr.format(formatter)}")
    println("Asr: ${times.asr.format(formatter)}")
    println("Sunset: ${times.sunset.format(formatter)}")
    println("Maghrib: ${times.maghrib.format(formatter)}")
    println("Isha: ${times.isha.format(formatter)}")
    println("Midnight: ${times.midnight.format(formatter)}")
}
```

### Custom Date

```kotlin
import java.time.LocalDate

val prayerTimes = IranPrayerTimes(IranCity.MASHHAD)
val specificDate = LocalDate.of(2026, 3, 21)
val times = prayerTimes.compute(specificDate)
```

### Custom Coordinates

```kotlin
import java.time.LocalDate
import java.time.ZoneId

val calculator = IranPrayerCalculator()
val today = LocalDate.now(ZoneId.of("Asia/Tehran"))
val latitude = 36.3264  // Mashhad
val longitude = 59.5433
val timeZone = "Asia/Tehran"

val times = calculator.compute(today, latitude, longitude, timeZone)
```

### Advanced Configuration

```kotlin
val calculator = IranPrayerCalculator(
    fajrAngle = 17.7,              // Fajr angle (degrees below horizon)
    ishaAngle = 14.0,              // Isha angle (degrees below horizon)
    sunriseSunsetAltitude = -0.833, // Sun altitude for sunrise/sunset
    asrShadowFactor = 1.0,         // Asr shadow factor (1.0 = Shafii, 2.0 = Hanafi)
    maghribOffsetMinutes = 19      // Minutes after sunset for Maghrib
)

val times = calculator.compute(
    LocalDate.now(), 
    35.6892,  // latitude
    51.3890,  // longitude
    "Asia/Tehran"
)
```

## Calculation Methodology

This library uses astronomical algorithms to calculate prayer times:

### Prayer Time Definitions

- **Fajr**: When the sun is 17.7° below the horizon (dawn)
- **Sunrise**: When the sun crosses the horizon (0.833° geometric correction)
- **Dhuhr**: Solar noon (midday)
- **Asr**: When shadow length equals object length + noon shadow (Shafii school)
- **Sunset**: When the sun crosses the horizon
- **Maghrib**: 19 minutes after sunset
- **Isha**: When the sun is 14° below the horizon (night)
- **Midnight**: Midpoint between sunset and next Fajr

### Algorithms

1. **Julian Day**: Converts Gregorian dates to Julian day numbers for astronomical calculations
2. **Solar Position**: Calculates solar declination and equation of time
3. **Hour Angle**: Determines sun position relative to observer's meridian
4. **Time Conversion**: Converts astronomical times to local timezone

### Calculation Parameters

The default parameters are optimized for Iran:
- Fajr angle: 17.7° (Institute of Geophysics, University of Tehran)
- Isha angle: 14° (Shia Ithna Ashari)
- Maghrib offset: 19 minutes after sunset
- Asr calculation: Shafii method (shadow factor = 1.0)

## Data Classes

### PrayerTimes

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

### CityCoordinates

```kotlin
data class CityCoordinates(
    val latitude: Double,
    val longitude: Double,
    val timeZone: String
)
```

## Examples

See `src/Main.kt` for complete working examples:

- `irtimes()`: Simple city-based calculation
- `ircalc()`: Custom coordinate calculation  
- `irall()`: Complete example with all prayer times

## Requirements

- Kotlin 1.9 or higher
- Java 8+ (for `java.time.*` API)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

### Ideas for Contribution

- Add more Iranian cities
- Add support for different calculation methods
- Create Gradle/Maven package
- Add unit tests
- Optimize astronomical calculations
- Add hijri calendar support

## License

MIT License - feel free to use this library in your projects.

## Acknowledgments

- Astronomical algorithms based on Jean Meeus's "Astronomical Algorithms"
- Prayer time calculation methodology from various Islamic authorities
- Inspired by the need for accurate prayer times in Iran

## Author

**RevEngine3r**
- GitHub: [@RevEngine3r](https://github.com/RevEngine3r)
- Website: [RevEngine3r.iR](https://www.RevEngine3r.iR)
- Location: Tabriz, Iran

---

*Made with ❤️ for the Muslim community in Iran*