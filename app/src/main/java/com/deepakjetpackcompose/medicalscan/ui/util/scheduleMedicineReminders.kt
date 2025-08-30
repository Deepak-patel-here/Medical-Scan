package com.deepakjetpackcompose.medicalscan.ui.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.deepakjetpackcompose.medicalscan.ui.service.ReminderReceiver
import java.util.Date
import kotlin.math.max

fun scheduleMedicineReminders(context: Context, medicineName: String, frequency: Int) {
    val reminderTimes = getReminderTimes(frequency) // testMode=true for 1-min test
    for (time in reminderTimes) {
        Log.d("ReminderDebug", "Scheduling $medicineName at ${Date(time.timeInMillis)}")
        scheduleReminder(context, time.timeInMillis, medicineName)
    }
}

fun scheduleReminder(
    context: Context,
    triggerAtMillis: Long,
    medicineName: String
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Check exact alarm permission for Android 12+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    // Make request code unique per medicine + trigger time
    val requestCode = ("$medicineName-$triggerAtMillis").hashCode()

    val intent = Intent(context, ReminderReceiver::class.java).apply {
        action = "com.deepakjetpackcompose.medicalscan.MEDICINE_REMINDER"
        data = Uri.parse("app://reminder/$requestCode") // ensures uniqueness
        putExtra("medicineName", medicineName)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Ensure trigger is at least 1 second in the future
    val finalTrigger = max(triggerAtMillis, System.currentTimeMillis() + 1_000L)

    // Use exact alarm that can wake device
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            finalTrigger,
            pendingIntent
        )
    } else {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            finalTrigger,
            pendingIntent
        )
    }

    Log.d("ReminderDebug", "Scheduled $medicineName at ${Date(finalTrigger)} (rc=$requestCode)")
}
