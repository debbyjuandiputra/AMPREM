package com.amprem.securevideo

import android.content.Context

object SessionManager {

    private const val PREFS_NAME = "app_session_prefs"
    private const val KEY_LOGIN_TIME = "login_time"

    // 15 menit dalam milidetik
    const val SESSION_DURATION_MS = 15 * 60 * 1000L

    fun markLoggedIn(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putLong(KEY_LOGIN_TIME, System.currentTimeMillis()).apply()
    }

    fun isSessionValid(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val loginTime = prefs.getLong(KEY_LOGIN_TIME, 0L)
        if (loginTime == 0L) return false
        return (System.currentTimeMillis() - loginTime) < SESSION_DURATION_MS
    }

    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_LOGIN_TIME).apply()
    }
}
