package com.iamdilipkumar.randomiser.utilities

import android.content.Context
import android.content.SharedPreferences

/**
 * @project:    Randomiser
 * @version:    1.0
 * @author:     Dilip Kumar <dilipkumar4813@gmail.com>
 */
class AppPreference constructor(val context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    companion object {
        private var prefManager: AppPreference? = null

        fun getInstance(context: Context): AppPreference {
            if (prefManager == null) {
                prefManager = AppPreference(context)
            }
            return prefManager as AppPreference
        }
    }


    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, vararg defValue: Int): Int {
        val value = if (defValue.isNotEmpty()) defValue[0] else 0
        return prefs.getInt(key, value)

    }

    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, vararg defValue: Float): Float {
        val value: Float = if (defValue.isNotEmpty()) defValue[0] else 0.0f
        return prefs.getFloat(key, value)
    }

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, vararg defValue: Long): Long {
        val value = if (defValue.isNotEmpty()) defValue[0] else 0
        return prefs.getLong(key, value)
    }

    fun putString(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, vararg defValue: String): String? {
        val value = if (defValue.isNotEmpty()) defValue[0] else null
        return prefs.getString(key, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, vararg defValue: Boolean): Boolean {
        val value = defValue.isNotEmpty() && defValue[0]
        return prefs.getBoolean(key, value)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}