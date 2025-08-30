package com.deepakjetpackcompose.medicalscan.ui.util

import java.util.Calendar



fun getReminderTimes(frequency: Int, testMode: Boolean = false): List<Calendar> {
    val times = mutableListOf<Calendar>()
    val now = Calendar.getInstance()

    if (testMode) {
        // In test mode, just fire a reminder 1 minute later
        times.add(Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
        })
        return times
    }

    when (frequency) {
        1 -> {
            times.add(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
            })
        }
        2 -> {
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 9); set(Calendar.MINUTE, 0) })
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 21); set(Calendar.MINUTE, 0) })
        }
        3 -> {
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 9); set(Calendar.MINUTE, 0) })
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 14); set(Calendar.MINUTE, 0) })
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 21); set(Calendar.MINUTE, 0) })
        }
        4 -> {
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 9); set(Calendar.MINUTE, 0) })
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 13); set(Calendar.MINUTE, 0) })
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 17); set(Calendar.MINUTE, 0) })
            times.add(Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 21); set(Calendar.MINUTE, 0) })
        }
    }

    // If any reminder time is already past today, schedule it for tomorrow
    return times.map {
        if (it.before(now)) {
            it.add(Calendar.DAY_OF_YEAR, 1)
        }
        it
    }
}


