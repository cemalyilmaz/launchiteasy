package com.sherepenko.android.launchiteasy.livedata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import com.sherepenko.android.launchiteasy.data.AppState

class AppStateLiveData(
    private val context: Context
) : LiveData<Event<AppState>>() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_PACKAGE_ADDED -> {
                    postValue(Event(AppState.ADDED))
                }
                Intent.ACTION_PACKAGE_CHANGED -> {
                    postValue(Event(AppState.CHANGED))
                }
                Intent.ACTION_PACKAGE_REMOVED -> {
                    postValue(Event(AppState.REMOVED))
                }
                Intent.ACTION_PACKAGE_REPLACED -> {
                    postValue(Event(AppState.REPLACED))
                }
                else -> {
                    // ignore
                }
            }
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_PACKAGE_ADDED)
        addAction(Intent.ACTION_PACKAGE_CHANGED)
        addAction(Intent.ACTION_PACKAGE_REMOVED)
        addAction(Intent.ACTION_PACKAGE_REPLACED)
        addDataScheme("package")
    }

    init {
        postValue(Event(AppState.INITIAL))
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(receiver, intentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(receiver)
    }
}
