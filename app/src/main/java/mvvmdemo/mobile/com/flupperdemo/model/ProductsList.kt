package mvvmdemo.mobile.com.flupperdemo.model

import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import mvvmdemo.mobile.com.flupperdemo.utility.TypeConverter


data class ProductsList (

	@TypeConverters(TypeConverter::class)
	@SerializedName("Products")
	val products : List<Product>
)