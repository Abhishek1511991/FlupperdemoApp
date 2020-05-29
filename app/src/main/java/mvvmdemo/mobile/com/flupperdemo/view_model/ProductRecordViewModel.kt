package mvvmdemo.mobile.com.flupperdemo.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import mvvmdemo.mobile.com.flupperdemo.model.Product

class ProductRecordViewModel(application: Application):AndroidViewModel(application) {


    var allProduct:LiveData<List<Product>>?=null
    var mProductRepository:RecordRepo?=null

    init {
        mProductRepository=RecordRepo(application)
        allProduct=mProductRepository?.getAllProducts()
    }

    fun getAllProducts():LiveData<ArrayList<Product>>
    {
        return allProduct!! as LiveData<ArrayList<Product>>
    }

    fun insert(product: Product?) {
        mProductRepository?.insert(product)
    }

    fun update(product: Product?) {
        mProductRepository?.update(product)
    }

    fun delete(product: Product?) {
        mProductRepository?.delete(product)
    }


}