package ir.revengine3r.prayertimes.calculator

import ir.revengine3r.prayertimes.model.PrayerTimes
import java.time.*
import kotlin.math.*

/**
 * Calculator for Islamic prayer times using astronomical algorithms.
 *
 * This implementation uses:
 * - Julian day calculations for accurate date handling
 * - Solar position algorithms (declination, equation of time)
 * - Hour angle calculations for different prayer times
 *
 * @property fajrAngle Sun angle below horizon for Fajr (degrees)
 * @property ishaAngle Sun angle below horizon for Isha (degrees)
 * @property sunriseSunsetAltitude Geometric altitude for sunrise/sunset calculations
 * @property asrShadowFactor Shadow length ratio for Asr (1.0 = Shafii, 2.0 = Hanafi)
 * @property maghribOffsetMinutes Minutes to add after sunset for Maghrib
 */
class PrayerTimeCalculator(
    private val fajrAngle: Double = 17.7,
    private val ishaAngle: Double = 14.0,
    private val sunriseSunsetAltitude: Double = -0.833,
    private val asrShadowFactor: Double = 1.0,
    private val maghribOffsetMinutes: Long = 21  // Adjusted from 19 to 21
) {
    /**
     * Computes prayer times for a specific date and location.
     *
     * @param date Date for which to calculate prayer times
     * @param latitude Geographic latitude in degrees (positive for North)
     * @param longitude Geographic longitude in degrees (positive for East)
     * @param timeZone IANA timezone identifier (e.g., "Asia/Tehran")
     * @return PrayerTimes object containing all prayer times
     */
    fun calculate(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        timeZone: String
    ): PrayerTimes {
        val zoneId = ZoneId.of(timeZone)
        // Use current date's midnight for timezone offset calculation
        val midnightBase = date.atStartOfDay(zoneId)
        val tzOffsetMinutes = midnightBase.offset.totalSeconds / 60

        val julianDay = calculateJulianDay(date)
        val (declination, equationOfTime) = calculateSolarParameters(julianDay)
        val latitudeRad = Math.toRadians(latitude)
        val solarNoon = 720.0 - 4.0 * longitude - equationOfTime

        // Sunrise and Sunset
        val sunHourAngle = calculateHourAngle(sunriseSunsetAltitude, latitudeRad, declination)
        val sunrise = convertToLocalTime(
            date, solarNoon - 4.0 * Math.toDegrees(sunHourAngle), tzOffsetMinutes, zoneId
        )
        val sunset = convertToLocalTime(
            date, solarNoon + 4.0 * Math.toDegrees(sunHourAngle), tzOffsetMinutes, zoneId
        )

        // Fajr and Isha
        val fajrHourAngle = calculateHourAngle(-fajrAngle, latitudeRad, declination)
        val ishaHourAngle = calculateHourAngle(-ishaAngle, latitudeRad, declination)
        val fajr = convertToLocalTime(
            date, solarNoon - 4.0 * Math.toDegrees(fajrHourAngle), tzOffsetMinutes, zoneId
        )
        val isha = convertToLocalTime(
            date, solarNoon + 4.0 * Math.toDegrees(ishaHourAngle), tzOffsetMinutes, zoneId
        )

        // Dhuhr (solar noon)
        val dhuhr = convertToLocalTime(date, solarNoon, tzOffsetMinutes, zoneId)

        // Asr
        val asrHourAngle = calculateAsrHourAngle(asrShadowFactor, latitudeRad, declination)
        val asr = convertToLocalTime(
            date, solarNoon + 4.0 * Math.toDegrees(asrHourAngle), tzOffsetMinutes, zoneId
        )

        // Maghrib (sunset + offset)
        val maghrib = sunset.plusMinutes(maghribOffsetMinutes)

        // Midnight (midpoint between sunset and next Fajr)
        val nextJulianDay = calculateJulianDay(date.plusDays(1))
        val (nextDeclination, nextEqTime) = calculateSolarParameters(nextJulianDay)
        val nextSolarNoon = 720.0 - 4.0 * longitude - nextEqTime
        val nextFajrHourAngle = calculateHourAngle(-fajrAngle, latitudeRad, nextDeclination)
        val nextFajr = convertToLocalTime(
            date.plusDays(1),
            nextSolarNoon - 4.0 * Math.toDegrees(nextFajrHourAngle),
            tzOffsetMinutes,
            zoneId
        )
        val midnight = sunset.plus(Duration.between(sunset, nextFajr).dividedBy(2))

        return PrayerTimes(fajr, sunrise, dhuhr, asr, sunset, maghrib, isha, midnight)
    }

    /**
     * Calculates solar parameters (declination and equation of time) for a given Julian day.
     */
    private fun calculateSolarParameters(julianDay: Double): Pair<Double, Double> {
        val daysSinceEpoch = julianDay - 2451545.0
        val meanAnomaly = Math.toRadians(357.529 + 0.98560028 * daysSinceEpoch)
        val meanLongitude = 280.459 + 0.98564736 * daysSinceEpoch
        val eclipticLongitude = Math.toRadians(
            (meanLongitude + 1.915 * sin(meanAnomaly) + 0.020 * sin(2 * meanAnomaly)) % 360
        )
        val obliquity = Math.toRadians(23.439 - 0.00000036 * daysSinceEpoch)
        
        val rightAscension = atan2(cos(obliquity) * sin(eclipticLongitude), cos(eclipticLongitude))
        val declination = asin(sin(obliquity) * sin(eclipticLongitude))
        
        // Normalize right ascension to 0-360 range
        var rightAscensionDegrees = Math.toDegrees(rightAscension)
        if (rightAscensionDegrees < 0) {
            rightAscensionDegrees += 360
        }
        
        // Normalize mean longitude
        val normalizedMeanLongitude = ((meanLongitude % 360) + 360) % 360
        
        // Calculate equation of time with proper angle wrapping
        var eqTimeDelta = normalizedMeanLongitude - rightAscensionDegrees
        if (eqTimeDelta > 180) eqTimeDelta -= 360
        if (eqTimeDelta < -180) eqTimeDelta += 360
        val equationOfTime = 4.0 * eqTimeDelta
        
        return Pair(declination, equationOfTime)
    }

    /**
     * Calculates hour angle for a given altitude, latitude, and solar declination.
     */
    private fun calculateHourAngle(altitudeDegrees: Double, latitude: Double, declination: Double): Double {
        val altitude = Math.toRadians(altitudeDegrees)
        val cosHourAngle = (sin(altitude) - sin(latitude) * sin(declination)) / (cos(latitude) * cos(declination))
        return acos(cosHourAngle.coerceIn(-1.0, 1.0))
    }

    /**
     * Calculates hour angle for Asr prayer based on shadow length ratio.
     */
    private fun calculateAsrHourAngle(shadowFactor: Double, latitude: Double, declination: Double): Double {
        val tanAltitude = 1.0 / (shadowFactor + tan(abs(latitude - declination)))
        val altitude = atan(tanAltitude)
        val cosHourAngle = (sin(altitude) - sin(latitude) * sin(declination)) / (cos(latitude) * cos(declination))
        return acos(cosHourAngle.coerceIn(-1.0, 1.0))
    }

    /**
     * Converts UTC minutes to local time for a specific date and timezone.
     * Uses proper rounding (round half up) to avoid systematic bias.
     */
    private fun convertToLocalTime(
        date: LocalDate,
        utcMinutes: Double,
        timezoneOffsetMinutes: Int,
        zoneId: ZoneId
    ): ZonedDateTime {
        val totalMinutes = utcMinutes + timezoneOffsetMinutes
        
        // Use proper rounding: add 0.5 before converting to int (round half up)
        val totalSeconds = ((totalMinutes * 60.0) + 0.5).toInt()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        
        return date
            .atStartOfDay(zoneId)
            .plusHours(hours.toLong())
            .plusMinutes(minutes.toLong())
            .plusSeconds(seconds.toLong())
    }

    /**
     * Calculates Julian day number for a given Gregorian date.
     */
    private fun calculateJulianDay(date: LocalDate): Double {
        var year = date.year
        var month = date.monthValue
        val day = date.dayOfMonth
        
        if (month <= 2) {
            year -= 1
            month += 12
        }
        
        val a = year / 100
        val b = 2 - a + a / 4
        
        return (365.25 * (year + 4716)).toInt() + 
               (30.6001 * (month + 1)).toInt() + 
               day + b - 1524.5
    }
}