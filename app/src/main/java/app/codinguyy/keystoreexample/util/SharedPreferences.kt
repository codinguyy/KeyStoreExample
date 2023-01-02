package app.codinguyy.keystoreexample.util

import android.content.Context

open class SharedPreferences(context: Context) {
    val sharedPref = context.getSharedPreferences(SHARED_PREF, 0)
    val editor = sharedPref.edit()

    companion object {
        val SHARED_PREF = "SharedPref"
        val Shared_Pref_Password = "password"
        val Shared_Pref_Iv = "iv"
    }

    var iv: String?
        get() = sharedPref.getString(Shared_Pref_Iv, "")
        set(value) {
            editor.putString(Shared_Pref_Iv, value).apply()
        }

    var password: String?
        get() = sharedPref.getString(Shared_Pref_Password, "")
        set(value) {
            editor.putString(Shared_Pref_Password, value).apply()
        }
}
