import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.ZoneId

// --- City Enum ---
enum class IranCity(val displayName: String) {
    TEHRAN("Tehran"),
    TABRIZ("Tabriz"),
    MASHHAD("Mashhad"),
    ISFAHAN("Isfahan"),
    SHIRAZ("Shiraz"),
    QOM("Qom"),
    AHVAZ("Ahvaz"),
    KERMANSHAH("Kermanshah"),
    RASHT("Rasht"),
    YAZD("Yazd")
}

// --- Coordinates data holder ---
data class CityCoordinates(
    val latitude: Double,
    val longitude: Double,
    val timeZone: String
)

// --- Coordinates map ---
val CITY_COORDS: Map<IranCity, CityCoordinates> = mapOf(
    IranCity.TEHRAN to CityCoordinates(35.6892, 51.3890, "Asia/Tehran"),
    IranCity.TABRIZ to CityCoordinates(38.0800, 46.2919, "Asia/Tehran"),
    IranCity.MASHHAD to CityCoordinates(36.3264, 59.5433, "Asia/Tehran"),
    IranCity.ISFAHAN to CityCoordinates(32.6525, 51.6746, "Asia/Tehran"),
    IranCity.SHIRAZ to CityCoordinates(29.5918, 52.5837, "Asia/Tehran"),
    IranCity.QOM to CityCoordinates(34.6401, 50.8764, "Asia/Tehran"),
    IranCity.AHVAZ to CityCoordinates(31.3203, 48.6692, "Asia/Tehran"),
    IranCity.KERMANSHAH to CityCoordinates(34.3142, 47.0650, "Asia/Tehran"),
    IranCity.RASHT to CityCoordinates(37.2808, 49.5831, "Asia/Tehran"),
    IranCity.YAZD to CityCoordinates(31.8974, 54.3569, "Asia/Tehran")
)

// --- IranPrayerTimes wrapper ---
data class IranPrayerTimes(val city: IranCity) {
    fun compute(date: LocalDate? = null): PrayerTimes {
        val coords = CITY_COORDS[city] ?: error("Coordinates not found for $city")
        val zoneId = ZoneId.of(coords.timeZone)
        val effectiveDate = date ?: LocalDate.now(zoneId)

        // Branching by city if needed later
        when (city) {
            IranCity.TEHRAN -> {
                // Tehran-specific tweaks could go here
            }
            IranCity.TABRIZ -> {
                // Tabriz-specific tweaks could go here
            }
            else -> {
                // Other cities
            }
        }

        val calculator = IranPrayerCalculator()
        return calculator.compute(effectiveDate, coords.latitude, coords.longitude, coords.timeZone)
    }
}