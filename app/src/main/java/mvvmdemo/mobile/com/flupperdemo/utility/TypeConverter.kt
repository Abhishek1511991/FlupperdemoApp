package mvvmdemo.mobile.com.flupperdemo.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mvvmdemo.mobile.com.flupperdemo.model.Colors
import mvvmdemo.mobile.com.flupperdemo.model.Store
import java.util.*
import kotlin.collections.ArrayList

class TypeConverter {

    @TypeConverter
    fun fromString(value: String?): ArrayList<Colors> {
        val listType =
            object : TypeToken<ArrayList<Colors>>() {}.type
        return Gson().fromJson<ArrayList<Colors>>(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Colors?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringToArrayList(value: String): ArrayList<Store> {
        val listType =
            object : TypeToken<ArrayList<Store>>() {}.type
        return Gson().fromJson<ArrayList<Store>>(value, listType)
    }

    @TypeConverter
    fun fromArrayListToString(list: ArrayList<Store>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }


    companion object
    {
        fun StringToBitMap(encodedString: String): Bitmap? {
            return try {
                val encodeByte =
                    Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            } catch (e: Exception) {
                e.message
                null
            }
        }
    }

}