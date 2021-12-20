package com.listocalixto.android.ecommerce.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SharedPref(activity: Activity) {

    private var prefs: SharedPreferences? = null

    init {
        prefs = activity.getSharedPreferences(
            "com.listocalixto.android.ecommerce",
            Context.MODE_PRIVATE
        )

    }

    fun save(key: String, item: Any) {
        try {
            val gson = Gson()
            val json = gson.toJson(item)
            with(prefs?.edit()) {
                this?.putString(key, json)
                this?.commit()
            }
        } catch (e: Exception) {
            Log.e(TAG, "save: An error was happened - ${e.message}", e)
        }
    }

    fun getData(key: String): String? {
        return prefs?.getString(key, "")
    }

    companion object {
        private const val TAG = "SharedPref"
    }

}