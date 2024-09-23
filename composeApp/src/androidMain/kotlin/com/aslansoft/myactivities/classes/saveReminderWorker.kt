package com.aslansoft.myactivities.classes

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class saveReminderWorker(
    context: Context,
    workerParams: WorkerParameters,

): Worker(context, workerParams) {
    override fun doWork(): Result {

            saveRemindersToPrefs(applicationContext)

        return Result.success()
    }

}

class ClearReminderWorker(
    context: Context,
    workerParams: WorkerParameters
): Worker(context, workerParams) {
    override fun doWork(): Result {
        clearRemindersToPrefs(applicationContext)
        return Result.success()
    }

}