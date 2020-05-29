package mvvmdemo.mobile.com.flupperdemo.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import mvvmdemo.mobile.com.flupperdemo.database.AppDatabase
import mvvmdemo.mobile.com.flupperdemo.database.DataDao
import mvvmdemo.mobile.com.flupperdemo.model.Product

class RecordRepo(application: Application)
{
    private var products: LiveData<List<Product>>?=null
    private var dataDao:DataDao?=null

    init {
            dataDao=AppDatabase(application).DataDao()
            products=dataDao?.getAllProducts()
    }

    fun getAllProducts(): LiveData<List<Product>> {
        return products!!
    }


    fun insert(product: Product?)
    {
        dataDao?.insert(product)
    }

    fun update(product: Product?)
    {
        dataDao?.update(product)
    }

    fun delete(product: Product?)
    {
        dataDao?.delete(product)
    }






}