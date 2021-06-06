package com.alp.app.servicios

import android.content.Context
import android.content.SharedPreferences

object PreferencesSingleton {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context, PREFS_NOMBRE : String) {
        prefs = context.getSharedPreferences(PREFS_NOMBRE, Context.MODE_PRIVATE)
    }

    fun leer(key: String, value: String): String? {
        return prefs.getString(key, value)
    }

    fun leer(key: String, value: Long): Long? {
        return prefs.getLong(key, value)
    }

    fun leer(key: String, value: Boolean): Boolean? {
        return prefs.getBoolean(key, value)
    }

    fun escribir(key: String, value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(key, value)
            commit()
        }
    }

    fun escribir(key: String, value: Long) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putLong(key, value)
            commit()
        }
    }

    fun escribir(key: String, value: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean(key, value)
            commit()
        }
    }

    fun eliminar(key: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            remove(key)
            commit()
        }
    }
}