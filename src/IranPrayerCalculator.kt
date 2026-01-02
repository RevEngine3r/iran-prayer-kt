import java.time.*
import kotlin.math.*

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

class IranPrayerCalculator(
    private val fajrAngle: Double = 17.7,
    private val ishaAngle: Double = 14.0,
    private val sunriseSunsetAltitude: Double = -0.833,
    private val asrShadowFactor: Double = 1.0,
    private val maghribOffsetMinutes: Long = 19
) {
    fun compute(date: LocalDate, latitude: Double, longitude: Double, timeZone: String): PrayerTimes {
        val zoneId = ZoneId.of(timeZone)
        val midnightBaseNext = date.plusDays(1).atStartOfDay(zoneId)
        val tzOffsetMinutesNext = midnightBaseNext.offset.totalSeconds / 60

        val jd = julianDay(date)
        val (declination, equationOfTime) = solarParams(jd)
        val phi = Math.toRadians(latitude)
        val solarNoon = 720 - 4 * longitude - equationOfTime

        // Sunrise / Sunset
        val hSun = hourAngle(sunriseSunsetAltitude, phi, declination)
        val sunrise = toLocal(
            date, solarNoon - 4 * Math.toDegrees(hSun), tzOffsetMinutesNext, zoneId
        )
        val sunset = toLocal(
            date, solarNoon + 4 * Math.toDegrees(hSun), tzOffsetMinutesNext, zoneId
        )

        // Fajr / Isha
        val hFajr = hourAngle(-fajrAngle, phi, declination)
        val hIsha = hourAngle(-ishaAngle, phi, declination)
        val fajr = toLocal(date, solarNoon - 4 * Math.toDegrees(hFajr), tzOffsetMinutesNext, zoneId)
        val isha = toLocal(date, solarNoon + 4 * Math.toDegrees(hIsha), tzOffsetMinutesNext, zoneId)

        // Dhuhr
        val dhuhr = toLocal(date, solarNoon, tzOffsetMinutesNext, zoneId)

        // Asr
        val hAsr = hourAngleAsr(asrShadowFactor, phi, declination)
        val asr = toLocal(date, solarNoon + 4 * Math.toDegrees(hAsr), tzOffsetMinutesNext, zoneId)

        // Maghrib (sunset + offset)
        val maghrib = sunset.plusMinutes(maghribOffsetMinutes)

        // Midnight: midpoint between sunset and next day's Fajr
        val jdNext = julianDay(date.plusDays(1))
        val (declinationNext, eqTimeNext) = solarParams(jdNext)
        val solarNoonNext = 720 - 4 * longitude - eqTimeNext
        val hFajrNext = hourAngle(-fajrAngle, phi, declinationNext)
        val fajrNext = toLocal(
            date.plusDays(1),
            solarNoonNext - 4 * Math.toDegrees(hFajrNext),
            tzOffsetMinutesNext,
            zoneId
        )
        val midnight = sunset.plus(Duration.between(sunset, fajrNext).dividedBy(2))

        return PrayerTimes(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha, midnight)
    }

    // --- Astronomy helpers ---
    private fun solarParams(jd: Double): Pair<Double, Double> {
        val d = jd - 2451545.0
        val g = Math.toRadians(357.529 + 0.98560028 * d)
        val q = 280.459 + 0.98564736 * d
        val l = Math.toRadians((q + 1.915 * sin(g) + 0.020 * sin(2 * g)) % 360)
        val e = Math.toRadians(23.439 - 0.00000036 * d)
        val ra = atan2(cos(e) * sin(l), cos(l))
        val dec = asin(sin(e) * sin(l))
        val raDeg = (Math.toDegrees(ra) % 360)
        val eqTime = 4 * (((q % 360) - raDeg + 540) % 360 - 180)
        return Pair(dec, eqTime)
    }

    private fun hourAngle(altDeg: Double, phi: Double, dec: Double): Double {
        val alt = Math.toRadians(altDeg)
        val cosH = (sin(alt) - sin(phi) * sin(dec)) / (cos(phi) * cos(dec))
        return acos(cosH.coerceIn(-1.0, 1.0))
    }

    private fun hourAngleAsr(k: Double, phi: Double, dec: Double): Double {
        // k = 1 for Shafii (standard), 2 for Hanafi
        // Calculate the solar altitude angle when shadow length = k * object height
        val tanAlt = 1.0 / (k + tan(abs(phi - dec)))
        val altitude = atan(tanAlt)
        
        // Now calculate hour angle for this altitude
        val cosH = (sin(altitude) - sin(phi) * sin(dec)) / (cos(phi) * cos(dec))
        return acos(cosH.coerceIn(-1.0, 1.0))
    }

    private fun toLocal(
        baseDate: LocalDate,
        utcMinutes: Double,
        tzMinutes: Int,
        zoneId: ZoneId
    ): ZonedDateTime {
        val totalMinutes = utcMinutes + tzMinutes
        val totalSeconds = (totalMinutes * 60.0).roundToInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return baseDate
            .atStartOfDay(zoneId)
            .plusHours(hours.toLong())
            .plusMinutes(minutes.toLong())
            .plusSeconds(seconds.toLong())
    }

    private fun julianDay(date: LocalDate): Double {
        var y = date.year
        var m = date.monthValue
        val day = date.dayOfMonth
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = y / 100
        val b = 2 - a + a / 4
        return (365.25 * (y + 4716)).toInt() + (30.6001 * (m + 1)).toInt() + day + b - 1524.5
    }
}