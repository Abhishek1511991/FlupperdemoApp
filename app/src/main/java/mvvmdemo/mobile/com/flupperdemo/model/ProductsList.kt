package mvvmdemo.mobile.com.flupperdemo.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName


data class ProductsList (

	@SerializedName("Products")
	val products : List<Product>
)