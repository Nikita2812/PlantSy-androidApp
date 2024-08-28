package com.nikita_prasad.plantsy

import android.content.Context

object Utils {
        object ContextUtils {
            private lateinit var appContext: Context

            fun initialize(context: Context) {
                appContext = context.applicationContext
            }

            fun getApplicationContext(): Context {
                return appContext
            }
        }
}