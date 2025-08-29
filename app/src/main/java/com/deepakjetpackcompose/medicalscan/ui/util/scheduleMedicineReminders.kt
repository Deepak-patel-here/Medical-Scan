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
    val reminderTimes = getReminderTimes(frequency)
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
    // 1) Ensure permissions/settings
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!am.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
            // Optionally prompt user to allow exact alarms in Settings
            // ensureExactAlarmAllowed(context)
        }
    }

    // 2) Make each PendingIntent unique
    val requestCode = ("$medicineName-$triggerAtMillis").hashCode()

    val intent = Intent(context, ReminderReceiver::class.java).apply {
        action = "com.yourapp.MEDICINE_REMINDER"
        data = Uri.parse("app://reminder/$requestCode") // makes it unique
        putExtra("medicineName", medicineName)
        putExtra("triggerAt", triggerAtMillis)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // 3) Guard against past times
    val finalTrigger = max(triggerAtMillis, System.currentTimeMillis() + 1_000L)

    // 4) Schedule exact (fires in Doze too)
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        finalTrigger,
        pendingIntent
    )

    Log.d("reminder", "Scheduled $medicineName at ${Date(finalTrigger)} (rc=$requestCode)")
}

