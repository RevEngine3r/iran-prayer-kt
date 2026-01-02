package ir.revengine3r.prayertimes.examples

import ir.revengine3r.prayertimes.IranPrayerTimes
import ir.revengine3r.prayertimes.calculator.PrayerTimeCalculator
import ir.revengine3r.prayertimes.model.City
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Example 1: Simple city-based calculation
 */
fun simpleCityExample() {
    println("=== Simple City Example ===")
    
    val prayerTimes = IranPrayerTimes(City.TABRIZ)
    val times = prayerTimes.calculate()
    
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    println("Prayer times for ${prayerTimes.city.displayName} (${prayerTimes.city.persianName}):")
    println("Fajr:     ${times.fajr.format(formatter)}")
    println("Sunrise:  ${times.sunrise.format(formatter)}")
    println("Dhuhr:    ${times.dhuhr.format(formatter)}")
    println("Asr:      ${times.asr.format(formatter)}")
    println("Sunset:   ${times.sunset.format(formatter)}")
    println("Maghrib:  ${times.maghrib.format(formatter)}")
    println("Isha:     ${times.isha.format(formatter)}")
    println("Midnight: ${times.midnight.format(formatter)}")
    println()
}

/**
 * Example 2: Custom coordinates calculation
 */
fun customCoordinatesExample() {
    println("=== Custom Coordinates Example ===")
    
    val calculator = PrayerTimeCalculator()
    val today = LocalDate.now(ZoneId.of("Asia/Tehran"))
    val times = calculator.calculate(
        date = today,
        latitude = 38.0800,  // Tabriz
        longitude = 46.2919,
        timeZone = "Asia/Tehran"
    )
    
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    println("Prayer times for custom coordinates:")
    println("Fajr:     ${times.fajr.format(formatter)}")
    println("Sunrise:  ${times.sunrise.format(formatter)}")
    println("Dhuhr:    ${times.dhuhr.format(formatter)}")
    println("Asr:      ${times.asr.format(formatter)}")
    println("Sunset:   ${times.sunset.format(formatter)}")
    println("Maghrib:  ${times.maghrib.format(formatter)}")
    println("Isha:     ${times.isha.format(formatter)}")
    println("Midnight: ${times.midnight.format(formatter)}")
    println()
}

/**
 * Example 3: Using the companion object methods
 */
fun companionObjectExample() {
    println("=== Companion Object Example ===")
    
    val times = IranPrayerTimes.calculateForCoordinates(
        latitude = 35.6892,  // Tehran
        longitude = 51.3890,
        timeZone = "Asia/Tehran"
    )
    
    println("Prayer times using companion object:")
    println(times.toString())
    println()
}

/**
 * Example 4: Multiple cities comparison
 */
fun multipleCitiesExample() {
    println("=== Multiple Cities Comparison ===")
    
    val cities = listOf(City.TEHRAN, City.TABRIZ, City.MASHHAD, City.SHIRAZ)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    
    cities.forEach { city ->
        val times = IranPrayerTimes.forCity(city).calculate()
        println("${city.displayName.padEnd(12)} - Fajr: ${times.fajr.format(formatter)}, " +
                "Dhuhr: ${times.dhuhr.format(formatter)}, " +
                "Maghrib: ${times.maghrib.format(formatter)}")
    }
    println()
}

/**
 * Example 5: Specific date calculation
 */
fun specificDateExample() {
    println("=== Specific Date Example ===")
    
    val prayerTimes = IranPrayerTimes(City.ISFAHAN)
    val ramadanStart = LocalDate.of(2026, 2, 28)  // Example date
    val times = prayerTimes.calculate(ramadanStart)
    
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
    println("Prayer times for ${prayerTimes.city.displayName} on ${ramadanStart}:")
    times.formatAll("HH:mm").forEach { (name, time) ->
        println("$name: $time")
    }
    println()
}

fun main() {
    simpleCityExample()
    customCoordinatesExample()
    companionObjectExample()
    multipleCitiesExample()
    specificDateExample()
}