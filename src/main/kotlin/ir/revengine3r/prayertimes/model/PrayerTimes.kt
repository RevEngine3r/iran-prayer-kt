package ir.revengine3r.prayertimes.model

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Data class representing Islamic prayer times for a specific date.
 *
 * @property fajr Dawn prayer time (before sunrise)
 * @property sunrise Sunrise time
 * @property dhuhr Noon prayer time
 * @property asr Afternoon prayer time
 * @property sunset Sunset time
 * @property maghrib Evening prayer time (after sunset)
 * @property isha Night prayer time
 * @property midnight Islamic midnight (midpoint between sunset and next Fajr)
 */
data class PrayerTimes(
    val fajr: ZonedDateTime,
    val sunrise: ZonedDateTime,
    val dhuhr: ZonedDateTime,
    val asr: ZonedDateTime,
    val sunset: ZonedDateTime,
    val maghrib: ZonedDateTime,
    val isha: ZonedDateTime,
    val midnight: ZonedDateTime
) {
    /**
     * Formats all prayer times using the specified pattern.
     *
     * @param pattern Date-time pattern (e.g., "HH:mm", "HH:mm:ss")
     * @return Map of prayer names to formatted time strings
     */
    fun formatAll(pattern: String = "HH:mm"): Map<String, String> {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return mapOf(
            "Fajr" to fajr.format(formatter),
            "Sunrise" to sunrise.format(formatter),
            "Dhuhr" to dhuhr.format(formatter),
            "Asr" to asr.format(formatter),
            "Sunset" to sunset.format(formatter),
            "Maghrib" to maghrib.format(formatter),
            "Isha" to isha.format(formatter),
            "Midnight" to midnight.format(formatter)
        )
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return """
            Prayer Times:
            Fajr:     ${fajr.format(formatter)}
            Sunrise:  ${sunrise.format(formatter)}
            Dhuhr:    ${dhuhr.format(formatter)}
            Asr:      ${asr.format(formatter)}
            Sunset:   ${sunset.format(formatter)}
            Maghrib:  ${maghrib.format(formatter)}
            Isha:     ${isha.format(formatter)}
            Midnight: ${midnight.format(formatter)}
        """.trimIndent()
    }
}