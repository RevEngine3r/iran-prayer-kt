package ir.revengine3r.prayertimes.model

/**
 * Enumeration of major Iranian cities with their geographic coordinates.
 *
 * @property persianName Persian name of the city
 * @property latitude Geographic latitude in degrees
 * @property longitude Geographic longitude in degrees
 * @property timeZone IANA timezone identifier
 */
enum class City(
    val persianName: String,
    val latitude: Double,
    val longitude: Double,
    val timeZone: String = "Asia/Tehran"
) {
    TEHRAN(
        persianName = "تهران",
        latitude = 35.6892,
        longitude = 51.3890
    ),
    TABRIZ(
        persianName = "تبریز",
        latitude = 38.0800,
        longitude = 46.2919
    ),
    MASHHAD(
        persianName = "مشهد",
        latitude = 36.3264,
        longitude = 59.5433
    ),
    ISFAHAN(
        persianName = "اصفهان",
        latitude = 32.6525,
        longitude = 51.6746
    ),
    SHIRAZ(
        persianName = "شیراز",
        latitude = 29.5918,
        longitude = 52.5837
    ),
    QOM(
        persianName = "قم",
        latitude = 34.6401,
        longitude = 50.8764
    ),
    AHVAZ(
        persianName = "اهواز",
        latitude = 31.3203,
        longitude = 48.6692
    ),
    KERMANSHAH(
        persianName = "کرمانشاه",
        latitude = 34.3142,
        longitude = 47.0650
    ),
    RASHT(
        persianName = "رشت",
        latitude = 37.2808,
        longitude = 49.5831
    ),
    YAZD(
        persianName = "یزد",
        latitude = 31.8974,
        longitude = 54.3569
    );

    /**
     * English display name (lowercase enum name with first letter capitalized).
     */
    val displayName: String
        get() = name.lowercase().replaceFirstChar { it.uppercase() }
}