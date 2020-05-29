package mvvmdemo.mobile.com.flupperdemo.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mvvmdemo.mobile.com.flupperdemo.model.Product
import mvvmdemo.mobile.com.flupperdemo.model.ProductsList
import java.io.IOException
import java.util.concurrent.Executors

@Database(entities = arrayOf(Product::class),exportSchema = false, version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DataDao(): DataDao

    //https://gabrieltanner.org/blog/android-room
    companion object {

        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "todo-list.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .addCallback(object : RoomDatabase.Callback() {

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)

                    val lanuchFlag=PrefrencesHandler.getInstance(context)?.launchFlag

                    if(!lanuchFlag!!)
                    {
                        PrefrencesHandler.getInstance(context)?.launchFlag=true


                        GlobalScope.launch {

                            val jsonFileString = getJsonDataFromAsset(context, "products.json")

                            val gson = Gson()
                            val listProductType = object : TypeToken<ProductsList>() {}.type

                            var productsList: ProductsList = gson.fromJson(jsonFileString, listProductType)
                            var persons: ArrayList<Product> = productsList?.products as ArrayList<Product>

                            instance?.DataDao()?.insertAll(persons)
                        }
                    }



                }
            })
            .build()


        fun getJsonDataFromAsset(context: Context, fileName: String): String? {
            val jsonString: String
            try {
                jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }


    }




}