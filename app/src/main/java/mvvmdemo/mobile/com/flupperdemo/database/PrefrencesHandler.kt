package mvvmdemo.mobile.com.flupperdemo.database

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color


class PrefrencesHandler {

    companion object
    {

        @Volatile
        private var INSTANCE: PrefrencesHandler? = null
        val APP_FIRST_TIME_LAUNCH = "app_launch"
        private val filename = "Keys"
        private var SP: SharedPreferences? = null

        @JvmStatic
        fun getInstance(context: Context?): PrefrencesHandler {
            return if (INSTANCE == null) {
                INSTANCE = createInstance()
                if (context != null) {
                    SP = context?.getApplicationContext()?.getSharedPreferences(filename, 0)
                    INSTANCE as PrefrencesHandler
                } else INSTANCE as PrefrencesHandler
            } else INSTANCE as PrefrencesHandler
        }

        private fun createInstance() = PrefrencesHandler()



    }

    //https://blog.teamtreehouse.com/making-sharedpreferences-easy-with-kotlin

     var launchFlag: Boolean
        get() = SP!!.getBoolean(APP_FIRST_TIME_LAUNCH, false)
        set(value) = SP!!.edit().let { it?.putBoolean(APP_FIRST_TIME_LAUNCH, value) }?.apply()!!




    fun clear() {
        val editor = SP!!.edit()
        editor.clear()
        editor.apply()
    }

    fun remove() {
        val editor = SP!!.edit()
        editor.remove(filename)
        editor.apply()
    }

}