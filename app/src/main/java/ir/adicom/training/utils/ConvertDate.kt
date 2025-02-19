package ir.adicom.training.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDateRelativelyLegacy(dateString: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val inputDate: Date = formatter.parse(dateString)!! // Handle potential ParseException

    val calendar = Calendar.getInstance()
    val today = calendar.time

    calendar.time = inputDate

    val inputDay = calendar.get(Calendar.DAY_OF_YEAR)
    val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    return when {
        inputDay == todayDay -> "Today"
        inputDay == todayDay - 1 -> "Yesterday"
        else -> dateString
    }
}