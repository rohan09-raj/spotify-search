package com.example.spotifysearch.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.spotifysearch.utils.Constants

class SharedPreference(context: Context) {
    private var preference = context.getSharedPreferences(PREF_NAME, PREF_MODE)

    companion object {
        private const val PREF_NAME = Constants.APP_NAME
        private const val PREF_MODE = Context.MODE_PRIVATE
        private val PREF_ACCESS_TOKEN = Pair("ACCESS_TOKEN", "")
        private val PREF_TOKEN_TYPE = Pair("TOKEN_TYPE", "")
        private val PREF_EXPIRES_IN = Pair("EXPIRES_IN", 0)
    }

    var accessToken: String?
        get() = preference.getString(PREF_ACCESS_TOKEN.first, PREF_ACCESS_TOKEN.second)
        set(value) = preference.edit {
            it.putString(PREF_ACCESS_TOKEN.first, value)
        }

    var tokenType: String?
        get() = preference.getString(PREF_TOKEN_TYPE.first, PREF_TOKEN_TYPE.second)
        set(value) = preference.edit {
            it.putString(PREF_TOKEN_TYPE.first, value)
        }

    var expiresIn: Int
        get() = preference.getInt(PREF_EXPIRES_IN.first, PREF_EXPIRES_IN.second)
        set(value) = preference.edit {
            it.putInt(PREF_EXPIRES_IN.first, value)
        }
}

private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
}