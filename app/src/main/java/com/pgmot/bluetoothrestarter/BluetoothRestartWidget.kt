package com.pgmot.bluetoothrestarter

import android.app.ActivityManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews


/**
 * Implementation of App Widget functionality.
 */
class BluetoothRestartWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent?.action) {
            "com.pgmot.bluetoothrestarter.action.RESTART" -> restartBluetooth(context)
        }
    }

    private fun restartBluetooth(context: Context?) {
        val activityManager = context?.getSystemService(android.content.Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses("com.android.bluetooth")
    }

    companion object {
        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent("com.pgmot.bluetoothrestarter.action.RESTART")
            val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.bluetooth_restart_widget)

            views.setImageViewResource(R.id.restart, android.R.drawable.ic_menu_rotate)
            views.setOnClickPendingIntent(R.id.restart, pendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

