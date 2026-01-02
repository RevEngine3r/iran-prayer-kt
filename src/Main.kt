import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun irtimes() {
    val ipt = IranPrayerTimes(IranCity.TABRIZ)
    val times = ipt.compute()
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
    println("Fajr: ${times.fajr.format(formatter)}")
    println("Sunrise: ${times.sunrise.format(formatter)}")
    println("Zuhr: ${times.dhuhr.format(formatter)}")
    println("Asr: ${times.asr.format(formatter)}")
    println("Sunset: ${times.sunset.format(formatter)}")
    println("Maghrib: ${times.maghrib.format(formatter)}")
    println("Isha: ${times.isha.format(formatter)}")
    println("Midnight: ${times.midnight.format(formatter)}")
}

fun ircalc() {
    val calculator = IranPrayerCalculator()
    val today = LocalDate.now(ZoneId.of("Asia/Tehran"))
    val latitude = 38.0800
    val longitude = 46.2919
    val timeZone = "Asia/Tehran"
    val times = calculator.compute(today, latitude, longitude, timeZone)
    println("Fajr: ${times.fajr.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))}")
    println("Sunrise: ${times.sunrise.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))}")
    println("Dhuhr: ${times.dhuhr.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))}")
    println("Sunset: ${times.sunset.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))}")
    println("Maghrib: ${times.maghrib.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))}")
    println("Midnight: ${times.midnight.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))}")
}

fun irall() {
    // Choose a city (Tabriz in this example)
    val prayerTimesForCity = IranPrayerTimes(IranCity.TABRIZ)
    // Compute prayer times for today (defaults to today's date in that city's timezone)
    val times = prayerTimesForCity.compute()
    // Formatter for output
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
    // Print results
    println("Prayer times for ${prayerTimesForCity.city.displayName}:")
    println("Fajr: ${times.fajr.format(formatter)}")
    println("Sunrise: ${times.sunrise.format(formatter)}")
    println("Dhuhr: ${times.dhuhr.format(formatter)}")
    println("Asr: ${times.asr.format(formatter)}")
    println("Sunset: ${times.sunset.format(formatter)}")
    println("Maghrib: ${times.maghrib.format(formatter)}")
    println("Isha: ${times.isha.format(formatter)}")
    println("Midnight: ${times.midnight.format(formatter)}")
}

fun main() {
    ircalc()
    irtimes()
    irall()
}