package mvvmdemo.mobile.com.flupperdemo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import mvvmdemo.mobile.com.flupperdemo.model.Product

@Dao
interface DataDao {

    @Query("SELECT * from product")
    fun getAllProducts(): LiveData<List<Product>>


    @Query("SELECT * from product")
    fun getAllProductsRecords(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mProduct: Product?)


    @Insert
    fun insertAll(users: List<Product>)

    @Query("DELETE FROM product")
    fun deleteAll()

    @Delete
    fun delete(product: Product?)

    @Update
    fun update(product: Product?)

}