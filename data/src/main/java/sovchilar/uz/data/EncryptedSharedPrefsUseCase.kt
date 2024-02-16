package sovchilar.uz.data

import android.content.Context
import android.content.Context.MODE_PRIVATE


class EncryptedSharedPrefsUseCase(val context: Context) {

    fun writeIntoFile(data: String, value: String) {
        val sharedPreferences = context.getSharedPreferences(data, MODE_PRIVATE)
        sharedPreferences.edit().putString(data, value).apply()
    }

    fun readFromFile(data: String): String {
        val sharedPreferences = context.getSharedPreferences(data, MODE_PRIVATE)
        return sharedPreferences.getString(data, null).toString()
    }

    fun readBoolean(data: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(data, MODE_PRIVATE)
        return sharedPreferences.getBoolean(data, true)
    }

    fun authenticated(data: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(data, MODE_PRIVATE)
        return sharedPreferences.getBoolean(data, false)
    }

    fun saveAuthState(data: String) {
        val sharedPreferences = context.getSharedPreferences(data, MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(data, true).apply()
    }
}