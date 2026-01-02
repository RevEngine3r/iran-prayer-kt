package ir.revengine3r.prayertimes

import ir.revengine3r.prayertimes.calculator.PrayerTimeCalculator
import ir.revengine3r.prayertimes.model.City
import ir.revengine3r.prayertimes.model.PrayerTimes
import java.time.LocalDate
import java.time.ZoneId

/**
 * Main API for calculating prayer times for Iranian cities.
 *
 * This class provides a convenient interface for calculating Islamic prayer times
 * for pre-configured Iranian cities.
 *
 * @property city The Iranian city for which to calculate prayer times
 * @property calculator Custom calculator instance (optional)
 */
class IranPrayerTimes(
    val city: City,
    private val calculator: PrayerTimeCalculator = PrayerTimeCalculator()
) {
    /**
     * Calculates prayer times for the specified city.
     *
     * @param date Date for which to calculate prayer times (defaults to today in city's timezone)
     * @return PrayerTimes object containing all prayer times
     */
    fun calculate(date: LocalDate? = null): PrayerTimes {
        val zoneId = ZoneId.of(city.timeZone)
        val effectiveDate = date ?: LocalDate.now(zoneId)
        
        return calculator.calculate(
            date = effectiveDate,
            latitude = city.latitude,
            longitude = city.longitude,
            timeZone = city.timeZone
        )
    }

    companion object {
        /**
         * Creates an instance for a specific city.
         *
         * @param city The city for which to calculate prayer times
         * @return IranPrayerTimes instance
         */
        fun forCity(city: City): IranPrayerTimes = IranPrayerTimes(city)

        /**
         * Calculates prayer times for custom coordinates.
         *
         * @param date Date for calculation
         * @param latitude Geographic latitude in degrees
         * @param longitude Geographic longitude in degrees
         * @param timeZone IANA timezone identifier
         * @param calculator Custom calculator (optional)
         * @return PrayerTimes object
         */
        fun calculateForCoordinates(
            date: LocalDate = LocalDate.now(),
            latitude: Double,
            longitude: Double,
            timeZone: String = "Asia/Tehran",
            calculator: PrayerTimeCalculator = PrayerTimeCalculator()
        ): PrayerTimes {
            return calculator.calculate(date, latitude, longitude, timeZone)
        }
    }
}