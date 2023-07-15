package com.sovchilar.made.domain.usecases

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import java.util.Calendar
import javax.inject.Inject

@Module
@InstallIn(FragmentComponent::class)
class DateUseCase @Inject constructor() {
    @Provides
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