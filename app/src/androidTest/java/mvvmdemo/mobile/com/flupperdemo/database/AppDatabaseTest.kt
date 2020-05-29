package mvvmdemo.mobile.com.flupperdemo.database

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mvvmdemo.mobile.com.flupperdemo.model.Product
import mvvmdemo.mobile.com.flupperdemo.model.ProductsList
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.IOException
import java.security.AccessController.getContext

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var dataDao: DataDao
    private lateinit var db: AppDatabase
    private  lateinit var context:Context

    @Before
    fun createDb() {
        context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        dataDao = db?.DataDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db?.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {

        val jsonFileString = getJsonDataFromAsset(context, "products.json")
        val gson = Gson()
        val listProductType = object : TypeToken<ProductsList>() {}.type

        var productsList: ProductsList = gson.fromJson(jsonFileString, listProductType)
        var persons: List<Product> = productsList?.products
        dataDao.insertAll(persons)
        val productsItem = dataDao?.getAllProductsRecords()
        assertThat(productsItem, equalTo(persons))

    }


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