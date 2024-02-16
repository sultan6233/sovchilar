package sovchilar.uz.domain.usecases

import java.util.Calendar

class DateUseCase  {
    fun compareExpireDate(date: String): Boolean {
        val dateArray = date.split("/")
        val month = dateArray[0].toInt()
        val year = dateArray[1].toInt()
        val currentYear = getCurrentYear()
        val currentMonth = getCurrentMonth()
        return if (year > currentYear) {
            true
        } else {
            month >= currentMonth
        }
    }

    private fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)-2000
    }

    private fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }
}